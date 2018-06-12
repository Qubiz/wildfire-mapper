package nl.dronexpert.wildfiremapper;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

import javax.inject.Inject;

import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.di.component.ApplicationComponent;
import nl.dronexpert.wildfiremapper.di.component.DaggerApplicationComponent;
import nl.dronexpert.wildfiremapper.di.module.ApplicationModule;
import nl.dronexpert.wildfiremapper.utils.AppConstants;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class WildfireMapperApplication extends Application {

    @Inject
    DataManager dataManager;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        applicationComponent.inject(this);

        Mapbox.getInstance(this, AppConstants.MAPBOX_TOKEN);
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }

}
