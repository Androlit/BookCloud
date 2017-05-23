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
import com.androlit.bookcloud.view.data.model.SignUpUserModel;
import com.androlit.bookcloud.view.utils.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpWithEmailFragment extends Fragment implements View.OnClickListener {

    private Button mBtnSignUp;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private EditText mEditTextConfirmPassword;
    private EditText mEditTextFullName;
    private SignUpUserModel mUser;

    private FirebaseAuth mAuth;

    private OnSignUpConfirmedListener onSignUpConfirmedListener;

    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View signUpView = inflater.inflate(R.layout.fragment_sign_up_with_email, container, false);
        bindViews(signUpView);
        onSignUpConfirmedListener = (OnSignUpConfirmedListener) getActivity();
        return signUpView;
    }

    private void bindViews(View signUpView) {
        mProgressDialog = new ProgressDialog(getContext());
        mBtnSignUp = (Button) signUpView.findViewById(R.id.btn_sign_up);
        mBtnSignUp.setOnClickListener(this);
        mEditTextEmail = (EditText) signUpView.findViewById(R.id.edit_text_sign_up_email);
        mEditTextPassword = (EditText) signUpView.findViewById(R.id.edit_text_password_sign_up);
        mEditTextConfirmPassword = (EditText) signUpView.findViewById(R.id.edit_text_confirm_password_sign_up);
        mEditTextFullName = (EditText) signUpView.findViewById(R.id.edit_text_sign_up_name);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_sign_up:
                createUserFromForm();
                if (validateSignUpForm()) {
                    signUpUserWithEmail();
                }
                break;
            default:
                break;
        }
    }

    private void createUserFromForm() {
        mUser = new SignUpUserModel();
        mUser.setEmail(mEditTextEmail.getText().toString());
        mUser.setFullName(mEditTextFullName.getText().toString());
        mUser.setPassword(mEditTextPassword.getText().toString());
    }

    private boolean validateSignUpForm() {
        if (!Validator.verifyEmail(mUser.getEmail())) {
            mEditTextEmail.setError("Doesn't look like an email");
            return false;
        }

        if (!Validator.verifyFullName(mUser.getFullName())) {
            mEditTextFullName.setError("Enter your full name");
            return false;
        }

        if (!Validator.verifyPassword(mUser.getPassword())) {
            mEditTextPassword.setError("Minimum 6 chars password");
            return false;
        }

        if (!Validator.matchPasswords(mUser.getPassword(),
                mEditTextConfirmPassword.getText().toString())) {
            mEditTextConfirmPassword.setError("Two passwords are not same");
            return false;
        }

        return true;
    }

    /**
     * this method will sign up user to firebase using email and password
     * and notify if user already exists with this email
     */
    private void signUpUserWithEmail() {
        showProgressDialog("Signing Up");
        mAuth.createUserWithEmailAndPassword(mUser.getEmail(), mUser.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (!task.isSuccessful()) {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // user already exist with this email
                                mEditTextEmail.setError("A User already exist with this email!");
                            }
                        } else {
                            onSignUpConfirmedListener.signUpSuccessful();
                        }
                    }
                });
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

    public interface OnSignUpConfirmedListener {
        void signUpSuccessful();
    }
}
