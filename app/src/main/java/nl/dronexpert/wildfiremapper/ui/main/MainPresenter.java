package nl.dronexpert.wildfiremapper.ui.main;

import android.content.Intent;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.ui.base.BasePresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.DeviceScanActivity;
import nl.dronexpert.wildfiremapper.ui.main.mvp.MainMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.main.mvp.MainMvpView;
import nl.dronexpert.wildfiremapper.utils.rx.SchedulerProvider;

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {

    private static final String TAG = MainPresenter.class.getSimpleName();

    @Inject
    public MainPresenter(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
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
