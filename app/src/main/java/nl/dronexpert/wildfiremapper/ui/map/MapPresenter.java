package nl.dronexpert.wildfiremapper.ui.map;

import android.support.annotation.NonNull;
import android.util.Log;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.hashids.Hashids;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.data.protobuf.HotspotMessageProto;
import nl.dronexpert.wildfiremapper.di.annotations.ActivityContext;
import nl.dronexpert.wildfiremapper.services.location.LocationService;
import nl.dronexpert.wildfiremapper.services.location.events.LocationUpdateEvent;
import nl.dronexpert.wildfiremapper.services.location.events.OrientationUpdateEvent;
import nl.dronexpert.wildfiremapper.services.mldp.events.HotspotMessageEvent;
import nl.dronexpert.wildfiremapper.ui.base.BasePresenter;
import nl.dronexpert.wildfiremapper.ui.map.mvp.MapMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.map.mvp.MapMvpView;
import nl.dronexpert.wildfiremapper.utils.CommonUtils;
import nl.dronexpert.wildfiremapper.utils.rx.SchedulerProvider;

public class MapPresenter<V extends MapMvpView> extends BasePresenter<V> implements MapMvpPresenter<V> {

    private static final String TAG = MapPresenter.class.getSimpleName();

    private static final long LOCATION_UPDATE_INTERVAL = 5000;

    private List<HotspotMessageProto.Hotspot> hotspotList = new ArrayList<>();

    private LocationService locationService;

    private MapboxMap map;

    private float currentAngle = 0f;

    @Inject
    public MapPresenter(DataManager dataManager,
                        SchedulerProvider schedulerProvider,
                        @ActivityContext CompositeDisposable compositeDisposable,
                        LocationService locationService) {
        super(dataManager, schedulerProvider, compositeDisposable);

        this.locationService = locationService;
    }


    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        EventBus.getDefault().register(this);

        locationService.start(LOCATION_UPDATE_INTERVAL);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        EventBus.getDefault().unregister(this);

        locationService.stop();
    }

    @Override
    public void onDrawerOptionDeviceScanClick() {
        getMvpView().openDeviceScanActivity();
        //TODO Implement onDrawerOptionDeviceScanClick()
    }

    @Override
    public void onNavigationMenuCreated() {
        //TODO Implement onNavigationMenuCreated()
    }

    @Override
    public void onViewInitialized() {
        //TODO Implement onViewInitialized()
    }

    @Override
    public void onMapReady(MapboxMap map) {
        this.map = map;

    }

    @Override
    public void onLocationPermissionsRequest(@NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onLocationPermissionsRequest(permissions, grantResults);
        locationService.start(LOCATION_UPDATE_INTERVAL);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrientationUpdateEvent(OrientationUpdateEvent event) {
        getMvpView().showUserRotation(event.ORIENTATION[0]);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationUpdateEvent(LocationUpdateEvent event) {
        switch (event.STATE) {
            case NO_LOCATION_PERMISSION:
                getMvpView().requestLocationPermission();
                break;
            case STARTED:
                getMvpView().showMessage("Location Service Started");
                break;
            case LOCATION_UPDATE:
                if (event.LOCATION != null) {
                    getMvpView().showUserLocation(event.LOCATION);
                }
                break;
            case STOPPED:
                getMvpView().showMessage("Location Service Stopped");
                break;
            case ERROR:
                getMvpView().showMessage("Location Service Error");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHotspotMessageEvent(HotspotMessageEvent event) {
        Log.d(TAG, "onHotspotMessageEvent");
        hotspotList.add(event.HOTSPOT);
        getMvpView().showHotspot(event.HOTSPOT);
        postHotspot(event.HOTSPOT);
    }

    private void postHotspot(HotspotMessageProto.Hotspot hotspot) {
        Point point = Point.fromLngLat(hotspot.getLocation().getLatitude(), hotspot.getLocation().getLongitude());
        Feature feature = Feature.fromGeometry(point);
        String phNumber = "PH-111";
        feature.addStringProperty("ph_number", phNumber);
        Hashids hashids = new Hashids(phNumber);
        long random = 1L + (long) (Math.random() * (Hashids.MAX_NUMBER - 1L));
        feature.addStringProperty("id", hashids.encode(random));
        feature.addStringProperty("timestamp", CommonUtils.getTimeStamp());
        feature.addStringProperty("lat_drone", String.valueOf(hotspot.getLocation().getLatitude()));
        feature.addStringProperty("lon_drone", String.valueOf(hotspot.getLocation().getLongitude()));
        feature.addStringProperty("z_drone", String.valueOf(0));
        feature.addStringProperty("lat_haard", String.valueOf(hotspot.getLocation().getLatitude()));
        feature.addStringProperty("lon_haard", String.valueOf(hotspot.getLocation().getLongitude()));
        feature.addStringProperty("temperatuur", String.valueOf(hotspot.getTemperature()));

        Disposable disposable = getDataManager().postPointData(FeatureCollection.fromFeature(feature).toJson())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(response -> Log.d(TAG, response.toString()));
        getCompositeDisposable().add(disposable);
    }
}
