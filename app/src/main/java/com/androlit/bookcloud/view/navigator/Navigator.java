package com.androlit.bookcloud.view.navigator;

import android.content.Context;
import android.content.Intent;

import com.androlit.bookcloud.view.activity.AuthenticationActivity;

/**
 * Created by ${farhanarnob} on ${06-Oct-16}.
 * It for navigating activity.
 */

public final class Navigator {

    /**
     * this method will navigate to AuthenticationActivity
     *
     * @param context is for setting context
     */
    public static void navigateToLogin(Context context) {
        Intent intent = AuthenticationActivity.getCallingIntent(context);
        context.startActivity(intent);
    }
}
