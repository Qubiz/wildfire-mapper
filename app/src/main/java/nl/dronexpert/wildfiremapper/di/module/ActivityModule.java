package nl.dronexpert.wildfiremapper.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.wealthfront.magellan.Navigator;
import com.wealthfront.magellan.Screen;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.dronexpert.wildfiremapper.di.annotations.ActivityContext;
import nl.dronexpert.wildfiremapper.ui.mapbox.MapboxScreen;

/**
 * Created by Mathijs de Groot on 07/06/2018.
 */
@Module
public final class ActivityModule {
    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return activity;
    }

}
