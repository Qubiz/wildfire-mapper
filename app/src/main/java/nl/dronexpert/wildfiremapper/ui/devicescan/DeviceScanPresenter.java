package nl.dronexpert.wildfiremapper.ui.devicescan;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.ui.base.BasePresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpView;
import nl.dronexpert.wildfiremapper.utils.rx.SchedulerProvider;

public class DeviceScanPresenter<V extends DeviceScanMvpView> extends BasePresenter<V> implements DeviceScanMvpPresenter<V> {

    private static final String TAG = DeviceScanPresenter.class.getSimpleName();

    @Inject
    public DeviceScanPresenter(DataManager dataManager,
                               SchedulerProvider schedulerProvider,
                               CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

}
