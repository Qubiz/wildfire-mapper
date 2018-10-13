package nl.dronexpert.wildfiremapper.di.component;

import android.app.Application;
import android.content.Context;

import com.github.pwittchen.reactivesensors.library.ReactiveSensors;
import com.patloew.rxlocation.RxLocation;
import com.polidea.rxandroidble2.RxBleClient;

import javax.inject.Singleton;

import dagger.Component;

import nl.dronexpert.wildfiremapper.WildfireMapperApplication;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;
import nl.dronexpert.wildfiremapper.di.module.ApplicationModule;
import nl.dronexpert.wildfiremapper.services.location.LocationService;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPConnectionService;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPDataReceiverService;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPDeviceScanService;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
@Singleton
@Component(
        modules = ApplicationModule.class
)
public interface ApplicationComponent {
    void inject(WildfireMapperApplication application);

    @ApplicationContext
    Context getApplicationContext();

    Application getApplication();

    DataManager getDataManager();

    RxBleClient getRxBleClient();

    RxLocation getRxLocation();

    ReactiveSensors getReactiveSensors();

    LocationService getLocationService();

    MLDPDataReceiverService getMLDPDatareceiverService();
    MLDPDeviceScanService getMLDPDeviceScanService();
    MLDPConnectionService getMLDPConnectionService();
}
