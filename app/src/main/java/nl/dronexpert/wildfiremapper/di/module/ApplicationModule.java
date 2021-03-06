package nl.dronexpert.wildfiremapper.di.module;

import android.app.Application;
import android.content.Context;

import com.github.pwittchen.reactivesensors.library.ReactiveSensors;
import com.patloew.rxlocation.RxLocation;
import com.polidea.rxandroidble2.RxBleClient;

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
import nl.dronexpert.wildfiremapper.di.annotations.DatabaseInfo;
import nl.dronexpert.wildfiremapper.services.ServiceConnectionHandler;
import nl.dronexpert.wildfiremapper.services.location.LocationService;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPConnectionService;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPDataReceiverService;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPDeviceScanService;
import nl.dronexpert.wildfiremapper.utils.AppConstants;

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

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    RxBleClient provideRxBleClient(@ApplicationContext Context context) {
        return RxBleClient.create(context);
    }

    @Provides
    RxLocation provideRxLocation(@ApplicationContext Context context) {
        return new RxLocation(context);
    }

    @Provides
    ReactiveSensors reactiveSensors(@ApplicationContext Context context) {
        return new ReactiveSensors(context);
    }

    @Provides
    @Singleton
    ServiceConnectionHandler provideServiceHandler() {
        return new ServiceConnectionHandler();
    }

    @Provides
    MLDPDeviceScanService provideBleDeviceScanService(ServiceConnectionHandler serviceConnectionHandler) {
        return serviceConnectionHandler.getDeviceScanService();
    }

    @Provides
    MLDPDataReceiverService provideMLDPDataReceivedService(ServiceConnectionHandler serviceConnectionHandler) {
        return serviceConnectionHandler.getDataReceiverService();
    }

    @Provides
    MLDPConnectionService provideMLDPConnectionService(ServiceConnectionHandler serviceConnectionHandler) {
        return serviceConnectionHandler.getConnectionService();
    }

    @Provides
    LocationService provideLocationService(ServiceConnectionHandler serviceConnectionHandler) {
        return serviceConnectionHandler.getLocationService();
    }


}
