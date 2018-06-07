package nl.dronexpert.wildfiremapper.ui.mapbox;

import android.content.Context;

import com.mapbox.mapboxsdk.maps.MapView;
import com.wealthfront.magellan.BaseScreenView;

import butterknife.BindView;
import butterknife.ButterKnife;

import nl.dronexpert.wildfiremapper.R;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class MapboxView extends BaseScreenView<MapboxScreen> {

    @BindView(R.id.mapView)
    MapView mapView;

    public MapboxView(Context context) {
        super(context);
        inflate(context, R.layout.mapbox_screen, this);
        ButterKnife.bind(this);
        mapView.onCreate(null);
    }

    public MapView getMapView() {
        return mapView;
    }
}
