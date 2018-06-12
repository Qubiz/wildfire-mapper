package nl.dronexpert.wildfiremapper.ui.base.mvp;

/**
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface MvpSubView extends MvpView {
    void onCreate();
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
    void attachParentMvpView(MvpView mvpView);
}
