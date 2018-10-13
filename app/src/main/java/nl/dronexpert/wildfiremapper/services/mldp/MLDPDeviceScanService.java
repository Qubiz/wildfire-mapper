package nl.dronexpert.wildfiremapper.services.mldp;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;
import nl.dronexpert.wildfiremapper.services.base.BaseService;
import nl.dronexpert.wildfiremapper.services.mldp.events.ScanEvent;
import nl.dronexpert.wildfiremapper.services.mldp.utils.MLDPUtils;
import nl.dronexpert.wildfiremapper.utils.CommonUtils;

import static nl.dronexpert.wildfiremapper.services.mldp.events.ScanEvent.State;
import static nl.dronexpert.wildfiremapper.services.mldp.utils.MLDPConstants.SCAN_FILTER_MLDP_PRIVATE_SERVICE;
import static nl.dronexpert.wildfiremapper.services.mldp.utils.MLDPConstants.SCAN_SETTINGS;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class MLDPDeviceScanService extends BaseService {

    public static final String TAG = MLDPDeviceScanService.class.getSimpleName();

    @Inject
    RxBleClient rxBleClient;

    Disposable scanDisposable;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MLDPDeviceScanService.class);
    }

    public void startScan(long scanDurationMilliseconds) {
        if (MLDPUtils.isBleClientReady(rxBleClient)) {
            Observable<ScanResult> scanResultObservable = getScanObservable(scanDurationMilliseconds,
                    TimeUnit.MILLISECONDS, SCAN_SETTINGS, SCAN_FILTER_MLDP_PRIVATE_SERVICE);

            scanDisposable = scanResultObservable.subscribe(this::onScanResult);
            getCompositeDisposable().add(scanDisposable);
        }
    }

    public void stopScan() {
        if (isScanning()) {
            scanDisposable.dispose();
        }
    }

    private boolean isScanning() {
        return scanDisposable != null;
    }

    private Observable<ScanResult> getScanObservable(long scanDuration,
                                                     TimeUnit timeUnit,
                                                     ScanSettings scanSettings,
                                                     ScanFilter... scanFilters) {

        return rxBleClient.scanBleDevices(scanSettings, scanFilters)
                .observeOn(AndroidSchedulers.mainThread())
                .take(scanDuration, timeUnit)
                .doOnError(this::onError)
                .doOnDispose(this::onDispose)
                .doOnComplete(this::stopScan)
                .doOnSubscribe(subscriber -> {
                    EventBus.getDefault().post(new ScanEvent(State.STARTED, null));
                });
    }

    private void onError(Throwable throwable) {
        EventBus.getDefault().post(new ScanEvent(State.ERROR, null));
    }

    private void onDispose() {
        EventBus.getDefault().post(new ScanEvent(State.FINISHED, null));
    }

    private void onScanResult(ScanResult scanResult) {
        RxBleDevice rxBleDevice = scanResult.getBleDevice();

        BleDevice bleDevice = new BleDevice();
        bleDevice.setName(rxBleDevice.getName());
        bleDevice.setMacAddress(rxBleDevice.getMacAddress());
        bleDevice.setCreatedAt(CommonUtils.getTimeStamp());
        bleDevice.setUpdatedAt(CommonUtils.getTimeStamp());
        bleDevice.setIsConnected(rxBleDevice.getConnectionState() == RxBleConnectionState.CONNECTED);

        EventBus.getDefault().post(new ScanEvent(State.NEW_SCAN_RESULT, bleDevice));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getServiceComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (scanDisposable != null) {
            scanDisposable.dispose();
        }

        super.onDestroy();
    }

    public RxBleClient getRxBleClient() {
        return rxBleClient;
    }

    @NonNull
    @Override
    public final IBinder onBind(Intent intent) {
        return new LocalBinder<MLDPDeviceScanService>() {
            @Override
            public MLDPDeviceScanService getService() {
                return MLDPDeviceScanService.this;
            }
        };
    }

}
