package com.androlit.bookcloud.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.view.fragment.SignInFragment;
import com.androlit.bookcloud.view.fragment.SignUpFragment;

import butterknife.ButterKnife;

/**
 * Created by ${farhanarnob} on ${06-Oct-16}.
 * It is for authentication activity.
 */

public class AuthenticationActivity extends AppCompatActivity implements SignInFragment.OnRegisterClickListener {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        addLoginFragment();
    }

    private void addLoginFragment() {
        SignInFragment signInFragment = new SignInFragment();
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, signInFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onClickRegister() {
        replaceSignInFragmentWithSignUpFragment();
    }

    private void replaceSignInFragmentWithSignUpFragment() {
        SignUpFragment signUpFragment = new SignUpFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            fragmentTransaction.add(R.id.fragment_container, signUpFragment);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, signUpFragment);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}