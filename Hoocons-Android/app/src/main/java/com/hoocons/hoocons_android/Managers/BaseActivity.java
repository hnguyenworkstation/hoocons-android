package com.hoocons.hoocons_android.Managers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hungnguyen on 6/3/17.
 */
public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    private static BaseActivity mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
    }
}
