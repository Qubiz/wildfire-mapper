package nl.dronexpert.wildfiremapper.di.module;

import android.app.Service;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import nl.dronexpert.wildfiremapper.di.annotations.ServiceContext;

/**
 * Created by Mathijs de Groot on 12/06/2018.
 */
@Module
public class ServiceModule {

    private final Service service;

    public ServiceModule(Service service) {
        this.service = service;
    }

    @ServiceContext
    @Provides
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

}
