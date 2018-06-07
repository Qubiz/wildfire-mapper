package nl.dronexpert.wildfiremapper.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import com.mapbox.mapboxsdk.Mapbox;

import com.wealthfront.magellan.ActionBarConfig;
import com.wealthfront.magellan.NavigationListener;
import com.wealthfront.magellan.Navigator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import nl.dronexpert.wildfiremapper.R;
import nl.dronexpert.wildfiremapper.WildfireMapperApplication;
import nl.dronexpert.wildfiremapper.di.component.ActivityComponent;
import nl.dronexpert.wildfiremapper.di.component.DaggerActivityComponent;
import nl.dronexpert.wildfiremapper.di.module.ActivityModule;

/**
 * Created by Mathijs de Groot on 06/06/2018.
 */
public class WildfireMapperActivity extends AppCompatActivity implements NavigationListener {

    @Inject
    Mapbox mapbox;

    @Inject
    Navigator navigator;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static ActivityComponent activityComponent;

    public static ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wildfire_mapper_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(WildfireMapperApplication.getComponent())
                .build();

        activityComponent.inject(this);

        navigator.onCreate(this, savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (!navigator.handleBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onNavigate(ActionBarConfig actionBarConfig) {
        // Update action bar config based on new Screen settings
        setActionBarColor(actionBarConfig.colorRes());
        if (actionBarConfig.visible()) {
            showActionBar(actionBarConfig.animated());
        } else {
            hideActionBar(actionBarConfig.animated());
        }

        if (navigator.atRoot()) {
//TODO            toolbar.setNavigationIcon(R.drawable.action_bar_icon);
        } else {
//TODO            toolbar.setNavigationIcon(R.drawable.back_arrow);
        }
    }

    private void hideActionBar(boolean animated) {
        if (!animated) {
            // Normally you'd probably want the action bar to be GONE.
            // Set to INVISIBLE here to avoid jumping after navigation completes.
            toolbar.setVisibility(View.GONE);
            return;
        }

        // Note, this simply moves the action bar off the top of the screen.
        // This may not be sufficient for other applications when hiding the
        // toolbar, as the space on the screen for the toolbar will still be
        // shown, but it will be blank. It's more complicated to do a real
        // collapse animation that animates the view height, and we didn't
        // want to complicate the sample too much
        int actionBarHeight = getActionBarHeight();
        toolbar.animate().y(-actionBarHeight).start();
    }

    private int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return 0;
    }

    private void showActionBar(boolean animated) {
        if (!animated) {
            toolbar.setVisibility(View.VISIBLE);
            return;
        }

        // Toolbar is visible by default, so we just need to undo the hide
        // animation. This would be different if you used a real collapse
        // animation that changed the height of the toolbar instead of just
        // moving the toolbar off the top of the screen.
        toolbar.setVisibility(View.VISIBLE);
        toolbar.animate().y(0).start();
    }

    private void setActionBarColor(int colorRes) {
        toolbar.setBackgroundColor(getResources().getColor(colorRes));
    }
}
