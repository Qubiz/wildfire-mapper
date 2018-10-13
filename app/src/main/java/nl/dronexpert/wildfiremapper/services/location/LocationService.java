package nl.dronexpert.wildfiremapper.services.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;
import com.github.pwittchen.reactivesensors.library.ReactiveSensorFilter;
import com.github.pwittchen.reactivesensors.library.ReactiveSensors;
import com.google.android.gms.location.LocationRequest;
import com.patloew.rxlocation.RxLocation;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nl.dronexpert.wildfiremapper.services.base.BaseService;
import nl.dronexpert.wildfiremapper.services.location.events.LocationUpdateEvent;
import nl.dronexpert.wildfiremapper.services.location.events.OrientationUpdateEvent;

import static nl.dronexpert.wildfiremapper.services.location.events.LocationUpdateEvent.State;

/**
 * Created by Mathijs de Groot on 14/06/2018.
 */
public class LocationService extends BaseService {

    public static final String TAG = LocationService.class.getSimpleName();

    @Inject
    RxLocation rxLocation;

    @Inject
    ReactiveSensors reactiveSensors;

    private Disposable locationDisposable;

    private Disposable accelerometerDisposable;
    private Disposable magnetometerDisposable;

    private float[] acceleration;
    private float[] magnetic;
    private float[] orientation = new float[3];

    public static Intent getStartIntent(Context context) {
        return new Intent(context, LocationService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getServiceComponent().inject(this);
    }

    public void start(long interval) {
        if (isLocationUpdatesStarted() || isOrientationUpdatesStarted()) {
            stop();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            EventBus.getDefault().post(new LocationUpdateEvent(State.NO_LOCATION_PERMISSION, null));
        } else {
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(interval);

            locationDisposable = rxLocation.location().updates(locationRequest)
                    .doOnError(this::onError)
                    .subscribe(this::onLocationUpdate, this::onError);

            getCompositeDisposable().add(locationDisposable);
        }

        if (reactiveSensors.hasSensor(Sensor.TYPE_ACCELEROMETER) && reactiveSensors.hasSensor(Sensor.TYPE_MAGNETIC_FIELD)) {
            accelerometerDisposable = reactiveSensors
                    .observeSensor(Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_UI)
                    .subscribeOn(Schedulers.computation())
                    .filter(ReactiveSensorFilter.filterSensorChanged())
                    .observeOn(AndroidSchedulers.mainThread())
                    .buffer(1, TimeUnit.SECONDS)
                    .subscribe(this::onSensorEvent, this::onError);

            magnetometerDisposable = reactiveSensors
                    .observeSensor(Sensor.TYPE_MAGNETIC_FIELD, SensorManager.SENSOR_DELAY_UI)
                    .subscribeOn(Schedulers.computation())
                    .filter(ReactiveSensorFilter.filterSensorChanged())
                    .observeOn(AndroidSchedulers.mainThread())
                    .buffer(1, TimeUnit.SECONDS)
                    .subscribe(this::onSensorEvent, this::onError);
        }
    }

    private void onSensorEvent(List<ReactiveSensorEvent> sensorEventList) {
        float[] sum = {0, 0, 0};

        for (ReactiveSensorEvent event : sensorEventList) {
            sum[0] += event.getSensorEvent().values[0];
            sum[1] += event.getSensorEvent().values[1];
            sum[2] += event.getSensorEvent().values[2];
        }

        sum[0] /= sensorEventList.size();
        sum[1] /= sensorEventList.size();
        sum[2] /= sensorEventList.size();

        if (!sensorEventList.isEmpty()) {
            switch (sensorEventList.get(0).getSensorEvent().sensor.getType()) {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magnetic = sum.clone();
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    acceleration = sum.clone();
                    break;
            }
        }

        if (magnetic != null && acceleration != null) {
            calculateOrientation(acceleration, magnetic);
        }
    }

    private void calculateOrientation(float[] acceleration, float[] magnetic) {
        float[] R = new float[16];
        float[] I = new float[16];
        SensorManager.getRotationMatrix(R, I, acceleration, magnetic);
        SensorManager.getOrientation(R, orientation);

        EventBus.getDefault().post(new OrientationUpdateEvent(orientation));
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, throwable.getMessage());
        EventBus.getDefault().post(new LocationUpdateEvent(State.ERROR, null));
    }

    private void onLocationUpdate(Location location) {
        EventBus.getDefault().post(new LocationUpdateEvent(State.LOCATION_UPDATE, location));
    }

    public void stop() {
        if (locationDisposable != null) {
            locationDisposable.dispose();
        }

        if (magnetometerDisposable != null) {
            magnetometerDisposable.dispose();
        }

        if (accelerometerDisposable != null) {
            accelerometerDisposable.dispose();
        }
    }

    public boolean isLocationUpdatesStarted() {
        return locationDisposable != null && !locationDisposable.isDisposed();
    }

    public boolean isOrientationUpdatesStarted() {
        return accelerometerDisposable != null && magnetometerDisposable != null
                && !accelerometerDisposable.isDisposed() && !magnetometerDisposable.isDisposed();
    }

    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder<LocationService>() {
            @Override
            public LocationService getService() {
                return LocationService.this;
            }
        };
    }

}
