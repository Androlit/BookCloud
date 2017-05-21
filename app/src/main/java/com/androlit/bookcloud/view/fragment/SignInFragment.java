/*
 * Copyright (C) 2017 The Android Open Source Project
 *
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

package com.androlit.bookcloud.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androlit.bookcloud.R;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;


public class SignInFragment extends Fragment implements View.OnClickListener {

    private static final String DIALOGUE = "LOGIN_DIALOGUE";
    private SignInDialogueFragment mSignInDialogueFragment;
    private FragmentManager mFragmentManager;

    // ui elements
    private Button signInButton;
    private Button btnRegister;
    private SignInButton mBtnGoogleSignIn;
    private LoginButton mBtnFacebookLogin;
    private TextView mTvAuthTitle;
    private TextView mTvAuthMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), "recreating", Toast.LENGTH_SHORT).show();
        View loginFragment = inflater.inflate(R.layout.fragment_sign_in, container, false);
        bindViews(loginFragment);
        createSignInDialogue();
        return loginFragment;
    }

    private void createSignInDialogue() {
        mSignInDialogueFragment = new SignInDialogueFragment();
        mFragmentManager = getActivity().getSupportFragmentManager();
    }

    private void bindViews(View loginFragment) {
        signInButton = (Button) loginFragment.findViewById(R.id.sign_in_with_email);
        signInButton.setOnClickListener(this);
        btnRegister = (Button) loginFragment.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        mBtnFacebookLogin = (LoginButton) loginFragment.findViewById(R.id.btn_facebook_login);
        mBtnFacebookLogin.setOnClickListener(this);
        mBtnGoogleSignIn = (SignInButton) loginFragment.findViewById(R.id.btn_google_login);
        mBtnGoogleSignIn.setOnClickListener(this);
        mTvAuthTitle = (TextView) loginFragment.findViewById(R.id.tv_auth_title);
        mTvAuthMessage = (TextView) loginFragment.findViewById(R.id.tv_auth_message);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sign_in_with_email:
                mSignInDialogueFragment.show(mFragmentManager, DIALOGUE);
                break;

            case R.id.btn_register:
                toggleViews();
                break;

            case R.id.btn_facebook_login:
                break;

            case R.id.btn_google_login:
                break;

            default:
                break;
        }
    }

    private void toggleViews() {

        if(mTvAuthTitle.getText().toString().toLowerCase().equals(
                getString(R.string.sign_in_to_book_cloud).toLowerCase())){
            mTvAuthTitle.setText(getString(R.string.sign_up_for_book_cloud));
        }else{
            mTvAuthTitle.setText(getString(R.string.sign_in_to_book_cloud));
        }

        if(mBtnFacebookLogin.getText().toString().toLowerCase().equals(
                getString(R.string.login_with_facebook).toLowerCase())){
            mBtnFacebookLogin.setText(getString(R.string.sign_up_with_facebook));
        }else{
            mBtnFacebookLogin.setText(getString(R.string.login_with_facebook));
        }

        if(mTvAuthMessage.getText().toString().toLowerCase().equals(
                getString(R.string.don_t_have_an_account).toLowerCase())){
            mTvAuthMessage.setText(getString(R.string.already_have_an_account));
        }else{
            mTvAuthMessage.setText(getString(R.string.don_t_have_an_account));
        }

        if(btnRegister.getText().toString().toLowerCase().equals(
                getString(R.string.register_here).toLowerCase())){
            btnRegister.setText(getString(R.string.login_here));
        }else{
            btnRegister.setText(getString(R.string.register_here));
        }

        if(signInButton.getText().toString().toLowerCase().equals(
                getString(R.string.sign_in).toLowerCase())){
            signInButton.setText(getString(R.string.sign_up));
        }else{
            signInButton.setText(getString(R.string.sign_in));
        }

    }
}
