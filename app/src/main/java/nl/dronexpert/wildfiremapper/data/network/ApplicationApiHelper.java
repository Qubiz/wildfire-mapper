package nl.dronexpert.wildfiremapper.data.network;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import nl.dronexpert.wildfiremapper.di.annotations.ApplicationContext;

import nl.dronexpert.wildfiremapper.utils.AppConstants;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Singleton
public class ApplicationApiHelper implements ApiHelper {

    private static final String TAG = ApplicationApiHelper.class.getSimpleName();

    @Inject
    public ApplicationApiHelper(@ApplicationContext Context context) {

    }

    @Override
    public Observable<Response> postPointData(String geoJsonData) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, geoJsonData);
        Request request = new Request.Builder()
                .url(AppConstants.GEO_SERVICE_URL)
                .post(body)
                .build();
        return Observable.create(emitter -> {
            Response response = client.newCall(request).execute();
            emitter.onNext(response);
            emitter.onComplete();
        });
    }
}
