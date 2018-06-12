package nl.dronexpert.wildfiremapper.data.database;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApplicationDatabaseHelper implements DatabaseHelper {

    @Inject
    public ApplicationDatabaseHelper(DatabaseOpenHelper databaseOpenHelper) {

    }

}
