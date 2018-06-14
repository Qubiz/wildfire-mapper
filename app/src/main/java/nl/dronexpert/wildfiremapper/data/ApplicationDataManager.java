package nl.dronexpert.wildfiremapper.data;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import nl.dronexpert.wildfiremapper.data.database.DatabaseHelper;
import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;
import nl.dronexpert.wildfiremapper.data.network.ApiHelper;
import nl.dronexpert.wildfiremapper.data.preferences.PreferencesHelper;
import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;

public class ApplicationDataManager implements DataManager {

    public static final String TAG = ApplicationDataManager.class.getSimpleName();

    private final Context context;
    private final DatabaseHelper databaseHelper;
    private final PreferencesHelper preferencesHelper;
    private final ApiHelper apiHelper;

    @Inject
    public ApplicationDataManager(@ApplicationContext Context context,
                                  DatabaseHelper databaseHelper,
                                  PreferencesHelper preferencesHelper,
                                  ApiHelper apiHelper
    ) {
        this.context = context;
        this.databaseHelper = databaseHelper;
        this.preferencesHelper = preferencesHelper;
        this.apiHelper = apiHelper;
    }

    @Override
    public Observable<Long> insertBleDevice(BleDevice device) {
        return databaseHelper.insertBleDevice(device);
    }

    @Override
    public Observable<Boolean> saveBleDevice(BleDevice device) {
        Log.d(TAG, "Saving ble device...");
        return databaseHelper.saveBleDevice(device);
    }

    @Override
    public Observable<Boolean> saveBleDeviceList(List<BleDevice> deviceList) {
        Log.d(TAG, "Saving list of ble devices...");
        return databaseHelper.saveBleDeviceList(deviceList);
    }

    @Override
    public Observable<Boolean> containsDevice(String macAddress) {
        return databaseHelper.containsDevice(macAddress);
    }

    @Override
    public Observable<BleDevice> getBleDevice(String macAddress) {
        return databaseHelper.getBleDevice(macAddress);
    }

    @Override
    public Observable<Boolean> setBleDeviceConnected(String macAddress) {
        return databaseHelper.setBleDeviceConnected(macAddress);
    }

    @Override
    public Observable<List<BleDevice>> getAllBleDevices() {
        Log.d(TAG, "Retrieving all ble devices in db...");
        return databaseHelper.getAllBleDevices();
    }

    @Override
    public Observable<BleDevice> getBleDevice(long id) {
        Log.d(TAG, "Retrieving ble device (" + id + ") from db...");
        return databaseHelper.getBleDevice(id);
    }

    @Override
    public Observable<Boolean> isBleDevicesEmpty() {
        return databaseHelper.isBleDevicesEmpty();
    }

    @Override
    public Observable<Boolean> clearBleDevices() {
        Log.d(TAG, "Removing ble devices from db...");
        return databaseHelper.clearBleDevices();
    }
}
