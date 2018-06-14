package nl.dronexpert.wildfiremapper.ui.devicescan.mvp;

import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;
import nl.dronexpert.wildfiremapper.ui.base.mvp.MvpView;

public interface DeviceScanMvpView extends MvpView {
    //TODO Implement methods to update the view
    void addBleDevices(List<BleDevice> deviceList);
    void addBleDevice(BleDevice bleDevice);
    void clearBleDevices();
    void requestLocationPermission();
    void requestEnableBluetooth();
    void showScanStarted(long duration);
    void showScanFinished();
}
