package nl.dronexpert.wildfiremapper.ui.devicescan;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.dronexpert.wildfiremapper.R;
import nl.dronexpert.wildfiremapper.data.database.model.BleDevice;
import nl.dronexpert.wildfiremapper.ui.base.BaseActivity;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpView;

import static com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic.Icon;

public class DeviceScanActivity extends BaseActivity implements DeviceScanMvpView {

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    @Inject
    DeviceScanMvpPresenter<DeviceScanMvpView> presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.devices_list_view)
    ListView devicesListView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    DeviceScanResultsAdapter scanResultsAdapter;

    ProgressBarAnimation progressBarAnimation;

    private boolean isBusy = false;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, DeviceScanActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan);
        getActivityComponent().inject(this);
        setUnbinder(ButterKnife.bind(this));
        presenter.onAttach(this);
        setUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_scan, menu);

        MenuItem menuItemProgress = menu.findItem(R.id.menu_item_in_progress);
        MenuItem menuItemScan = menu.findItem(R.id.menu_item_scan);

        menuItemProgress.setActionView(new ProgressBar(this));
        menuItemScan.setIcon(new IconicsDrawable(this)
                .icon(Icon.gmi_search)
                .color(getResources().getColor(R.color.white))
                .sizeDp(24));

        menuItemScan.setVisible(!isBusy);
        menuItemProgress.setVisible(isBusy);

        return true;
    }

    @Override
    protected void setUp() {
        progressBarAnimation = new ProgressBarAnimation(progressBar, 0, 100);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        devicesListView.setAdapter(scanResultsAdapter);
        scanResultsAdapter.setOnItemClickListener(presenter);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                Objects.requireNonNull(Objects.requireNonNull(upIntent).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            case R.id.menu_item_scan:
                presenter.onMenuItemScanClick();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void showDevices(List<BleDevice> devices) {
        scanResultsAdapter.addAll(devices);
    }

    @Override
    public void showDevice(BleDevice device) {
        scanResultsAdapter.add(device);
    }

    @Override
    public void clearDevices() {
        scanResultsAdapter.clear();
    }

    @Override
    public void requestEnableBluetooth() {
        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    public void showScanStarted(long duration) {
        devicesListView.setOnItemClickListener(null);

        progressBarAnimation.setDuration(duration);
        progressBar.startAnimation(progressBarAnimation);

        setBusy(true);
    }


    @Override
    public void showScanFinished() {
        devicesListView.setOnItemClickListener(presenter);
        setBusy(false);
    }

    @Override
    public void showDeviceConnected(String name, String macAddress) {
        scanResultsAdapter.setDeviceConnected(name, macAddress, true);
        setBusy(false);
    }

    @Override
    public void showDeviceConnecting(String name, String macAddress) {
        setBusy(true);
    }

    @Override
    public void showDeviceDisconnecting(String name, String macAddress) {
        setBusy(true);
    }

    @Override
    public void showDeviceDisconnected(String name, String macAddress) {
        scanResultsAdapter.setDeviceConnected(name, macAddress, false);
        setBusy(false);
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
        invalidateOptionsMenu();

        if (isBusy) {
            devicesListView.setOnItemClickListener(null);
        } else {
            devicesListView.setOnItemClickListener(presenter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
                presenter.onBluetoothEnableRequest(resultCode, data);
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float to;

        ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }
    }
}
