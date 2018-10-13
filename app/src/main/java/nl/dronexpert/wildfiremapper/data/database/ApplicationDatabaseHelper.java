package nl.dronexpert.wildfiremapper.data.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.dronexpert.wildfiremapper.data.database.model.DaoMaster;
import nl.dronexpert.wildfiremapper.data.database.model.DaoSession;

@Singleton
public class ApplicationDatabaseHelper implements DatabaseHelper {

    private static final String TAG = ApplicationDatabaseHelper.class.getSimpleName();
    private final DaoSession daoSession;

    @Inject
    public ApplicationDatabaseHelper(DatabaseOpenHelper databaseOpenHelper) {
        daoSession = new DaoMaster(databaseOpenHelper.getWritableDb()).newSession();
    }

}
