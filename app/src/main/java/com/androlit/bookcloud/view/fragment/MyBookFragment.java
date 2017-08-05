package com.androlit.bookcloud.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.data.model.FirebaseBook;
import com.androlit.bookcloud.view.adapters.BookListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by rubel on 7/30/2017.
 */

public class MyBookFragment extends Fragment{

    RecyclerView.LayoutManager layoutManager;
    BookListAdapter mBookListAdapter;
    ArrayList<FirebaseBook> mFirebaseBooks;
    private RecyclerView recyclerView;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mBooksDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ProgressDialog mProgressDialog;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View availableBookListView = inflater.inflate(R.layout.fragment_list_of_avaiable_books, container, false);

        // initialize firebase component
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mBooksDatabaseReference = mFirebaseDatabase.getReference()
                .child("user_books")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // add book list
        mFirebaseBooks = new ArrayList<>();

        mProgressDialog = new ProgressDialog(getContext());
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
        mBookListAdapter.onAttachedToRecyclerView(recyclerView);
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBookListAdapter.clear();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mBooksDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }

    }

    private void attachDatabaseReadListener() {
        showProgressDialog("Your Books");
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    hideProgressDialog();
                    FirebaseBook firebaseBook = dataSnapshot.getValue(FirebaseBook.class);
                    mBookListAdapter.add(firebaseBook);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }

        if(mBooksDatabaseReference != null)
            mBooksDatabaseReference.addChildEventListener(mChildEventListener);

    }

    private void showProgressDialog(String msg) {
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
