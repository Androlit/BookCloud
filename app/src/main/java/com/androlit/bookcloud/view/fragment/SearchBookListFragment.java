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

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.data.model.FirebaseBook;
import com.androlit.bookcloud.utils.LocationBasedBookList;
import com.androlit.bookcloud.view.adapters.BookListAdapter;
import com.androlit.bookcloud.view.listeners.RecycleViewScrollViewListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SearchBookListFragment extends Fragment {
    RecyclerView.LayoutManager layoutManager;
    BookListAdapter mBookListAdapter;
    ArrayList<FirebaseBook> mFirebaseBooks;
    RecycleViewScrollViewListener mRecycleViewScrollViewListener;
    String query = null;
    LocationBasedBookList locationBasedBookList;
    Location mCurrentLocation;
    private RecyclerView recyclerView;
    // Firebase
    private FirebaseDatabase mFirebaseDatabase;

    private void setCurrentLocation() {
        SharedPreferences preferences = getActivity().getSharedPreferences("com.androlit.bookcloud",
                Context.MODE_PRIVATE);
        String json = preferences.getString("location", "");
        Location location = new Gson().fromJson(json, Location.class);

        if(location == null){
            mCurrentLocation = new Location("");
            mCurrentLocation.setLatitude(23.793993);
            mCurrentLocation.setLongitude(90.404272);
        }else{
            mCurrentLocation = location;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View availableBookListView = inflater.inflate(R.layout.fragment_list_of_avaiable_books, container, false);

        // This is for giving acknowledgment to the activity that recycle view is scrolling
        mRecycleViewScrollViewListener = (RecycleViewScrollViewListener) getActivity();

        // add book list
        mFirebaseBooks = new ArrayList<>();


        recyclerView = (RecyclerView) availableBookListView.findViewById(R.id.home_pager_recycle_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mBookListAdapter = new BookListAdapter(mFirebaseBooks, getContext());
        recyclerView.setAdapter(mBookListAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mRecycleViewScrollViewListener.onScrolling(dy);
            }
        });

        setCurrentLocation();

        locationBasedBookList = new LocationBasedBookList(mCurrentLocation);

        return availableBookListView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(this.query != null){
            searchAndUpdate();
        }

    }

    public void searchAndUpdate() {
        if(mFirebaseDatabase == null){
            mFirebaseDatabase = FirebaseDatabase.getInstance();
        }

        Query fireQuery = mFirebaseDatabase.getReference().child("books")
                .orderByChild("titleLowerCase")
                .startAt(this.query); // equalTo()  , will make this exact search

        if(fireQuery!=null){
            fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            FirebaseBook book = snapshot.getValue(FirebaseBook.class);
                            locationBasedBookList.addBook(book);
                        }

                        List<FirebaseBook> books = locationBasedBookList.getAllBooks();
                        for (FirebaseBook book : books) {
                            mBookListAdapter.add(book);
                        }

                    } else {
                        // TODO: create a dialog that no results found
                        // give user options to go home
                        Log.d("SEARCH:", "NO Results Found.");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    public void setQuery(String query) {
        this.query = query;
    }

}
