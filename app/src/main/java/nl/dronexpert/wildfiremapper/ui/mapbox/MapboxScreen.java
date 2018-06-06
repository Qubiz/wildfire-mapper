package nl.dronexpert.wildfiremapper.ui.mapbox;

import android.content.Context;
import android.util.Log;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import com.wealthfront.magellan.Screen;

import nl.dronexpert.wildfiremapper.R;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class MapboxScreen extends Screen<MapboxView> implements OnMapReadyCallback, MapboxMap.OnCameraMoveListener {

    private static final String TAG = MapboxScreen.class.getSimpleName();

    private MapboxMap mapboxMap;
    private CameraPosition cameraPosition;

    @Override
    protected MapboxView createView(Context context) {
        MapboxView mapboxView = new MapboxView(context);
        mapboxView.getMapView().getMapAsync(this);
        Log.d(TAG, "createView()");
        return mapboxView;
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        if (cameraPosition != null) {
            mapboxMap.setCameraPosition(cameraPosition);
        }
        mapboxMap.addOnCameraMoveListener(this);
        this.mapboxMap = mapboxMap;
    }

    @Override
    public void onCameraMove() {
        // Save the camera position, for when we need to restore the view, i.e. on rotation.
        this.cameraPosition = mapboxMap.getCameraPosition();
    }

    @Override
    protected int getActionBarColorRes() {
        return R.color.colorPrimary;
    }

    @Override
    protected boolean shouldShowActionBar() {
        return false;
    }

    @Override
    protected boolean shouldAnimateActionBar() {
        return false;
    }
}
