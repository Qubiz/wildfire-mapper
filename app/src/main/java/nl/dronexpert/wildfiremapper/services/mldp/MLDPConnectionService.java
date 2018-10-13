package nl.dronexpert.wildfiremapper.services.mldp;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.RxBleDeviceServices;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import nl.dronexpert.wildfiremapper.services.base.BaseService;
import nl.dronexpert.wildfiremapper.services.mldp.events.ConnectionEvent;
import nl.dronexpert.wildfiremapper.services.mldp.events.DataReceivedEvent;
import nl.dronexpert.wildfiremapper.services.mldp.utils.MLDPConstants;
import nl.dronexpert.wildfiremapper.services.mldp.utils.MLDPUtils;

import static com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class MLDPConnectionService extends BaseService {

    public static final String TAG = MLDPConnectionService.class.getSimpleName();

    @Inject
    RxBleClient rxBleClient;

    private RxBleDevice rxBleDevice;

    private RxBleConnection rxBleConnection;

    private Disposable connectionStateDisposable;
    private Disposable connectionDisposable;
    private Disposable notificationDisposable;

    private BluetoothGattCharacteristic mldpDataCharacteristic;
    private BluetoothGattCharacteristic mldpControlCharacteristic;
    private BluetoothGattCharacteristic txDataCharacteristic;
    private BluetoothGattCharacteristic rxDataCharacteristic;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MLDPConnectionService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getServiceComponent().inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void connect(final String macAddress) {
        if (isConnected() && rxBleDevice.getMacAddress().equals(macAddress)) {
            disconnect();
        } else {
            rxBleDevice = rxBleClient.getBleDevice(macAddress);

            connectionStateDisposable = rxBleDevice.observeConnectionStateChanges()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(this::onError)
                    .subscribe(this::onConnectionStateChanged, this::onError);

            connectionDisposable = rxBleDevice.establishConnection(false)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(this::onError)
                    .retry(2)
                    .subscribe(this::onConnectionReceived, this::onError);

            getCompositeDisposable().addAll(connectionStateDisposable, connectionDisposable);
        }
    }

    public void disconnect() {
        if (isConnected()) {
            EventBus.getDefault().post(new ConnectionEvent(RxBleConnectionState.DISCONNECTING, rxBleDevice.getName(), rxBleDevice.getMacAddress()));
            if (connectionDisposable != null) {
                notificationDisposable.dispose();
                connectionDisposable.dispose();
                connectionStateDisposable.dispose();
            }
            EventBus.getDefault().post(new ConnectionEvent(RxBleConnectionState.DISCONNECTED, rxBleDevice.getName(), rxBleDevice.getMacAddress()));
        }
    }

    private boolean isConnected() {
        return rxBleDevice != null && rxBleDevice.getConnectionState() == RxBleConnectionState.CONNECTED;
    }

    public void write(byte[] bytes) {
        BluetoothGattCharacteristic writeDataCharacteristic;
        if (mldpDataCharacteristic != null) {
            writeDataCharacteristic = mldpDataCharacteristic;
        } else {
            writeDataCharacteristic = rxDataCharacteristic;
        }

        getCompositeDisposable().add(rxBleConnection.writeCharacteristic(writeDataCharacteristic, bytes)
                .doOnError(this::onError)
                .subscribe());
    }

    @Nullable
    public RxBleDevice getConnectedDevice() {
        if (isConnected()) {
            return rxBleDevice;
        } else {
            return null;
        }
    }

    private void onConnectionReceived(RxBleConnection rxBleConnection) {
        this.rxBleConnection = rxBleConnection;
        getCompositeDisposable().add(rxBleConnection.discoverServices()
                .doOnError(this::onError)
                .subscribe(this::onServicesDiscovered, this::onError));
    }

    private void onServicesDiscovered(RxBleDeviceServices rxBleDeviceServices) {
        readGattServices(rxBleDeviceServices.getBluetoothGattServices());
    }

    private void readGattServices(@Nullable List<BluetoothGattService> gattServiceList) {
        if (gattServiceList != null) {
            for (BluetoothGattService gattService : gattServiceList) {
                if (MLDPUtils.isPrivateService(gattService)) {
                    readGattCharacteristics(gattService.getCharacteristics());
                }
            }
        }
    }

    private void readGattCharacteristics(List<BluetoothGattCharacteristic> gattCharacteristicsList) {
        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristicsList) {
            UUID uuid = gattCharacteristic.getUuid();

            if (uuid.equals(MLDPConstants.UUID_TRANSPARENT_TX_PRIVATE_CHAR)) {
                txDataCharacteristic = gattCharacteristic;
                if (MLDPUtils.isCharacteristicNotifiable(txDataCharacteristic)) {
                    MLDPUtils.enableNotifications(txDataCharacteristic, rxBleConnection);
                }

                if (MLDPUtils.isCharacteristicWritable(txDataCharacteristic)) {
                    MLDPUtils.setWriteTypeNoResponse(txDataCharacteristic);
                }
            }

            if (uuid.equals(MLDPConstants.UUID_TRANSPARENT_RX_PRIVATE_CHAR)) {
                rxDataCharacteristic = gattCharacteristic;
                if (MLDPUtils.isCharacteristicWritable(rxDataCharacteristic)) {
                    MLDPUtils.setWriteTypeNoResponse(txDataCharacteristic);
                }
            }

            if (uuid.equals(MLDPConstants.UUID_MLDP_DATA_PRIVATE_CHAR)) {
                mldpDataCharacteristic = gattCharacteristic;
                if (MLDPUtils.isCharacteristicNotifiable(gattCharacteristic)) {
                    MLDPUtils.enableNotifications(mldpDataCharacteristic, rxBleConnection);
                }

                if (MLDPUtils.isCharacteristicWritable(gattCharacteristic)) {
                    MLDPUtils.setWriteTypeNoResponse(mldpDataCharacteristic);
                }

                notificationDisposable = rxBleConnection.setupNotification(mldpDataCharacteristic)
                        .doOnError(this::onError)
                        .flatMap(observable -> observable)
                        .subscribe(this::onDataReceived, this::onError);

                getCompositeDisposable().add(notificationDisposable);
            }
        }
    }

    private void onDataReceived(byte[] bytes) {
        EventBus.getDefault().post(new DataReceivedEvent(bytes));
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, throwable.getMessage());
    }

    private void onConnectionStateChanged(RxBleConnectionState rxBleConnectionState) {
        EventBus.getDefault().post(new ConnectionEvent(rxBleConnectionState, rxBleDevice.getName(), rxBleDevice.getMacAddress()));
    }

    @NonNull
    @Override
    public final IBinder onBind(Intent intent) {
        return new LocalBinder<MLDPConnectionService>() {
            @Override
            public MLDPConnectionService getService() {
                return MLDPConnectionService.this;
            }
        };
    }

}
