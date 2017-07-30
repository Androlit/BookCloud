package com.androlit.bookcloud.view.fragment;

import android.app.ProgressDialog;
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
import com.androlit.bookcloud.view.adapters.BookListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View availableBookListView = inflater.inflate(R.layout.fragment_list_of_avaiable_books, container, false);

        // add book list
        mFirebaseBooks = new ArrayList<>();

        recyclerView = (RecyclerView) availableBookListView.findViewById(R.id.home_pager_recycle_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mBookListAdapter = new BookListAdapter(mFirebaseBooks, getContext());
        recyclerView.setAdapter(mBookListAdapter);
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
                .orderByChild("title")
                .startAt(this.query); // equalTo()  , will make this exact search

        if(fireQuery!=null){
            fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            FirebaseBook book = snapshot.getValue(FirebaseBook.class);
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
