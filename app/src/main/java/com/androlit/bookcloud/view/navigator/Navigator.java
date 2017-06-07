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


package com.androlit.bookcloud.view.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.androlit.bookcloud.view.activity.AddBookActivity;
import com.androlit.bookcloud.view.activity.AuthenticationActivity;


public final class Navigator {

    public static int AUTH_REQUEST = 201;
    /**
     * this method will navigate to AuthenticationActivity
     *
     * @param context is for setting context
     */
    public static void navigateToAuth(Context context) {
        Intent intent = AuthenticationActivity.getCallingIntent(context);
        ((Activity) context).startActivityForResult(intent, AUTH_REQUEST);
    }


    /**
     * this method will navigate to AddBookActivity
     *
     * @param context is for setting context
     */
    public static void navigateToAddBook(Context context) {
        Intent intent = AddBookActivity.getCallingIntent(context);
        context.startActivity(intent);
    }
}
