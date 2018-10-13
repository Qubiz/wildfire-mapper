package nl.dronexpert.wildfiremapper.ui.base.mvp;

import android.support.annotation.NonNull;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface MvpPresenter<V extends MvpView> {
    void onAttach(V mvpView);
    void onDetach();
    void onLocationPermissionsRequest(@NonNull String[] permissions, @NonNull int[] grantResults);
}
