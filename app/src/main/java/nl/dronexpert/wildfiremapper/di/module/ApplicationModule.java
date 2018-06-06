package nl.dronexpert.wildfiremapper.di.module;

import android.app.Application;
import android.content.Context;

import com.mapbox.mapboxsdk.Mapbox;
import com.wealthfront.magellan.Navigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;
import nl.dronexpert.wildfiremapper.ui.mapbox.MapboxScreen;
import nl.dronexpert.wildfiremapper.utils.Constants;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
@Module
public final class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    Navigator provideNavigator() {
        return Navigator.withRoot(new MapboxScreen()).build();
    }

    @Provides
    @Singleton
    Mapbox provideMapbox(@ApplicationContext Context context) {
        return Mapbox.getInstance(context, Constants.Mapbox.MAPBOX_ACCESS_TOKEN);
    }
}
