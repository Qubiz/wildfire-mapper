package nl.dronexpert.wildfiremapper.ui.map.mvp;

import android.location.Location;

import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import nl.dronexpert.wildfiremapper.data.protobuf.HotspotMessageProto;
import nl.dronexpert.wildfiremapper.ui.base.mvp.MvpView;

public interface MapMvpView extends MvpView, OnMapReadyCallback {
    //TODO Implement methods to update the view
    void closeNavigationDrawer();
    void lockDrawer();
    void unlockDrawer();
    void openDeviceScanActivity();
    void setCameraPosition(Location location, double zoom);
    void showUserLocation(Location location);
    void showUserRotation(float angle);
    void showHotspot(HotspotMessageProto.Hotspot hotspot);
}
