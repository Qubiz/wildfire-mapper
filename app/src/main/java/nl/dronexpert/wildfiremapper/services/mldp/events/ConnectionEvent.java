package nl.dronexpert.wildfiremapper.services.mldp.events;

import android.support.annotation.Nullable;
import android.util.Log;

import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import com.polidea.rxandroidble2.RxBleDevice;

import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;

/**
 * Created by Mathijs de Groot on 14/06/2018.
 */
public class ConnectionEvent {

    public static final String TAG = ConnectionEvent.class.getSimpleName();

    public final RxBleConnectionState STATE;
    public final String DEVICE_NAME;
    public final String DEVICE_MAC_ADDRESS;

    public ConnectionEvent(RxBleConnectionState state, String name, String deviceMacAddress) {
        Log.d(TAG, state.name());
        STATE = state;
        DEVICE_NAME = name;
        DEVICE_MAC_ADDRESS = deviceMacAddress;
    }

}
