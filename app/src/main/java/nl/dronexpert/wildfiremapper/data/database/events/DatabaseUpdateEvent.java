package nl.dronexpert.wildfiremapper.data.database.events;

import io.reactivex.annotations.Nullable;

/**
 * Created by Mathijs de Groot on 12/06/2018.
 */
public class DatabaseUpdateEvent {

    public enum UpdateType {
        CLEAR,
        INSERT,
        SAVE,
        LIST_SAVE
    }

    public enum DaoType {
        BLE_DEVICE_DAO
    }

    public final Long ENTRY_ID;
    public final DaoType DAO_TYPE;
    public final UpdateType UPDATE_TYPE;

    public DatabaseUpdateEvent(DaoType daoType, UpdateType updateType, @Nullable Long entryID) {
        this.DAO_TYPE = daoType;
        this.UPDATE_TYPE = updateType;
        this.ENTRY_ID = entryID;
    }

}
