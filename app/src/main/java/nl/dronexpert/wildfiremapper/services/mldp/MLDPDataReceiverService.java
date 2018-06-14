package nl.dronexpert.wildfiremapper.services.mldp;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;

import nl.dronexpert.wildfiremapper.services.base.BaseService;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class MLDPDataReceiverService extends BaseService {

    public static final String TAG = MLDPDataReceiverService.class.getSimpleName();

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MLDPDataReceiverService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getServiceComponent().inject(this);
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
