package nl.dronexpert.wildfiremapper.ui.devicescan;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

import com.polidea.rxandroidble2.RxBleDevice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;
import nl.dronexpert.wildfiremapper.di.annotations.ActivityContext;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPConnectionService;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPDeviceScanService;
import nl.dronexpert.wildfiremapper.services.mldp.events.ConnectionEvent;
import nl.dronexpert.wildfiremapper.services.mldp.events.ScanEvent;
import nl.dronexpert.wildfiremapper.ui.base.BasePresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpView;
import nl.dronexpert.wildfiremapper.utils.rx.SchedulerProvider;

public class DeviceScanPresenter<V extends DeviceScanMvpView> extends BasePresenter<V> implements DeviceScanMvpPresenter<V> {

    private static final String TAG = DeviceScanPresenter.class.getSimpleName();

    private MLDPDeviceScanService deviceScanService;
    private MLDPConnectionService connectionService;

    private final static long SCAN_DURATION_MILLISECONDS = 5000;

    @Inject
    public DeviceScanPresenter(DataManager dataManager,
                               SchedulerProvider schedulerProvider,
                               @ActivityContext CompositeDisposable compositeDisposable,
                               MLDPDeviceScanService deviceScanService,
                               MLDPConnectionService connectionService) {
        super(dataManager, schedulerProvider, compositeDisposable);

        this.deviceScanService = deviceScanService;
        this.connectionService = connectionService;
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        EventBus.getDefault().register(this);

        RxBleDevice connectedDevice = connectionService.getConnectedDevice();

        if (connectedDevice != null) {
            getMvpView().showDeviceConnected(connectedDevice.getName(), connectedDevice.getMacAddress());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        deviceScanService.stopScan();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        // TODO Implement: create a connection
        BleDevice device = (BleDevice) adapterView.getItemAtPosition(position);
        connectionService.connect(device.getMacAddress());
    }

    @Override
    public void onMenuItemScanClick() {
        if (ready()) {
            deviceScanService.startScan(SCAN_DURATION_MILLISECONDS);
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
                getMvpView().onError("No location permissions granted...");
                return false;
            case LOCATION_SERVICES_NOT_ENABLED:
                getMvpView().onError("Please enable location services...");
                return false;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanEvent(ScanEvent event) {
        switch (event.SCAN_STATE) {
            case STARTED:
                getMvpView().showMessage("Scan Started");
                getMvpView().clearDevices();
                RxBleDevice connectedDevice = connectionService.getConnectedDevice();

                if (connectedDevice != null) {
                    getMvpView().showDeviceConnected(connectedDevice.getName(), connectedDevice.getMacAddress());
                }
                getMvpView().showScanStarted(SCAN_DURATION_MILLISECONDS);
                break;
            case ERROR:
                getMvpView().onError("Error while scanning...");
                break;
            case NEW_SCAN_RESULT:
                getMvpView().showDevice(event.BLE_DEVICE);
                break;
            case FINISHED:
                getMvpView().showMessage("Scan Finished");
                getMvpView().showScanFinished();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectionEvent(ConnectionEvent event) {
        switch (event.STATE) {
            case CONNECTING:
                getMvpView().showDeviceConnecting(event.DEVICE_NAME, event.DEVICE_MAC_ADDRESS);
                getMvpView().showMessage("Connecting to " + event.DEVICE_NAME);
                break;
            case CONNECTED:
                getMvpView().showDeviceConnected(event.DEVICE_NAME, event.DEVICE_MAC_ADDRESS);
                getMvpView().showMessage("Connected to " + event.DEVICE_NAME);
                break;
            case DISCONNECTED:
                getMvpView().showDeviceDisconnected(event.DEVICE_NAME, event.DEVICE_MAC_ADDRESS);
                getMvpView().showMessage("Disconnected from " + event.DEVICE_NAME);
                break;
            case DISCONNECTING:
                getMvpView().showDeviceDisconnecting(event.DEVICE_NAME, event.DEVICE_MAC_ADDRESS);
                break;
        }
    }


}
