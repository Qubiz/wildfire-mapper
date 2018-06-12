package nl.dronexpert.wildfiremapper.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

import nl.dronexpert.wildfiremapper.WildfireMapperApplication;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;
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
    Context getApplicationContext();

    Application getApplication();

    DataManager getDataManager();
}
