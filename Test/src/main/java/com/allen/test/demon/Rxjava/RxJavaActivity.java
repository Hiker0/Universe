package com.allen.test.demon.Rxjava;

import android.os.Bundle;

import com.allen.test.R;

import com.allen.z.library.utils.ActivityUtils;


/**
 * Created by Johnson on 2017-04-24.
 */
public class RxJavaActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RxjavaFragment rxjavaFragment = (RxjavaFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (rxjavaFragment == null) {
            rxjavaFragment = RxjavaFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    rxjavaFragment, R.id.contentFrame);
        }
    }

    @Override
    int getContentLayout() {
        return R.layout.activity_demon_rxjava;
    }
}
