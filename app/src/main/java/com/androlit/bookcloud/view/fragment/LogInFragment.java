package com.androlit.bookcloud.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androlit.bookcloud.R;

/**
 * Created by ${farhanarnob} on ${06-Oct-16}.
 * It is for Login option.
 */

public class LogInFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, true);
    }
}
