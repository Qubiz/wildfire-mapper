package nl.dronexpert.wildfiremapper.services.ble;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class BleDeviceScanService extends Service {

    private static final String TAG = BleDeviceScanService.class.getSimpleName();

    private static RxBleClient rxBleClient;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, BleDeviceScanService.class);
    }

    public static void start(
            Context context,
            long scanDuration,
            TimeUnit timeUnit,
            ScanSettings scanSettings,
            ScanFilter... scanFilters
    ) {
        Intent starter = getStartIntent(context);
        context.startService(starter);

        rxBleClient.scanBleDevices(scanSettings, scanFilters)
                .observeOn(AndroidSchedulers.mainThread())
                .take(scanDuration, timeUnit)
                .doOnError(BleDeviceScanService::onError)
                .doOnDispose(BleDeviceScanService::onDispose)
                .doOnComplete(() -> stop(context))
                .doOnNext(BleDeviceScanService::onScanResult)
                .subscribe();

    }

    public static void stop(Context context) {
        context.stopService(getStartIntent(context));
    }

    private static void onError(Throwable throwable) {
        Log.e(TAG, "Error while scanning... ", throwable);
    }

    private static void onDispose() {

    }

    private static void onScanResult(ScanResult scanResult){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        rxBleClient = RxBleClient.create(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, TAG + " started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, TAG + " stopped");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
