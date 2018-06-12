package nl.dronexpert.wildfiremapper.ui.splash;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.di.annotations.ActivityContext;
import nl.dronexpert.wildfiremapper.ui.base.BasePresenter;
import nl.dronexpert.wildfiremapper.ui.splash.mvp.SplashMvpPresenter;
import nl.dronexpert.wildfiremapper.ui.splash.mvp.SplashMvpView;
import nl.dronexpert.wildfiremapper.utils.rx.AppSchedulerProvider;
import nl.dronexpert.wildfiremapper.utils.rx.SchedulerProvider;

public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V> implements SplashMvpPresenter<V> {

    @Inject
    public SplashPresenter(DataManager dataManager,
                           SchedulerProvider schedulerProvider,
                           @ActivityContext CompositeDisposable compositeDisposable) {

        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        getCompositeDisposable().add(Completable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(getSchedulerProvider().ui())
                .subscribe(() -> getMvpView().openMainActivity()));
    }




}
