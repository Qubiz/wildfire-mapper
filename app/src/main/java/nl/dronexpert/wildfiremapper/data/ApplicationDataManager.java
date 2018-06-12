package nl.dronexpert.wildfiremapper.data;

import android.content.Context;

import javax.inject.Inject;

import nl.dronexpert.wildfiremapper.data.database.DatabaseHelper;
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
                                  ApiHelper apiHelper) {
        this.context = context;
        this.databaseHelper = databaseHelper;
        this.preferencesHelper = preferencesHelper;
        this.apiHelper = apiHelper;
    }

}
