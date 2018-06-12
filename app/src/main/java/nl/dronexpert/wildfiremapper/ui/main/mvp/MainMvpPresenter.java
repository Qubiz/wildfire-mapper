package nl.dronexpert.wildfiremapper.ui.main.mvp;

import nl.dronexpert.wildfiremapper.di.annotations.PerActivity;
import nl.dronexpert.wildfiremapper.ui.base.mvp.MvpPresenter;

@PerActivity
public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {
    //TODO Define interaction methods
    void onDrawerOptionDeviceScanClick();
    void onNavigationMenuCreated();
    void onViewInitialized();
}
