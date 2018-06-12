package nl.dronexpert.wildfiremapper.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.Unbinder;
import nl.dronexpert.wildfiremapper.di.component.ActivityComponent;
import nl.dronexpert.wildfiremapper.ui.base.mvp.MvpView;
import nl.dronexpert.wildfiremapper.utils.CommonUtils;

public abstract class BaseFragment extends Fragment implements MvpView {

        private BaseActivity activity;

        private Unbinder unbinder;

        private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.activity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

    @Override
    public void showLoading() {
        hideLoading();
        progressDialog = CommonUtils.showLoadingDialog(getContext());
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onError(int resId) {
        if (activity != null) {
            activity.onError(resId);
        }
    }

    @Override
    public void onError(String message) {
        if (activity != null) {
            activity.onError(message);
        }
    }

    @Override
    public void showMessage(int resId) {
        if (activity != null) {
            activity.showMessage(resId);
        }
    }

    @Override
    public void showMessage(String message) {
        if (activity != null) {
            activity.showMessage(message);
        }
    }

    @Override
    public void hideKeyboard() {
        if (activity != null) {
            activity.hideKeyboard();
        }
    }

    public ActivityComponent getActivityComponent() {
        if (activity != null) {
            return activity.getActivityComponent();
        } else {
            return null;
        }
    }

    public BaseActivity getBaseActivity() {
        return activity;
    }

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    @Override
    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    protected abstract void setUp(View view);

    public interface Callback {
        void onFragmentAttached();
        void onFragmentDetached(String tag);
    }
}
