package nl.dronexpert.wildfiremapper.data.database;

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
import nl.dronexpert.wildfiremapper.data.database.model.DaoMaster;
import nl.dronexpert.wildfiremapper.data.database.model.DaoSession;

import static nl.dronexpert.wildfiremapper.data.database.events.DatabaseUpdateEvent.*;

@Singleton
public class ApplicationDatabaseHelper implements DatabaseHelper {

    private final DaoSession daoSession;

    @Inject
    public ApplicationDatabaseHelper(DatabaseOpenHelper databaseOpenHelper) {
        daoSession = new DaoMaster(databaseOpenHelper.getWritableDb()).newSession();
    }

    @Override
    public Observable<Long> insertBleDevice(BleDevice device) {
        return Observable.fromCallable(() -> {
            long id = daoSession.getBleDeviceDao().insert(device);
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
        EventBus.getDefault().post(new DatabaseUpdateEvent(daoType, updateType, entryID));
    }
}
