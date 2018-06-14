package nl.dronexpert.wildfiremapper;

import android.app.Application;
import android.content.Context;

import com.mapbox.mapboxsdk.Mapbox;

import javax.inject.Inject;

import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.di.component.ApplicationComponent;
import nl.dronexpert.wildfiremapper.di.component.DaggerApplicationComponent;
import nl.dronexpert.wildfiremapper.di.module.ApplicationModule;
import nl.dronexpert.wildfiremapper.services.ServiceConnectionHandler;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPConnectionService;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPDataReceiverService;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPDeviceScanService;
import nl.dronexpert.wildfiremapper.utils.AppConstants;
import nl.dronexpert.wildfiremapper.utils.AppLogger;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class WildfireMapperApplication extends Application {

    public static final String TAG = WildfireMapperApplication.class.getSimpleName();

    @Inject
    DataManager dataManager;

    @Inject
    ServiceConnectionHandler serviceConnectionHandler;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        AppLogger.init();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        applicationComponent.inject(this);

        bindServices();

        Mapbox.getInstance(this, AppConstants.MAPBOX_TOKEN);
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }

    private void bindServices() {
        bindService(MLDPDeviceScanService.getStartIntent(this), serviceConnectionHandler, Context.BIND_AUTO_CREATE);
        bindService(MLDPConnectionService.getStartIntent(this), serviceConnectionHandler, Context.BIND_AUTO_CREATE);
        bindService(MLDPDataReceiverService.getStartIntent(this), serviceConnectionHandler, Context.BIND_AUTO_CREATE);
    }

}
