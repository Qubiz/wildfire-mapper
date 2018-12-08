package nl.dronexpert.wildfiremapper.ui.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.dronexpert.wildfiremapper.R;
//import nl.dronexpert.wildfiremapper.data.protobuf.hotspot.HotspotProto;
import nl.dronexpert.wildfiremapper.data.protobuf.HotspotMessageProto;
import nl.dronexpert.wildfiremapper.ui.base.BaseActivity;
import nl.dronexpert.wildfiremapper.ui.devicescan.DeviceScanActivity;
import nl.dronexpert.wildfiremapper.ui.map.mvp.MapMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.map.mvp.MapMvpView;
import nl.dronexpert.wildfiremapper.utils.CommonUtils;
import nl.dronexpert.wildfiremapper.utils.LinearGradient;

public class MapActivity extends BaseActivity implements MapMvpView {

    public static final String TAG = MapActivity.class.getSimpleName();

    @Inject
    MapMvpPresenter<MapMvpView> presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_view)
    DrawerLayout drawer;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.mapView)
    MapView mapView;

    private MapboxMap map;
    private Marker userLocationMarker;
    private IconicsDrawable myLocationIcon;
    private IconicsDrawable hotspotIcon;
    private IconicsDrawable droneIcon;

    private static final int[] GRADIENT_COLORS = new int[] {Color.rgb(255,255,102), Color.YELLOW, Color.RED};
    private LinearGradient linearGradient = new LinearGradient(GRADIENT_COLORS);
    private double MIN_TEMPERATURE_VALUE = 30.0;
    private double MAX_TEMPERATURE_VALUE = 150.0;

    private ActionBarDrawerToggle drawerToggle;

    private SensorManager sensorManager;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MapActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);
        setUnbinder(ButterKnife.bind(this));
        presenter.onAttach(this);
        setUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (drawer != null) {
            unlockDrawer();
        }

    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Drawable drawable = item.getIcon();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        switch (item.getItemId()) {
            case R.id.nav_item_device_scan:
                presenter.onDrawerOptionDeviceScanClick();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void setUp() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        setupNavigationMenu();

        myLocationIcon = new IconicsDrawable(this)
                .icon(MaterialDesignIconic.Icon.gmi_navigation)
                .color(getResources().getColor(R.color.white))
                .sizeDp(10);

        hotspotIcon = new IconicsDrawable(this)
                .icon(MaterialDesignIconic.Icon.gmi_circle)
                .color(getResources().getColor(R.color.white))
                .sizeDp(10);

        droneIcon = new IconicsDrawable(this)
                .icon(MaterialDesignIconic.Icon.gmi_navigation)
                .color(getResources().getColor(R.color.colorAccent))
                .sizeDp(10);

        mapView.getMapAsync(this);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    void setupNavigationMenu() {
        View headerLayout = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(item -> {
            drawer.closeDrawer(GravityCompat.START);
            switch (item.getItemId()) {
                case R.id.nav_item_device_scan:
                    presenter.onDrawerOptionDeviceScanClick();
                    return true;
                default:
                    return false;
            }
        });
        presenter.onNavigationMenuCreated();
    }

    @Override
    public void closeNavigationDrawer() {
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onFragmentAttached() { }

    @Override
    public void onFragmentDetached(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .remove(fragment)
                    .commitNow();
            unlockDrawer();
        }
    }

    @Override
    public void lockDrawer() {
        if (drawer != null) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void unlockDrawer() {
        if (drawer != null) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    public void openDeviceScanActivity() {
        Intent intent = DeviceScanActivity.getStartIntent(this);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                presenter.onLocationPermissionsRequest(permissions, grantResults);
                return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(MapboxMap map) {
        map.getUiSettings().setRotateGesturesEnabled(false);
        this.map = map;
        presenter.onMapReady(map);
    }

    @Override
    public void setCameraPosition(Location location, double zoom) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), zoom));
    }

    @Override
    public void showUserLocation(Location location) {
        if (map != null) {
            if (userLocationMarker == null) {
                userLocationMarker = map.addMarker(new MarkerOptions()
                        .icon(IconFactory.getInstance(this).fromBitmap(CommonUtils.iconicsDrawableToBitmap(myLocationIcon)))
                        .position(new LatLng(location.getLatitude(), location.getLongitude())));
                setCameraPosition(location, 15);
            } else {
                userLocationMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }
    }

    @Override
    public void showUserRotation(float angle) {
        if (userLocationMarker != null) {
            userLocationMarker.setIcon(IconFactory.getInstance(this)
                    .fromBitmap(CommonUtils.rotateBitmap(CommonUtils.iconicsDrawableToBitmap(myLocationIcon), (float) (angle * 180 / Math.PI))));
        }
    }

    @Override
    public void showHotspot(HotspotMessageProto.Hotspot hotspot) {
        if (map != null) {

            double r = normalize(hotspot.getTemperature(), MIN_TEMPERATURE_VALUE, MAX_TEMPERATURE_VALUE);
            hotspotIcon.color(linearGradient.getColor(r));

            map.addMarker(new MarkerOptions()
                   .icon(IconFactory.getInstance(this).fromBitmap(CommonUtils.iconicsDrawableToBitmap(hotspotIcon)))
                   .position(new LatLng(hotspot.getLocation().getLatitude(), hotspot.getLocation().getLongitude()))
                   .setSnippet("Temperature: " + hotspot.getTemperature()));

            map.addMarker(new MarkerOptions()
                   .icon(IconFactory.getInstance(this).fromBitmap(CommonUtils.rotateBitmap(CommonUtils.iconicsDrawableToBitmap(droneIcon),
                           (float) (hotspot.getDroneInfo().getHeading() * 180 / Math.PI))))
                   .position(new LatLng(hotspot.getDroneInfo().getLocation().getLatitude(), hotspot.getDroneInfo().getLocation().getLongitude())));
        }
    }

    private double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }
}
