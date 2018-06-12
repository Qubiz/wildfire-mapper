package nl.dronexpert.wildfiremapper.data.network;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;

@Singleton
public class ApplicationApiHelper implements ApiHelper {

    @Inject
    public ApplicationApiHelper(@ApplicationContext Context context) {

    }

}
