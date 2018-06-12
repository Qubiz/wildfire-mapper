package nl.dronexpert.wildfiremapper.ui.devicescan;

import android.view.View;
import android.widget.AdapterView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.data.database.events.DatabaseUpdateEvent;
import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;
import nl.dronexpert.wildfiremapper.di.annotations.ActivityContext;
import nl.dronexpert.wildfiremapper.services.ble.BleDeviceScanService;
import nl.dronexpert.wildfiremapper.ui.base.BasePresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpView;
import nl.dronexpert.wildfiremapper.utils.rx.SchedulerProvider;

public class DeviceScanPresenter<V extends DeviceScanMvpView> extends BasePresenter<V> implements DeviceScanMvpPresenter<V> {

    private static final String TAG = DeviceScanPresenter.class.getSimpleName();

    @Inject
    public DeviceScanPresenter(DataManager dataManager,
                               SchedulerProvider schedulerProvider,
                               @ActivityContext CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        getCompositeDisposable().add(getDataManager()
                .isBleDevicesEmpty()
                .subscribe(empty -> {
                    if (!empty) {
                        getCompositeDisposable().add(getDataManager()
                                .getAllBleDevices()
                                .subscribe(getMvpView()::addBleDevices));
                    }
                })
        );



        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDatabaseUpdateEvent(DatabaseUpdateEvent event) {
        if (event.DAO_TYPE == DatabaseUpdateEvent.DaoType.BLE_DEVICE_DAO) {
            switch (event.UPDATE_TYPE) {
                case CLEAR:
                    getMvpView().clearBleDevices();
                    break;
                case INSERT:
                    getCompositeDisposable().add(getDataManager()
                            .getBleDevice(event.ENTRY_ID)
                            .subscribe(getMvpView()::addBleDevice));
                    break;
                case SAVE:
                    getCompositeDisposable().add(getDataManager()
                            .getAllBleDevices()
                            .subscribe(getMvpView()::addBleDevices));
                    break;
                case LIST_SAVE:
                    getCompositeDisposable().add(getDataManager()
                            .getAllBleDevices()
                            .subscribe(getMvpView()::addBleDevices));
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        final BleDevice bleDevice = (BleDevice) adapterView.getItemAtPosition(position);
        // TODO Implement: create a connection
    }
}
