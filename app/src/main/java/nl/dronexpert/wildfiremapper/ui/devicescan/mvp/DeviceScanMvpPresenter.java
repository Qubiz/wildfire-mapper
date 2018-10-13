package nl.dronexpert.wildfiremapper.ui.devicescan.mvp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

import nl.dronexpert.wildfiremapper.di.annotations.PerActivity;
import nl.dronexpert.wildfiremapper.ui.base.mvp.MvpPresenter;

@PerActivity
public interface DeviceScanMvpPresenter<V extends DeviceScanMvpView> extends MvpPresenter<V>, AdapterView.OnItemClickListener {
    //TODO Define interaction methods
    void onMenuItemScanClick();
    void onBluetoothEnableRequest(int resultCode, Intent data);
}
