package com.androlit.bookcloud.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.data.model.UserConnection;
import com.androlit.bookcloud.view.adapters.MessagesListAdapter;
import com.androlit.bookcloud.view.navigator.Navigator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rubel on 8/6/2017.
 */

public class MessageFragment extends Fragment {

    MessagesListAdapter mConnectionAdapter;
    List<UserConnection> mUserConnections;
    List<String> mConnectionsUid;

    //Firebase API Clients
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private boolean trackOnline = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserConnections = new ArrayList<>();
        mConnectionsUid = new ArrayList<>();
        mConnectionAdapter = new MessagesListAdapter(mUserConnections, getContext());

        setAdapterListener();

        initFirebaseAuthAndUser();

        initFirebaseDatabaseAndStorage();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        setRecyclerViewAdapter(view);

        attachDatabaseListener();

        return view;
    }

    private void setRecyclerViewAdapter(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_fragment_chat_user);
        recyclerView.setAdapter(mConnectionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void setAdapterListener() {
        mConnectionAdapter.setMessageOnItemClickListener(new MessagesListAdapter.MessageOnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Navigator.navigateToMessage(getActivity(), mConnectionsUid.get(position));
            }
        });
    }

    private void attachDatabaseListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot lastMessageSnapshot, String s) {
                    addConnectionToRecyclerList(lastMessageSnapshot, lastMessageSnapshot.getKey());
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
    }

    private void addConnectionToRecyclerList(DataSnapshot lastMessageSnapshot, String connectionKey) {
        String message = lastMessageSnapshot.child("lastMessage").getValue().toString();
        if (!lastMessageSnapshot.child("senderId").getValue().toString()
                .equals(connectionKey))
            message = "You:" + message;

        UserConnection connection = new UserConnection(
                lastMessageSnapshot.child("name").getValue().toString(),
                lastMessageSnapshot.child("messageId").getValue().toString(),
                lastMessageSnapshot.child("senderId").getValue().toString(),
                message,
                Long.valueOf(lastMessageSnapshot.child("timestamps").getValue().toString()));

        mUserConnections.add(connection);
        mConnectionsUid.add(connectionKey);
        mConnectionAdapter.notifyDataSetChanged();
    }



    private void initFirebaseAuthAndUser() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    private void initFirebaseDatabaseAndStorage() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabaseReference = mFirebaseDatabase.getReference().child("users_connections")
                .child(mFirebaseUser.getUid());
    }

    @Override
    public void onPause() {
        super.onPause();
        mUserConnections.clear();
        mConnectionAdapter.notifyDataSetChanged();
        if (mFirebaseDatabaseReference != null)
            mFirebaseDatabaseReference.removeEventListener(mChildEventListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseDatabaseReference.addChildEventListener(mChildEventListener);
    }
}
