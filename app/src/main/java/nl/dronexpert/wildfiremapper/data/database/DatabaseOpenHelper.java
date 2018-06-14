package nl.dronexpert.wildfiremapper.data.database;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.dronexpert.wildfiremapper.data.database.model.DaoMaster;
import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;
import nl.dronexpert.wildfiremapper.di.annotations.DatabaseInfo;

@Singleton
public class DatabaseOpenHelper extends DaoMaster.OpenHelper {

    @Inject
    public DatabaseOpenHelper(@ApplicationContext Context context, @DatabaseInfo String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
