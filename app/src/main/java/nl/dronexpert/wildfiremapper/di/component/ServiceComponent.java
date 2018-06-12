package nl.dronexpert.wildfiremapper.di.component;

import dagger.Component;
import nl.dronexpert.wildfiremapper.di.annotations.PerService;
import nl.dronexpert.wildfiremapper.di.module.ServiceModule;
import nl.dronexpert.wildfiremapper.services.ble.BleDeviceScanService;

/**
 * Created by Mathijs de Groot on 12/06/2018.
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    void inject(BleDeviceScanService service);
}
