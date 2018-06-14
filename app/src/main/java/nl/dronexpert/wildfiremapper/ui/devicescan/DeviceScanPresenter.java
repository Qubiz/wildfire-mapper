package nl.dronexpert.wildfiremapper.ui.devicescan;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.data.database.events.DatabaseUpdateEvent;
import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;
import nl.dronexpert.wildfiremapper.di.annotations.ActivityContext;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPDeviceScanService;
import nl.dronexpert.wildfiremapper.services.mldp.events.ScanEvent;
import nl.dronexpert.wildfiremapper.ui.base.BasePresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpView;
import nl.dronexpert.wildfiremapper.utils.rx.SchedulerProvider;

public class DeviceScanPresenter<V extends DeviceScanMvpView> extends BasePresenter<V> implements DeviceScanMvpPresenter<V> {

    private static final String TAG = DeviceScanPresenter.class.getSimpleName();

    private MLDPDeviceScanService deviceScanService;

    private final static long SCAN_DURATION_MILLISECONDS = 10000;

    @Inject
    public DeviceScanPresenter(DataManager dataManager,
                               SchedulerProvider schedulerProvider,
                               @ActivityContext CompositeDisposable compositeDisposable,
                               MLDPDeviceScanService deviceScanService) {
        super(dataManager, schedulerProvider, compositeDisposable);

        this.deviceScanService = deviceScanService;
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        deviceScanService.stopScan();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        final BleDevice bleDevice = (BleDevice) adapterView.getItemAtPosition(position);
        // TODO Implement: create a connection
    }

    @Override
    public void onMenuItemScanClick() {
        if (ready()) {
            deviceScanService.startScan(SCAN_DURATION_MILLISECONDS);
        }
         // TODO Check if scan is in progress, if not, start a new scan.
    }

    @Override
    public void onLocationPermissionsRequest(@NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getMvpView().showMessage("Permission granted");
        } else {
            getMvpView().showMessage("Permission denied");
        }
    }

    @Override
    public void onBluetoothEnableRequest(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            deviceScanService.startScan(SCAN_DURATION_MILLISECONDS);
        } else {
            getMvpView().onError("Error: Bluetooth not enabled (" + resultCode + ")");
        }
    }

    private boolean ready() {
        switch (deviceScanService.getRxBleClient().getState()) {
            case READY:
                return true;
            case BLUETOOTH_NOT_AVAILABLE:
                getMvpView().onError("Bluetooth not available...");
                return false;
            case BLUETOOTH_NOT_ENABLED:
                getMvpView().requestEnableBluetooth();
                return false;
            case LOCATION_PERMISSION_NOT_GRANTED:
                getMvpView().requestLocationPermission();
                return false;
            case LOCATION_SERVICES_NOT_ENABLED:
                getMvpView().onError("Please enable location services...");
                return false;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDatabaseUpdateEvent(DatabaseUpdateEvent event) {
        Log.d(TAG, "Database update event received...");
        if (event.DAO_TYPE == DatabaseUpdateEvent.DaoType.BLE_DEVICE_DAO) {
            switch (event.UPDATE_TYPE) {
                case CLEAR:
                    getMvpView().clearBleDevices();
                    break;
                case INSERT:
                    getCompositeDisposable().add(getDataManager()
                            .getBleDevice(event.ENTRY_ID)
                            .subscribe(getMvpView()::addBleDevice));
                    break;
                case SAVE:
                    getCompositeDisposable().add(getDataManager()
                            .getAllBleDevices()
                            .subscribe(getMvpView()::addBleDevices));
                    break;
                case LIST_SAVE:
                    getCompositeDisposable().add(getDataManager()
                            .getAllBleDevices()
                            .subscribe(getMvpView()::addBleDevices));
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanEvent(ScanEvent event) {
        switch (event.SCAN_STATE) {
            case STARTED:
                getMvpView().showMessage("Scan Started");
                getMvpView().showScanStarted(SCAN_DURATION_MILLISECONDS);
                break;
            case ERROR:
                getMvpView().onError("Error while scanning...");
                break;
            case FINISHED:
                getMvpView().showMessage("Scan Finished");
                getMvpView().showScanFinished();
                break;
        }
    }


}
