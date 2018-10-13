package nl.dronexpert.wildfiremapper.services;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import nl.dronexpert.wildfiremapper.services.base.BaseService;
import nl.dronexpert.wildfiremapper.services.location.LocationService;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPConnectionService;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPDataReceiverService;
import nl.dronexpert.wildfiremapper.services.mldp.MLDPDeviceScanService;
import nl.dronexpert.wildfiremapper.utils.AppLogger;

/**
 * Created by Mathijs de Groot on 13/06/2018.
 */
public class ServiceConnectionHandler implements ServiceConnection {

    public static final String TAG = ServiceConnectionHandler.class.getSimpleName();

    private MLDPDeviceScanService deviceScanService;
    private boolean deviceScanServiceBound = false;

    private MLDPConnectionService connectionService;
    private boolean connectionServiceBound = false;

    private MLDPDataReceiverService dataReceiverService;
    private boolean dataReceiverServiceBound = false;

    private LocationService locationService;
    private boolean locationServiceBound = false;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (name.getClassName().equals(MLDPDeviceScanService.class.getName())) {
            MLDPDeviceScanService.LocalBinder binder = (MLDPDeviceScanService.LocalBinder) service;
            deviceScanService = (MLDPDeviceScanService) binder.getService();
            deviceScanServiceBound = true;

            Log.d(TAG, "Connected to " + MLDPDeviceScanService.TAG);
        }

        if (name.getClassName().equals(MLDPDataReceiverService.class.getName())) {
            MLDPDataReceiverService.LocalBinder binder = (MLDPDataReceiverService.LocalBinder) service;
            dataReceiverService = (MLDPDataReceiverService) binder.getService();
            dataReceiverServiceBound = true;

            Log.d(TAG, "Connected to " + MLDPDataReceiverService.TAG);
        }

        if (name.getClassName().equals(MLDPConnectionService.class.getName())) {
            MLDPConnectionService.LocalBinder binder = (MLDPConnectionService.LocalBinder) service;
            connectionService = (MLDPConnectionService) binder.getService();
            connectionServiceBound = true;

            Log.d(TAG, "Connected to " + MLDPConnectionService.TAG);
        }

        if (name.getClassName().equals(LocationService.class.getName())) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            locationService = (LocationService) binder.getService();
            locationServiceBound = true;

            Log.d(TAG, "Connected to " + LocationService.TAG);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (name.getClassName().equals(MLDPDeviceScanService.class.getName())) {
            deviceScanServiceBound = false;

            Log.d(TAG, "Disconnected from " + MLDPDeviceScanService.TAG);
        }

        if (name.getClassName().equals(MLDPConnectionService.class.getName())) {
            connectionServiceBound = false;

            Log.d(TAG, "Disconnected from " + MLDPConnectionService.TAG);
        }

        if (name.getClassName().equals(MLDPDataReceiverService.class.getName())) {
            dataReceiverServiceBound = false;

            Log.d(TAG, "Disconnected from " + MLDPDataReceiverService.TAG);
        }

        if (name.getClassName().equals(LocationService.class.getName())) {
            locationServiceBound = false;

            Log.d(TAG, "Disconnected from " + LocationService.TAG);
        }
    }

    public MLDPDeviceScanService getDeviceScanService() {
        return deviceScanService;
    }

    public MLDPConnectionService getConnectionService() {
        return connectionService;
    }

    public MLDPDataReceiverService getDataReceiverService() {
        return dataReceiverService;
    }

    public LocationService getLocationService() {
        return locationService;
    }

    public boolean isDeviceScanServiceBound() {
        return deviceScanServiceBound;
    }

    public boolean isConnectionServiceBound() {
        return connectionServiceBound;
    }

    public boolean isDataReceiverServiceBound() {
        return dataReceiverServiceBound;
    }

    public boolean isLocationServiceBound() {
        return locationServiceBound;
    }
}
