package nl.dronexpert.wildfiremapper.ui.devicescan.mvp;

import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;
import nl.dronexpert.wildfiremapper.ui.base.mvp.MvpView;

public interface DeviceScanMvpView extends MvpView {
    //TODO Implement methods to update the view
    void clearDevices();
    void requestEnableBluetooth();
    void showScanStarted(long duration);
    void showScanFinished();
    void showDevices(List<BleDevice> deviceList);
    void showDevice(BleDevice bleDevice);
    void showDeviceConnected(String name, String macAddress);
    void showDeviceConnecting(String name, String macAddress);
    void showDeviceDisconnecting(String name, String macAddress);
    void showDeviceDisconnected(String name, String macAddress);
}
