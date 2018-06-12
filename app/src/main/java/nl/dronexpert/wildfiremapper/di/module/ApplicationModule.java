package nl.dronexpert.wildfiremapper.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import nl.dronexpert.wildfiremapper.data.ApplicationDataManager;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.data.database.ApplicationDatabaseHelper;
import nl.dronexpert.wildfiremapper.data.database.DatabaseHelper;
import nl.dronexpert.wildfiremapper.data.network.ApiHelper;
import nl.dronexpert.wildfiremapper.data.network.ApplicationApiHelper;
import nl.dronexpert.wildfiremapper.data.preferences.ApplicationPreferencesHelper;
import nl.dronexpert.wildfiremapper.data.preferences.PreferencesHelper;
import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;
import nl.dronexpert.wildfiremapper.di.annotations.MapboxInfo;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(ApplicationDataManager applicationDataManager) {
        return applicationDataManager;
    }

    @Provides
    @Singleton
    DatabaseHelper provideDatabaseHelper(ApplicationDatabaseHelper applicationDatabaseHelper) {
        return applicationDatabaseHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(ApplicationPreferencesHelper applicationPreferencesHelper) {
        return applicationPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(ApplicationApiHelper applicationApiHelper) {
        return applicationApiHelper;
    }



}
