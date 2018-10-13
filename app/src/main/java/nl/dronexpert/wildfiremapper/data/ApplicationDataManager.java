package nl.dronexpert.wildfiremapper.data;

import android.content.Context;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import nl.dronexpert.wildfiremapper.data.database.DatabaseHelper;
import nl.dronexpert.wildfiremapper.data.network.ApiHelper;
import nl.dronexpert.wildfiremapper.data.preferences.PreferencesHelper;
import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;
import okhttp3.Response;

public class ApplicationDataManager implements DataManager {

    public static final String TAG = ApplicationDataManager.class.getSimpleName();

    private final Context context;
    private final DatabaseHelper databaseHelper;
    private final PreferencesHelper preferencesHelper;
    private final ApiHelper apiHelper;

    @Inject
    public ApplicationDataManager(@ApplicationContext Context context,
                                  DatabaseHelper databaseHelper,
                                  PreferencesHelper preferencesHelper,
                                  ApiHelper apiHelper
    ) {
        this.context = context;
        this.databaseHelper = databaseHelper;
        this.preferencesHelper = preferencesHelper;
        this.apiHelper = apiHelper;
    }

    @Override
    public Observable<Response> postPointData(String geoJsonData) {
        return apiHelper.postPointData(geoJsonData);
    }
}
