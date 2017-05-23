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

package com.androlit.bookcloud.view.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

public final class Validator {

    public static boolean verifyEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).find();
    }

    public static boolean verifyFullName(String name) {
        return !TextUtils.isEmpty(name);

    }

    public static boolean verifyPassword(String password) {
        return password.length() >= 6;

    }

    public static boolean matchPasswords(String password, String confirmPassword) {
        return password.equals(confirmPassword);

    }
}
