package nl.dronexpert.wildfiremapper.services.mldp;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;

import nl.dronexpert.wildfiremapper.services.base.BaseService;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class MLDPConnectionService extends BaseService {

    public static final String TAG = MLDPConnectionService.class.getSimpleName();

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MLDPConnectionService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getServiceComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @NonNull
    @Override
    public final IBinder onBind(Intent intent) {
        return new LocalBinder<MLDPConnectionService>() {
            @Override
            public MLDPConnectionService getService() {
                return MLDPConnectionService.this;
            }
        };
    }
}
