package nl.dronexpert.wildfiremapper.ui.map;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.di.annotations.ActivityContext;
import nl.dronexpert.wildfiremapper.ui.base.BasePresenter;
import nl.dronexpert.wildfiremapper.ui.map.mvp.MapMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.map.mvp.MapMvpView;
import nl.dronexpert.wildfiremapper.utils.rx.SchedulerProvider;

public class MapPresenter<V extends MapMvpView> extends BasePresenter<V> implements MapMvpPresenter<V> {

    private static final String TAG = MapPresenter.class.getSimpleName();

    @Inject
    public MapPresenter(DataManager dataManager,
                        SchedulerProvider schedulerProvider,
                        @ActivityContext CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onDrawerOptionDeviceScanClick() {
        getMvpView().openDeviceScanActivity();
        //TODO Implement onDrawerOptionDeviceScanClick()
    }

    @Override
    public void onNavigationMenuCreated() {
        //TODO Implement onNavigationMenuCreated()
    }

    @Override
    public void onViewInitialized() {
        //TODO Implement onViewInitialized()
    }
}
