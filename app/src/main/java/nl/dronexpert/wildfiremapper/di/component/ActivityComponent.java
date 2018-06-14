package nl.dronexpert.wildfiremapper.di.component;

import dagger.Component;
import nl.dronexpert.wildfiremapper.di.annotations.PerActivity;
import nl.dronexpert.wildfiremapper.di.module.ActivityModule;
import nl.dronexpert.wildfiremapper.ui.devicescan.DeviceScanActivity;
import nl.dronexpert.wildfiremapper.ui.map.MapActivity;
import nl.dronexpert.wildfiremapper.ui.splash.SplashActivity;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MapActivity mapActivity);
    void inject(SplashActivity splashActivity);
    void inject(DeviceScanActivity deviceScanActivity);

    //TODO Implement injectors for the different activities, fragments & dialogs if they need injection
}
