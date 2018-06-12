package nl.dronexpert.wildfiremapper.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import nl.dronexpert.wildfiremapper.di.annotations.ActivityContext;
import nl.dronexpert.wildfiremapper.di.annotations.PerActivity;
import nl.dronexpert.wildfiremapper.ui.devicescan.DeviceScanPresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.devicescan.mvp.DeviceScanMvpView;
import nl.dronexpert.wildfiremapper.ui.main.MainPresenter;
import nl.dronexpert.wildfiremapper.ui.main.mvp.MainMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.main.mvp.MainMvpView;
import nl.dronexpert.wildfiremapper.ui.splash.SplashPresenter;
import nl.dronexpert.wildfiremapper.ui.splash.mvp.SplashMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.splash.mvp.SplashMvpView;
import nl.dronexpert.wildfiremapper.utils.rx.AppSchedulerProvider;
import nl.dronexpert.wildfiremapper.utils.rx.SchedulerProvider;

@Module
public class ActivityModule {

    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @PerActivity
    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(SplashPresenter<SplashMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> provideMainPresenter(MainPresenter<MainMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    DeviceScanMvpPresenter<DeviceScanMvpView> provideDeviceScanPresenter(DeviceScanPresenter<DeviceScanMvpView> presenter) {
        return presenter;
    }

    //TODO Implement providers for the presenters of the views

}
