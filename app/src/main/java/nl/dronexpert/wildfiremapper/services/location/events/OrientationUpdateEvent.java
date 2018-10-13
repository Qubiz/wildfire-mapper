package nl.dronexpert.wildfiremapper.services.location.events;

import android.util.Log;

/**
 * Created by Mathijs de Groot on 16/06/2018.
 */
public class OrientationUpdateEvent {

    public static final String TAG = OrientationUpdateEvent.class.getSimpleName();

    public final float[] ORIENTATION;

    public OrientationUpdateEvent(float[] orienation) {
        ORIENTATION = orienation;
    }

    @Override
    public String toString() {
        return "[" + ORIENTATION[0] + ", " + ORIENTATION[1] + ", " + ORIENTATION[2] + "]" ;
    }
}
