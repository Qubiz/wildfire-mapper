package nl.dronexpert.wildfiremapper.ui.base;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import nl.dronexpert.wildfiremapper.data.DataManager;
import nl.dronexpert.wildfiremapper.ui.base.mvp.MvpPresenter;
import nl.dronexpert.wildfiremapper.ui.base.mvp.MvpView;
import nl.dronexpert.wildfiremapper.utils.rx.SchedulerProvider;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private static final String TAG = BasePresenter.class.getSimpleName();

    private final DataManager dataManager;
    private final SchedulerProvider schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    private V mvpView;

    @Inject
    public BasePresenter(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void onDetach() {
        compositeDisposable.dispose();
        mvpView = null;
    }

    public boolean isViewAttached() {
        return mvpView != null;
    }

    public V getMvpView() {
        return mvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public SchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }

}
