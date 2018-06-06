package nl.dronexpert.wildfiremapper.di.component;

import javax.inject.Singleton;

import dagger.Component;
import nl.dronexpert.wildfiremapper.ui.WildfireMapperActivity;
import nl.dronexpert.wildfiremapper.di.module.ApplicationModule;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
@Component(modules = ApplicationModule.class)
@Singleton
public interface ApplicationComponent {
    void inject(WildfireMapperActivity activity);
}
