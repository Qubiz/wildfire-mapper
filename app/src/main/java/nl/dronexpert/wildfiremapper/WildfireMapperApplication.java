package nl.dronexpert.wildfiremapper;

import android.app.Application;
import android.content.Context;

import nl.dronexpert.wildfiremapper.di.component.ApplicationComponent;
import nl.dronexpert.wildfiremapper.di.component.DaggerApplicationComponent;
import nl.dronexpert.wildfiremapper.di.module.ApplicationModule;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class WildfireMapperApplication extends Application {

    private ApplicationComponent appComponent;

    public static WildfireMapperApplication application(Context context) {
        return (WildfireMapperApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent injector() {
        return appComponent;
    }
}
