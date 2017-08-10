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

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androlit.bookcloud.R;
import com.androlit.bookcloud.data.model.FirebaseBook;
import com.androlit.bookcloud.data.model.Message;
import com.androlit.bookcloud.data.model.UserConnection;
import com.androlit.bookcloud.view.adapters.BookListAdapter;
import com.androlit.bookcloud.view.listeners.RecycleViewScrollViewListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AvailableBookListFragment extends Fragment implements
        BookListAdapter.BookItemClickListener {
    RecyclerView.LayoutManager layoutManager;
    BookListAdapter mBookListAdapter;
    ArrayList<FirebaseBook> mFirebaseBooks;
    FirebaseUser mFirebaseUser;
    DatabaseReference mSenderMessageReference;
    DatabaseReference mReceiverMessageReference;
    DatabaseReference mGlobalMessageReference;
    ChildEventListener mMessaseChildEventListener;
    RecycleViewScrollViewListener mRecycleViewScrollViewListener;
    private RecyclerView recyclerView;
    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mBooksDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View availableBookListView = inflater.inflate(R.layout.fragment_list_of_avaiable_books, container, false);

        mRecycleViewScrollViewListener = (RecycleViewScrollViewListener) getActivity();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mBooksDatabaseReference = mFirebaseDatabase.getReference().child("books");

        // add book list
        mFirebaseBooks = new ArrayList<>();

        mProgressDialog = new ProgressDialog(getContext());
        recyclerView = (RecyclerView) availableBookListView.findViewById(R.id.home_pager_recycle_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mBookListAdapter = new BookListAdapter(mFirebaseBooks, getContext());
        mBookListAdapter.setBookItemClickListener(this);
        recyclerView.setAdapter(mBookListAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mRecycleViewScrollViewListener.onScrolling(dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return availableBookListView;
    }

    private void initFirebaseComponents(String receiverId, String messageId) {
        mSenderMessageReference = mFirebaseDatabase.getReference().child("users_connections")
                .child(mFirebaseUser.getUid());
        mReceiverMessageReference = mFirebaseDatabase.getReference().child("users_connections")
                .child(receiverId);
        mGlobalMessageReference = mFirebaseDatabase.getReference().child("messages")
                .child(messageId);
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressDialog("Books loading");
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

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    hideProgressDialog();
                    FirebaseBook firebaseBook = dataSnapshot.getValue(FirebaseBook.class);
                    mFirebaseBooks.add(firebaseBook);
                    mBookListAdapter.notifyDataSetChanged();
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

    @Override
    public void onItemClick(View itemView, int position) {
        FirebaseBook book = mFirebaseBooks.get(position);

        if(book == null) return;

        if(book.getUserId().equals(mFirebaseUser.getUid())){
            showInvalidRequestDialog();
            return;
        }

        String messageId =getMessageId(book.getUserId());
        String name = mFirebaseUser.getDisplayName() == null ? "unknown" : mFirebaseUser.getDisplayName();
        String content = "hi! , I want to buy " + book.getTitle() + " that you have offered to BookCloud";
        long time = System.currentTimeMillis();

        initFirebaseComponents(book.getUserId(), messageId);

        Message message = new Message(content, mFirebaseUser.getUid(), book.getUserId(), time);

        UserConnection reciever = new UserConnection("Unknown", messageId, mFirebaseUser.getUid(), content,
                time);

        UserConnection sender = new UserConnection(name, messageId, mFirebaseUser.getUid(), content,
                time);

        mGlobalMessageReference.push().setValue(message);
        mSenderMessageReference.child(book.getUserId()).setValue(reciever);
        mReceiverMessageReference.child(mFirebaseUser.getUid()).setValue(sender);

        new MaterialDialog.Builder(getContext())
                .title("Message Sent to Book Owner")
                .positiveText("Ok")
                .show();

    }

    private void showInvalidRequestDialog() {
        new MaterialDialog.Builder(getContext())
                .title("Invalid Request")
                .content("This book is offered by you, so you can not by this.")
                .positiveText("Ok")
                .show();
    }

    public String getMessageId(String second) {
        String first = mFirebaseUser.getUid();
        int comp = first.compareTo(second);
        if (comp < 0) return first + ";" + second;
        return second + ";" + first;
    }

}
