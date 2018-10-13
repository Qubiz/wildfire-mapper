package nl.dronexpert.wildfiremapper.services.mldp;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import nl.dronexpert.wildfiremapper.data.protobuf.HeaderProto;
import nl.dronexpert.wildfiremapper.data.protobuf.HotspotMessageProto;
import nl.dronexpert.wildfiremapper.services.base.BaseService;
import nl.dronexpert.wildfiremapper.services.mldp.events.DataReceivedEvent;
import nl.dronexpert.wildfiremapper.services.mldp.events.HotspotMessageEvent;
import okio.Buffer;
import okio.ByteString;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class MLDPDataReceiverService extends BaseService {

    public static final String TAG = MLDPDataReceiverService.class.getSimpleName();

    private static final ByteString SYNC_WORD = ByteString.decodeHex("AA55");

    private Buffer buffer = new Buffer();
    private boolean inSync = false;

    private HeaderProto.Header header;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MLDPDataReceiverService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getServiceComponent().inject(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataReceived(DataReceivedEvent event) {
        buffer.write(event.DATA);
        ByteString byteString;

        if (!inSync) {
            byteString = buffer.readByteString();
            int indexSyncWord = byteString.lastIndexOf(SYNC_WORD);
            if (indexSyncWord != -1) {
                // Sync word is completely in the buffer.
                // Cut off the sync word and everything that is in front of it.
                // Store the remainder in the byteString.
                byteString = byteString.substring(indexSyncWord + 2, byteString.size());
                // We are now in sync.
                inSync = true;
                Log.d(TAG, "[ INFO ] Found a sync word.");
            } else {
                inSync = false;
            }
            // Write the remainder back into the buffer.
            buffer.write(byteString);
        } else {
            if (header == null) {
                if (buffer.size() >= 10) {
                    Log.d(TAG, "[ INFO ] Trying to parse a header.");
                    byteString = buffer.readByteString();
                    try {
                        header = HeaderProto.Header.parseFrom(byteString.substring(0, 10).toByteArray());
                        byteString = byteString.substring(10);
                        Log.d(TAG, "[ SUCCESS ] Header parsed correctly.");
                    } catch (InvalidProtocolBufferException e) {
                        // Failed to parse a header. Therefore we are now out of sync.
                        // We should retry to find a sync word.
                        header = null;
                        inSync = false;
                        Log.d(TAG, "[ FAILED ] Could not parse the header. " + e.getMessage());
                    }
                    // Write the remainder back into the buffer.
                    buffer.write(byteString);
                } else {
                    Log.d(TAG, "[ INFO ] The header has not yet been fully received... [" + (buffer.size()) + " / " + 10 + "] bytes.");
                }
            } else {
                // We have parsed a header, and can now try to parse the message.
                if (buffer.size() >= header.getMessageSize()) {
                    Log.d(TAG, "[ INFO ] Trying to parse a message with message id [" + header.getMessageType() + "].");
                    byteString = parseMessage(buffer.readByteString(), header);
                    header = null;
                    inSync = false;
                    // Write the remainder back into the buffer.
                    buffer.write(byteString);
                } else {
                    Log.d(TAG, "[ INFO ] The message has not yet been fully received... [" + (buffer.size()) + " / " + header.getMessageSize() + "] bytes.");
                }
            }
        }

    }

    private ByteString parseMessage(ByteString byteString, HeaderProto.Header header) {
        if (header.getMessageType() == HotspotMessageProto.Hotspot.getDescriptor().getName().hashCode()) {
            try {
                HotspotMessageProto.Hotspot hotspot = HotspotMessageProto.Hotspot.parseFrom(byteString.substring(0, header.getMessageSize()).toByteArray());
                EventBus.getDefault().postSticky(new HotspotMessageEvent(hotspot));
                return byteString.substring(header.getMessageSize());
            } catch (InvalidProtocolBufferException e) {
                Log.d(TAG, "[ FAILED ] Could not parse the message. " + e.getMessage());
            }
        } else {
            Log.d(TAG, "[ FAILED ] Unknown message id.");
        }

        return byteString;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @NonNull
    @Override
    public final IBinder onBind(Intent intent) {
        return new LocalBinder<MLDPDataReceiverService>() {
            @Override
            public MLDPDataReceiverService getService() {
                return MLDPDataReceiverService.this;
            }
        };
    }



}
