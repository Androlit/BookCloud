package com.androlit.bookcloud.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.androlit.bookcloud.R;

/**
 * Created by ${farhanarnob} on ${06-Oct-16}.
 * It is for Login option.
 */

public class SignInFragment extends Fragment implements View.OnClickListener {

    private static final String DIALOGUE = "LOGIN_DIALOGUE";
    private SignInDialogueFragment mSignInDialogueFragment;
    private FragmentManager mFragmentManager;
    private Button signInButton;
    private Button btnRegister;

    private OnRegisterClickListener mListenerActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), "recreating", Toast.LENGTH_SHORT).show();
        View loginFragment = inflater.inflate(R.layout.fragment_sign_in, container, false);
        bindViews(loginFragment);
        createSignInDialogue();
        mListenerActivity = (OnRegisterClickListener) getActivity();
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
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sign_in_with_email:
                mSignInDialogueFragment.show(mFragmentManager, DIALOGUE);
                break;

            case R.id.btn_register:
                mListenerActivity.onClickRegister();
                break;

            default:
                break;
        }
    }

    public interface OnRegisterClickListener {
        void onClickRegister();
    }
}
