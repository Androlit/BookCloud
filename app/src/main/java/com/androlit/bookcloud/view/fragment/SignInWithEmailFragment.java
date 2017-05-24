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

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.data.model.AuthUserModel;
import com.androlit.bookcloud.utils.Validator;
import com.androlit.bookcloud.view.listeners.AuthSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class SignInWithEmailFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private AuthUserModel mUser;

    private Button mBtnSignIn;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private ProgressDialog mProgressDialog;

    private AuthSuccessListener mSignInConfirmedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View signInView = inflater.inflate(R.layout.fragment_sign_in_wtih_email, container, false);
        bindViews(signInView);
        mSignInConfirmedListener = (AuthSuccessListener) getActivity();
        return signInView;
    }

    private void bindViews(View signInView) {
        mBtnSignIn = (Button) signInView.findViewById(R.id.btn_sign_in_with_email);
        mBtnSignIn.setOnClickListener(this);
        mEditTextEmail = (EditText) signInView.findViewById(R.id.edit_text_email_sign_in);
        mEditTextPassword = (EditText) signInView.findViewById(R.id.edit_text_password_sign_in);
        mProgressDialog = new ProgressDialog(getContext());
    }


    private void createUserFromForm() {
        mUser = new AuthUserModel();
        mUser.setEmail(mEditTextEmail.getText().toString());
        mUser.setFullName(null);
        mUser.setPassword(mEditTextPassword.getText().toString());
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_sign_in_with_email:
                createUserFromForm();
                if (verifyUserForSignIn()) {
                    signInUser();
                }
                break;
            default:
                break;
        }
    }

    private void signInUser() {
        showProgressDialog("Signing in");
        mAuth.signInWithEmailAndPassword(mUser.getEmail(), mUser.getPassword())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (!task.isSuccessful()) {
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                // no user exist with this email , offer user to sign up
                                mEditTextEmail.setError("This email is not registered");
                            }
                        } else {
                            mSignInConfirmedListener.onAuthSuccess();
                        }
                    }
                });
    }

    private boolean verifyUserForSignIn() {

        if (!Validator.verifyEmail(mUser.getEmail())) {
            mEditTextEmail.setError("Enter a valid email");
            return false;
        }

        if (!Validator.verifyPassword(mUser.getPassword())) {
            mEditTextPassword.setError("Minimum 6 chars password");
            return false;
        }

        return true;
    }
}
