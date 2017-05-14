package com.allen.test.demon.Rxjava;


import android.support.v4.app.Fragment;

/**
 * Created by allen.z on 2017-05-14.
 */
public class RxjavaFragment extends Fragment implements RxJavaContract.View {

    public static RxjavaFragment newInstance() {
        return new RxjavaFragment();
    }
    @Override
    public void setPresenter(RxJavaContract.Presenter presenter) {

    }
}
