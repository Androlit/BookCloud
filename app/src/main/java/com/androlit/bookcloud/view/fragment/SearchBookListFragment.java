package com.androlit.bookcloud.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.androlit.bookcloud.data.model.LocationBook;
import com.androlit.bookcloud.utils.LocationComparator;
import com.androlit.bookcloud.view.adapters.BookListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by rubel on 7/30/2017.
 */

public class SearchBookListFragment extends Fragment {
    RecyclerView.LayoutManager layoutManager;
    BookListAdapter mBookListAdapter;
    ArrayList<FirebaseBook> mFirebaseBooks;
    private RecyclerView recyclerView;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;

    String query = null;

    PriorityQueue<LocationBook> priorityQueue;
    Location mCurrentLocation;

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

        // add book list
        mFirebaseBooks = new ArrayList<>();

        priorityQueue = new PriorityQueue<>(10, new LocationComparator());

        recyclerView = (RecyclerView) availableBookListView.findViewById(R.id.home_pager_recycle_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mBookListAdapter = new BookListAdapter(mFirebaseBooks, getContext());
        recyclerView.setAdapter(mBookListAdapter);

        setCurrentLocation();


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
                            Location loc = new Gson().fromJson(book.getLocationJson(), Location.class);
                            priorityQueue.add(new LocationBook(mCurrentLocation.distanceTo(loc), book));
                            //mBookListAdapter.add(book);
                        }


                        while(!priorityQueue.isEmpty()){
                            LocationBook book = priorityQueue.remove();
                            Log.d("BOOK:" , book.getDistance().toString());
                            Integer dis = Math.round(book.getDistance()/1000);
                            book.getBook().setLocationName(dis.toString() + " km, " +
                                    "" + book.getBook().getLocationName());
                            mBookListAdapter.add(book.getBook());

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
