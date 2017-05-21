package com.androlit.bookcloud.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androlit.bookcloud.R;

/**
 * Created by ${farhanarnob} on ${06-Oct-16}.
 * this fragment for sign up
 */

public class SignUpFragment extends Fragment implements View.OnClickListener {


    private static final String SIGN_UP_DIALOGUE = "SIGN_UP_DIALOGUE";
    private FragmentManager mFragmentManager;
    private SignUpDialogueFragment mSignUpDialoguFragment;
    private Button btnSignUp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View signUpView = inflater.inflate(R.layout.fragment_signup, container, false);
        bindView(signUpView);
        return signUpView;
    }

    private void bindView(View signUpView) {
        btnSignUp = (Button) signUpView.findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_sign_up:
                showSignUpDialogue();
                break;
            default:
                break;
        }
    }

    private void showSignUpDialogue() {
        mSignUpDialoguFragment = new SignUpDialogueFragment();
        mFragmentManager = getActivity().getSupportFragmentManager();
        mSignUpDialoguFragment.show(mFragmentManager, SIGN_UP_DIALOGUE);
    }
}
