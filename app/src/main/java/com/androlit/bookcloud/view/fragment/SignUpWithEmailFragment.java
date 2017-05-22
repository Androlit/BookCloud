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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.view.data.model.SignUpUserModel;

import java.util.regex.Pattern;

public class SignUpWithEmailFragment extends Fragment implements View.OnClickListener {

    private Button mBtnSignUp;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private EditText mEditTextConfirmPassword;
    private EditText mEditTextFullName;
    private SignUpUserModel mUser;

    private OnSignUpConfirmedListener onSignUpConfirmedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View signUpView = inflater.inflate(R.layout.fragment_sign_up_with_email, container, false);
        bindViews(signUpView);
        onSignUpConfirmedListener = (OnSignUpConfirmedListener) getActivity();
        return signUpView;
    }

    private void bindViews(View signUpView) {
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
                    // TODO: Sign up user in this fragment
                    // then call activity's listener
                    onSignUpConfirmedListener.signUpSuccessful();
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
        if (!verifyEmail(mUser.getEmail())) {
            mEditTextEmail.setError("Doesn't look like an email");
            return false;
        }

        if (!verifyFullName(mUser.getFullName())) {
            return false;
        }

        return verifyPassword(mUser.getPassword());

    }

    private boolean verifyEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).find();
    }

    private boolean verifyFullName(String name) {
        if (TextUtils.isEmpty(name)) {
            mEditTextFullName.setError("Write your full name");
            return false;
        }

        return true;
    }

    private boolean verifyPassword(String password) {
        if (!password.equals(mEditTextConfirmPassword.getText().toString())) {
            mEditTextConfirmPassword.setError("Given password doesn't match");
            return false;
        }

        if (password.length() < 6) {
            mEditTextPassword.setError("Minimum 6 chars password");
            return false;
        }

        return true;
    }

    public interface OnSignUpConfirmedListener {
        void signUpSuccessful();
    }
}
