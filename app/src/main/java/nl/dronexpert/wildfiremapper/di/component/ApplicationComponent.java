package nl.dronexpert.wildfiremapper.di.component;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.wealthfront.magellan.Navigator;

import javax.inject.Singleton;

import dagger.Component;
import nl.dronexpert.wildfiremapper.WildfireMapperApplication;
import nl.dronexpert.wildfiremapper.di.annotations.ActivityContext;
import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;
import nl.dronexpert.wildfiremapper.services.ble.BleDeviceScanService;
import nl.dronexpert.wildfiremapper.ui.WildfireMapperActivity;
import nl.dronexpert.wildfiremapper.di.module.ApplicationModule;

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
    Context getContext();

    Application getApplication();

    Navigator getNavigator();

    Mapbox getMapbox();
}
