package nl.dronexpert.wildfiremapper.di.component;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.wealthfront.magellan.Navigator;

import javax.inject.Singleton;

import dagger.Component;

import nl.dronexpert.wildfiremapper.di.annotations.ActivityContext;
import nl.dronexpert.wildfiremapper.di.annotations.PerActivity;
import nl.dronexpert.wildfiremapper.di.module.ActivityModule;
import nl.dronexpert.wildfiremapper.ui.WildfireMapperActivity;

/**
 * Created by Mathijs de Groot on 07/06/2018.
 */
@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = ActivityModule.class
)
public interface ActivityComponent {
    void inject(WildfireMapperActivity activity);

    @ActivityContext
    Context getContext();

    AppCompatActivity getActivity();

}
