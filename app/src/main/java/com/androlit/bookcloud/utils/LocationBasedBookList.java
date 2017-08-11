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

package com.androlit.bookcloud.utils;

import android.location.Location;

import com.androlit.bookcloud.data.model.FirebaseBook;
import com.androlit.bookcloud.data.model.LocationBook;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class LocationBasedBookList {

    private PriorityQueue<LocationBook> priorityQueue;
    private Location mCurrentLocation;

    public LocationBasedBookList(Location location) {
        this.priorityQueue = new PriorityQueue<>(10, new LocationComparator());
        this.mCurrentLocation = location;
    }

    public void addBook(FirebaseBook book) {
        Location loc = new Gson().fromJson(book.getLocationJson(), Location.class);
        priorityQueue.add(new LocationBook(mCurrentLocation.distanceTo(loc), book));
    }

    public List<FirebaseBook> getAllBooks() {
        List<FirebaseBook> books = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            LocationBook book = priorityQueue.remove();
            Integer dis = Math.round(book.getDistance() / 1000);
            book.getBook().setLocationName(dis.toString() + " km, " +
                    "" + book.getBook().getLocationName());
            books.add(book.getBook());
        }

        return books;
    }
}
