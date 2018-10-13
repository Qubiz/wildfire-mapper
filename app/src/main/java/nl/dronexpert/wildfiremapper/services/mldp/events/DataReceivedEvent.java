package nl.dronexpert.wildfiremapper.services.mldp.events;

/**
 * Created by Mathijs de Groot on 14/06/2018.
 */
public class DataReceivedEvent {

    public final byte[] DATA;

    public DataReceivedEvent(byte[] data) {
        this.DATA = data;
    }

}
