package com.androlit.bookcloud.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androlit.bookcloud.R;

/**
 * Created by ${farhanarnob} on ${06-Oct-16}.
 */

public class SignUpWithEmailFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialogue_sign_up_with_email, container, false);
    }
}
