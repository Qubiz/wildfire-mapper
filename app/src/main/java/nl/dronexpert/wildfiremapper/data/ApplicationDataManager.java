package nl.dronexpert.wildfiremapper.data;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import nl.dronexpert.wildfiremapper.data.database.DatabaseHelper;
import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;
import nl.dronexpert.wildfiremapper.data.network.ApiHelper;
import nl.dronexpert.wildfiremapper.data.preferences.PreferencesHelper;
import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;

public class ApplicationDataManager implements DataManager {

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
        return databaseHelper.saveBleDevice(device);
    }

    @Override
    public Observable<Boolean> saveBleDeviceList(List<BleDevice> deviceList) {
        return databaseHelper.saveBleDeviceList(deviceList);
    }

    @Override
    public Observable<List<BleDevice>> getAllBleDevices() {
        return databaseHelper.getAllBleDevices();
    }

    @Override
    public Observable<BleDevice> getBleDevice(long id) {
        return databaseHelper.getBleDevice(id);
    }

    @Override
    public Observable<Boolean> isBleDevicesEmpty() {
        return databaseHelper.isBleDevicesEmpty();
    }

    @Override
    public Observable<Boolean> clearBleDevices() {
        return databaseHelper.clearBleDevices();
    }
}
