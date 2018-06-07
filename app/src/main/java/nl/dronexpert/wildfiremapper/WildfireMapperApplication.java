package nl.dronexpert.wildfiremapper;

import android.app.Application;

import nl.dronexpert.wildfiremapper.di.component.ApplicationComponent;
import nl.dronexpert.wildfiremapper.di.component.DaggerApplicationComponent;
import nl.dronexpert.wildfiremapper.di.module.ApplicationModule;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class WildfireMapperApplication extends Application {

    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();


    }

    public static ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
