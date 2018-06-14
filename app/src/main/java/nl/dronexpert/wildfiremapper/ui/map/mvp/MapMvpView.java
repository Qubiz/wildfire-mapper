package nl.dronexpert.wildfiremapper.ui.map.mvp;

import nl.dronexpert.wildfiremapper.ui.base.mvp.MvpView;

public interface MapMvpView extends MvpView {
    //TODO Implement methods to update the view
    void closeNavigationDrawer();
    void lockDrawer();
    void unlockDrawer();
    void openDeviceScanActivity();
}
