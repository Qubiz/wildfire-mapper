package nl.dronexpert.wildfiremapper.ui.devicescan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.dronexpert.wildfiremapper.R;
import nl.dronexpert.wildfiremapper.ui.base.BaseActivity;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpView;
import nl.dronexpert.wildfiremapper.ui.main.MainActivity;

public class DeviceScanActivity extends BaseActivity implements DeviceScanMvpView {

    @Inject
    DeviceScanMvpPresenter<DeviceScanMvpView> presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
    protected void setUp() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                Objects.requireNonNull(upIntent).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
