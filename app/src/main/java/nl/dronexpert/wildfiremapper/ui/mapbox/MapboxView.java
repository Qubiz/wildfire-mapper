package nl.dronexpert.wildfiremapper.ui.mapbox;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
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
        inflate(context, R.layout.mapbox, this);
        ButterKnife.bind(this);
        mapView.onCreate(null);
    }

    public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
        mapView.getMapAsync(onMapReadyCallback);
    }
}
