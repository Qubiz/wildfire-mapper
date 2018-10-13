package nl.dronexpert.wildfiremapper.data.network;

import io.reactivex.Observable;
import okhttp3.Response;

public interface ApiHelper {
    //TODO Implement ways to interact with the network
    Observable<Response> postPointData(String geoJsonData);
}
