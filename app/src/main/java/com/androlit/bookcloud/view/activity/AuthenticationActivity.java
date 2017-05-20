package com.androlit.bookcloud.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.view.fragment.SignInFragment;
import com.androlit.bookcloud.view.fragment.SignUpFragment;

import butterknife.ButterKnife;

/**
 * Created by ${farhanarnob} on ${06-Oct-16}.
 * It is for authentication activity.
 */

public class AuthenticationActivity extends AppCompatActivity implements SignInFragment.OnRegisterClickListener {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private SignUpFragment mSignUpFragment;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    public void onClickRegister() {
        Toast.makeText(this, "main activity", Toast.LENGTH_SHORT).show();
        replaceSignInFragmentWithSignUpFragment();
    }

    private void replaceSignInFragmentWithSignUpFragment() {
        mSignUpFragment = new SignUpFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_login, mSignUpFragment);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }
}
