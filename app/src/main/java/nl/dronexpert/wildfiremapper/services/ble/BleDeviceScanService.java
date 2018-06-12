package nl.dronexpert.wildfiremapper.services.ble;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import nl.dronexpert.wildfiremapper.WildfireMapperApplication;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;
import nl.dronexpert.wildfiremapper.di.annotations.ServiceContext;
import nl.dronexpert.wildfiremapper.di.component.DaggerServiceComponent;
import nl.dronexpert.wildfiremapper.di.component.ServiceComponent;
import nl.dronexpert.wildfiremapper.utils.CommonUtils;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class BleDeviceScanService extends Service {

    private static final String TAG = BleDeviceScanService.class.getSimpleName();

    private static RxBleClient rxBleClient;

    @Inject
    @ServiceContext
    CompositeDisposable compositeDisposable;

    @Inject
    DataManager dataManager;

    private static Intent getStartIntent(Context context) {
        return new Intent(context, BleDeviceScanService.class);
    }

    public void start(
            Context context,
            long scanDuration,
            TimeUnit timeUnit,
            ScanSettings scanSettings,
            ScanFilter... scanFilters
    ) {
        Intent starter = getStartIntent(context);
        context.startService(starter);

        compositeDisposable.add(dataManager.clearBleDevices()
                .subscribe(aBoolean -> compositeDisposable.add(getScanObservable(scanDuration,
                        timeUnit, scanSettings, scanFilters)
                .doOnComplete(() -> stop(context))
                .subscribe(this::onScanResult))));
    }

    public void stop(Context context) {
        context.stopService(getStartIntent(context));
    }

    private static Observable<ScanResult> getScanObservable(
            long scanDuration,
            TimeUnit timeUnit,
            ScanSettings scanSettings,
            ScanFilter... scanFilters
    ) {
        return rxBleClient.scanBleDevices(scanSettings, scanFilters)
                .observeOn(AndroidSchedulers.mainThread())
                .take(scanDuration, timeUnit)
                .doOnError(BleDeviceScanService::onError)
                .doOnDispose(BleDeviceScanService::onDispose);
    }

    private static void onError(Throwable throwable) {
        Log.e(TAG, "Error while scanning... ", throwable);
    }

    private static void onDispose() {

    }

    private void onScanResult(ScanResult scanResult) {
        RxBleDevice rxBleDevice = scanResult.getBleDevice();
        BleDevice bleDevice = new BleDevice();
        bleDevice.setName(rxBleDevice.getName());
        bleDevice.setMacAddress(rxBleDevice.getMacAddress());
        bleDevice.setCreatedAt(CommonUtils.getTimeStamp());
        bleDevice.setUpdatedAt(CommonUtils.getTimeStamp());
        bleDevice.setIsConnected(rxBleDevice.getConnectionState() == RxBleConnectionState.CONNECTED);
        dataManager.insertBleDevice(bleDevice);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rxBleClient = RxBleClient.create(this);

        ServiceComponent serviceComponent = DaggerServiceComponent.builder()
                .applicationComponent(((WildfireMapperApplication) getApplication()).getComponent())
                .build();

        serviceComponent.inject(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, TAG + " started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, TAG + " stopped");
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
