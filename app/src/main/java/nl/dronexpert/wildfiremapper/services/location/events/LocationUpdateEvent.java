package nl.dronexpert.wildfiremapper.services.location.events;

import android.location.Location;
import android.support.annotation.Nullable;

/**
 * Created by Mathijs de Groot on 14/06/2018.
 */
public class LocationUpdateEvent {

    public enum State {
        NO_LOCATION_PERMISSION,
        STARTED,
        LOCATION_UPDATE,
        STOPPED,
        ERROR
    }

    public final Location LOCATION;
    public final State STATE;

    public LocationUpdateEvent(State state, @Nullable Location location) {
        this.LOCATION = location;
        this.STATE = state;
    }

}
