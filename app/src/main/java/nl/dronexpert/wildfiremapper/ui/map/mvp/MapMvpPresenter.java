package nl.dronexpert.wildfiremapper.ui.map.mvp;

import nl.dronexpert.wildfiremapper.di.annotations.PerActivity;
import nl.dronexpert.wildfiremapper.ui.base.mvp.MvpPresenter;

@PerActivity
public interface MapMvpPresenter<V extends MapMvpView> extends MvpPresenter<V> {
    //TODO Define interaction methods
    void onDrawerOptionDeviceScanClick();
    void onNavigationMenuCreated();
    void onViewInitialized();
}
