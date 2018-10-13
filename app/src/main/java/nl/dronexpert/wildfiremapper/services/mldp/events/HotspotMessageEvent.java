package nl.dronexpert.wildfiremapper.services.mldp.events;

import nl.dronexpert.wildfiremapper.data.protobuf.HotspotMessageProto;

/**
 * Created by Mathijs de Groot on 19/06/2018.
 */
public class HotspotMessageEvent {

    public final HotspotMessageProto.Hotspot HOTSPOT;

    public HotspotMessageEvent(HotspotMessageProto.Hotspot hotspot) {
        this.HOTSPOT = hotspot;
    }

}
