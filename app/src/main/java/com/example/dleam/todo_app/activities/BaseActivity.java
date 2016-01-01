package com.example.dleam.todo_app.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.dleam.todo_app.R;

/**
 * Created by DevinL on 12/31/2015.
 */
public class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    // Activate primary Toolbar
    protected Toolbar activateToolbar() {
        if(mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.app_bar);
            if(mToolbar != null) {
                mToolbar.setTitle("TuDu");
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }
}
