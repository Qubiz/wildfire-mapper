package nl.dronexpert.wildfiremapper.ui.map.mvp;

import com.mapbox.mapboxsdk.maps.MapboxMap;

import nl.dronexpert.wildfiremapper.di.annotations.PerActivity;
import nl.dronexpert.wildfiremapper.ui.base.mvp.MvpPresenter;

@PerActivity
public interface MapMvpPresenter<V extends MapMvpView> extends MvpPresenter<V> {

    void onDrawerOptionDeviceScanClick();
    void onNavigationMenuCreated();
    void onViewInitialized();
    void onMapReady(MapboxMap map);

}
