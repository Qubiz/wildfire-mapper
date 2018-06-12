package nl.dronexpert.wildfiremapper.data.database;

import java.util.List;

import io.reactivex.Observable;
import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;

public interface DatabaseHelper {

    //TODO Implement ways to interact with the database

    Observable<Long> insertBleDevice(final BleDevice device);
    Observable<Boolean> saveBleDevice(final BleDevice device);
    Observable<Boolean> saveBleDeviceList(final List<BleDevice> deviceList);
    Observable<List<BleDevice>> getAllBleDevices();
    Observable<BleDevice> getBleDevice(final long id);
    Observable<Boolean> isBleDevicesEmpty();
    Observable<Boolean> clearBleDevices();

}
