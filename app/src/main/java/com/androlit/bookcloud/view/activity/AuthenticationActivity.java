package com.androlit.bookcloud.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.androlit.bookcloud.R;

import butterknife.ButterKnife;

/**
 * Created by ${farhanarnob} on ${06-Oct-16}.
 * It is for authentication activity.
 */

public class AuthenticationActivity extends AppCompatActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }
}
