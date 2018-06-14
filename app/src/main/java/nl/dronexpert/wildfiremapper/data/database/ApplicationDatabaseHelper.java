package nl.dronexpert.wildfiremapper.data.database;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import nl.dronexpert.wildfiremapper.data.database.events.DatabaseUpdateEvent;
import nl.dronexpert.wildfiremapper.data.database.events.DatabaseUpdateEvent.DaoType;
import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;
import nl.dronexpert.wildfiremapper.data.database.model.BleDeviceDao;
import nl.dronexpert.wildfiremapper.data.database.model.DaoMaster;
import nl.dronexpert.wildfiremapper.data.database.model.DaoSession;

import static nl.dronexpert.wildfiremapper.data.database.events.DatabaseUpdateEvent.*;

@Singleton
public class ApplicationDatabaseHelper implements DatabaseHelper {

    private static final String TAG = ApplicationDatabaseHelper.class.getSimpleName();
    private final DaoSession daoSession;

    @Inject
    public ApplicationDatabaseHelper(DatabaseOpenHelper databaseOpenHelper) {
        daoSession = new DaoMaster(databaseOpenHelper.getWritableDb()).newSession();
    }

    @Override
    public Observable<Long> insertBleDevice(BleDevice device) {
        return Observable.fromCallable(() -> {
            long id = daoSession.getBleDeviceDao().insertOrReplace(device);
            postDatabaseUpdateEvent(DaoType.BLE_DEVICE_DAO, UpdateType.INSERT, id);
            return id;
        });
    }

    @Override
    public Observable<Boolean> saveBleDevice(BleDevice device) {
        return Observable.fromCallable(() -> {
            daoSession.getBleDeviceDao().insertOrReplaceInTx(device);
            postDatabaseUpdateEvent(DaoType.BLE_DEVICE_DAO, UpdateType.SAVE, null);
            return true;
        });
    }

    @Override
    public Observable<Boolean> saveBleDeviceList(List<BleDevice> deviceList) {
        return Observable.fromCallable(() -> {
            daoSession.getBleDeviceDao().insertOrReplaceInTx(deviceList);
            postDatabaseUpdateEvent(DaoType.BLE_DEVICE_DAO, UpdateType.LIST_SAVE, null);
            return true;
        });
    }

    @Override
    public Observable<Boolean> containsDevice(String macAddress) {
        return Observable.fromCallable(() -> {
            List<BleDevice> devices = daoSession.getBleDeviceDao().queryBuilder()
                    .where(BleDeviceDao.Properties.MacAddress.eq(macAddress))
                    .list();
            return !devices.isEmpty();
        });
    }

    @Override
    public Observable<BleDevice> getBleDevice(String macAddress) {
        // TODO Implement getBleDevice(String macAddress)
        return null;
    }

    @Override
    public Observable<Boolean> setBleDeviceConnected(String macAddress) {
        // TODO Implement setBleDeviceConnected(String macAddress)
        return null;
    }

    @Override
    public Observable<List<BleDevice>> getAllBleDevices() {
        return Observable.fromCallable(daoSession.getBleDeviceDao()::loadAll);
    }

    @Override
    public Observable<BleDevice> getBleDevice(long id) {
        return Observable.fromCallable(() -> daoSession.getBleDeviceDao().load(id));
    }

    @Override
    public Observable<Boolean> isBleDevicesEmpty() {
        return Observable.fromCallable(() -> !(daoSession.getBleDeviceDao().count() > 0));
    }

    @Override
    public Observable<Boolean> clearBleDevices() {
        return Observable.fromCallable(() -> {
            daoSession.getBleDeviceDao().deleteAll();
            postDatabaseUpdateEvent(DaoType.BLE_DEVICE_DAO, UpdateType.CLEAR, null);
            return true;
        });
    }

    private static void postDatabaseUpdateEvent(DaoType daoType, UpdateType updateType, @Nullable Long entryID) {
        Log.d(TAG, "Posting database update event!");
        EventBus.getDefault().post(new DatabaseUpdateEvent(daoType, updateType, entryID));
    }
}
