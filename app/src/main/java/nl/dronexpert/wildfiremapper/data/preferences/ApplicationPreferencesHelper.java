package nl.dronexpert.wildfiremapper.data.preferences;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;

@Singleton
public class ApplicationPreferencesHelper implements PreferencesHelper {

    @Inject
    public ApplicationPreferencesHelper(@ApplicationContext Context context) {

    }



}
