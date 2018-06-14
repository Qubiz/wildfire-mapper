package nl.dronexpert.wildfiremapper.services.mldp.events;

import android.util.Log;

/**
 * Created by Mathijs de Groot on 13/06/2018.
 */
public class ScanEvent {

    public static final String TAG = ScanEvent.class.getSimpleName();

    public enum State {
        STARTED,
        ERROR,
        FINISHED
    }

    public final State SCAN_STATE;

    public ScanEvent(State state) {
        Log.d(TAG, state.name());
        this.SCAN_STATE = state;
    }

}
