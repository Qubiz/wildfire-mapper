package nl.dronexpert.wildfiremapper.services.mldp.events;

import android.util.Log;

import javax.annotation.Nullable;

import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;

/**
 * Created by Mathijs de Groot on 13/06/2018.
 */
public class ScanEvent {

    public static final String TAG = ScanEvent.class.getSimpleName();

    public enum State {
        STARTED,
        ERROR,
        NEW_SCAN_RESULT,
        FINISHED
    }

    public final State SCAN_STATE;
    public final BleDevice BLE_DEVICE;

    public ScanEvent(State state, @Nullable BleDevice device) {
        Log.d(TAG, state.name());
        this.SCAN_STATE = state;
        this.BLE_DEVICE = device;
    }

}
