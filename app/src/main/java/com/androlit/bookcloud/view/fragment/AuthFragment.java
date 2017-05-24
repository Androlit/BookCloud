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


package com.androlit.bookcloud.view.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.view.listeners.AuthSuccessListener;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;


public class AuthFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static int RC_GOOGLE_AUTH = 201;

    // ui elements
    private Button signInButton;
    private Button signUpButton;
    private Button btnRegister;
    private SignInButton mBtnGoogleSignIn;
    private LoginButton mBtnFacebookLogin;
    private TextView mTvAuthTitle;
    private TextView mTvAuthMessage;
    private AuthEmailStateListener mAuthListenerActivity;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private AuthSuccessListener mAuthSuccessListener;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        initGoogleClient();
    }

    private void initGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), "recreating", Toast.LENGTH_SHORT).show();
        View loginFragment = inflater.inflate(R.layout.fragment_auth, container, false);
        bindViews(loginFragment);
        mAuthListenerActivity = (AuthEmailStateListener) getActivity();
        mAuthSuccessListener = (AuthSuccessListener) getActivity();
        return loginFragment;
    }

    private void bindViews(View authFragment) {
        signInButton = (Button) authFragment.findViewById(R.id.sign_in_with_email);
        signInButton.setOnClickListener(this);
        signUpButton = (Button) authFragment.findViewById(R.id.sign_up_with_email);
        signUpButton.setOnClickListener(this);
        btnRegister = (Button) authFragment.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        mBtnFacebookLogin = (LoginButton) authFragment.findViewById(R.id.btn_facebook_login);
        mBtnFacebookLogin.setOnClickListener(this);
        mBtnGoogleSignIn = (SignInButton) authFragment.findViewById(R.id.btn_google_login);
        mBtnGoogleSignIn.setOnClickListener(this);
        mTvAuthTitle = (TextView) authFragment.findViewById(R.id.tv_auth_title);
        mTvAuthMessage = (TextView) authFragment.findViewById(R.id.tv_auth_message);
        mProgressDialog = new ProgressDialog(getContext());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sign_in_with_email:
                mAuthListenerActivity.onSignInClicked();
                break;
            case R.id.sign_up_with_email:
                mAuthListenerActivity.onSignUpClicked();

            case R.id.btn_register:
                toggleViews();
                break;

            case R.id.btn_facebook_login:
                break;

            case R.id.btn_google_login:
                authenticateUsingGoogleAccount();
                break;

            default:
                break;
        }
    }

    private void authenticateUsingGoogleAccount() {
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(googleSignInIntent, RC_GOOGLE_AUTH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_GOOGLE_AUTH && resultCode == Activity.RESULT_OK) {
            Log.i("FRAGMENT", "GOT IT");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            signInFirebaseUsingGoogle(result.getSignInAccount());
        } else {
            Snackbar.make(btnRegister, "Google sign in failed", Snackbar.LENGTH_LONG).show();
        }
    }

    private void signInFirebaseUsingGoogle(GoogleSignInAccount googleAccount) {
        showProgressDialog("Configuring account for Book Cloud");
        AuthCredential credential = GoogleAuthProvider
                .getCredential(googleAccount.getIdToken(), null);

        // sign in firebase
        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
                if (!task.isSuccessful()) {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        // TODO: handle duplicate account
                        Snackbar.make(btnRegister, "This account conflicts with an existing account!"
                                , Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Log.i("FRAGMENT", "GOT IT 2");
                    mAuthSuccessListener.onAuthSuccess();
                }
            }
        });
    }

    private void toggleViews() {
        if (signInButton.getVisibility() != View.GONE) {
            signInButton.setVisibility(View.GONE);
            signUpButton.setVisibility(View.VISIBLE);
        } else {
            signUpButton.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void showProgressDialog(String msg) {
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public interface AuthEmailStateListener {
        void onSignInClicked();

        void onSignUpClicked();
    }
}
