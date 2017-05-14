package com.allen.test.demon.Rxjava;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by allen.z on 2017-05-14.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());

    }

    abstract int getContentLayout();
}
