package com.allen.test.demon.Rxjava;

import com.allen.test.BasePresenter;
import com.allen.test.BaseView;

/**
 * Created by allen.z on 2017-05-14.
 */
public interface RxJavaContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
