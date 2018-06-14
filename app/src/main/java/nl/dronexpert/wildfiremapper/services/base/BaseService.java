package nl.dronexpert.wildfiremapper.services.base;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import nl.dronexpert.wildfiremapper.WildfireMapperApplication;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.di.component.DaggerServiceComponent;
import nl.dronexpert.wildfiremapper.di.component.ServiceComponent;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPDeviceScanService;

/**
 * Created by Mathijs de Groot on 13/06/2018.
 */
public abstract class BaseService extends Service {

    @Inject
    DataManager dataManager;

    ServiceComponent serviceComponent;

    CompositeDisposable compositeDisposable;

    @Override
    public void onCreate() {
        super.onCreate();

        serviceComponent = DaggerServiceComponent.builder()
                .applicationComponent(((WildfireMapperApplication) getApplication()).getComponent())
                .build();

        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }

        super.onDestroy();
    }

    public ServiceComponent getServiceComponent() {
        return serviceComponent;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public abstract class LocalBinder<T extends BaseService> extends Binder {
        public abstract T getService();
    }
}
