package nl.dronexpert.wildfiremapper.ui.splash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import javax.inject.Inject;

import butterknife.ButterKnife;
import nl.dronexpert.wildfiremapper.R;
import nl.dronexpert.wildfiremapper.ui.base.BaseActivity;
import nl.dronexpert.wildfiremapper.ui.map.MapActivity;
import nl.dronexpert.wildfiremapper.ui.splash.mvp.SplashMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.splash.mvp.SplashMvpView;

public class SplashActivity extends BaseActivity implements SplashMvpView {

    @Inject
    SplashMvpPresenter<SplashMvpView> presenter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getActivityComponent().inject(this);

        setUnbinder(ButterKnife.bind(this));

        presenter.onAttach(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void openMainActivity() {
        Intent intent = MapActivity.getStartIntent(this);
        startActivity(intent);
        finish();
    }

}
