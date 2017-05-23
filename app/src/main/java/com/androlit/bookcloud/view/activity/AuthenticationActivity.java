/*
 * Copyright (C) 2017 Book Cloud
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androlit.bookcloud.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.view.fragment.AuthFragment;
import com.androlit.bookcloud.view.fragment.SignInWithEmailFragment;
import com.androlit.bookcloud.view.fragment.SignUpWithEmailFragment;

import butterknife.ButterKnife;

public class AuthenticationActivity extends AppCompatActivity implements
        AuthFragment.mAuthListenerActivity,
        SignUpWithEmailFragment.OnSignUpConfirmedListener {

    private static int AUTH_FRAGMENT = 101;
    private static int SIGN_IN_FRAGMENT = 201;
    private static int SIGN_UP_FRAGMENT = 301;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        addFragment(AUTH_FRAGMENT);
    }

    private void addFragment(int fragId) {
        Fragment fragment = getDesiredFragment(fragId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            fragmentTransaction.add(R.id.fragment_container, fragment);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    private Fragment getDesiredFragment(int fragId) {
        if (fragId == AUTH_FRAGMENT) {
            return new AuthFragment();
        } else if (fragId == SIGN_IN_FRAGMENT) {
            return new SignInWithEmailFragment();
        } else if (fragId == SIGN_UP_FRAGMENT) {
            return new SignUpWithEmailFragment();
        } else {
            throw new NullPointerException("fragment not found in auth activity");
        }
    }

    @Override
    public void onSignInClicked() {
        addFragment(SIGN_IN_FRAGMENT);
    }

    @Override
    public void onSignUpClicked() {
        addFragment(SIGN_UP_FRAGMENT);
    }


    @Override
    public void signUpSuccessful() {
        // if need to return data pass using intent
        setResult(Activity.RESULT_OK);
        finish();
    }
}
