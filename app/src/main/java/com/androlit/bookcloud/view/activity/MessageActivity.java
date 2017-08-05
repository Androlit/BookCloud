package com.androlit.bookcloud.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.data.model.Message;
import com.androlit.bookcloud.data.model.UserConnection;
import com.androlit.bookcloud.utils.AppConstants;
import com.androlit.bookcloud.view.adapters.MessageActivityListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

/**
 * Created by rubel on 4/17/2017.
 */

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;
    ImageButton mImageButtonEmoji;
    ImageButton mImageButtonSend;
    EmojiconEditText mEditTextMessage;
    EmojIconActions mEmojIcon;
    View mRootView;

    MessageActivityListAdapter mMessageListAdapter;
    List<Message> mMessageList;

    FirebaseUser mFirebaseUser;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mSenderMessageReference;
    DatabaseReference mReceiverMessageReference;
    DatabaseReference mGlobalMessageReference;
    ChildEventListener mMessaseChildEventListener;

    String messageId;
    private String mUserReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        initReceiverUser();

        initViews();

        setRecyclerAdapter();

        initFirebaseDatabase();

        initMessageChildListener();
    }

    private void initMessageChildListener() {
        mMessaseChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    Message message = dataSnapshot.getValue(Message.class);
                    boolean isLeft = dataSnapshot.child("sender").getValue().toString().equals(mFirebaseUser.getUid());
                    message.setLeft(isLeft);
                    mMessageList.add(message);
                    mMessageListAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(mMessageListAdapter.getItemCount());
                }
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

    private void initFirebaseDatabase() {
        messageId = getMessageId();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSenderMessageReference = mFirebaseDatabase.getReference().child("users_connections")
                .child(mFirebaseUser.getUid());
        mReceiverMessageReference = mFirebaseDatabase.getReference().child("users_connections")
                .child(mUserReceiver);
        mGlobalMessageReference = mFirebaseDatabase.getReference().child("messages")
                .child(getMessageId());
    }

    public String getMessageId() {
        String first = mFirebaseUser.getUid();
        String second = mUserReceiver;
        int comp = first.compareTo(second);
        if (comp < 0) return first + ";" + second;
        return second + ";" + first;
    }

    private void setRecyclerAdapter() {
        mMessageList = new ArrayList<>();
        mMessageListAdapter = new MessageActivityListAdapter(mMessageList, this);
        mRecyclerView.setAdapter(mMessageListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        mRootView = findViewById(R.id.root_layout_chat);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat);
        mEditTextMessage = (EmojiconEditText) findViewById(R.id.edit_text_chat_content);
        mImageButtonEmoji = (ImageButton) findViewById(R.id.image_button_chat_emoicon);
        mImageButtonSend = (ImageButton) findViewById(R.id.image_button_chat_send);
        mImageButtonSend.setOnClickListener(this);
        mEmojIcon = new EmojIconActions(this, mRootView, mEditTextMessage, mImageButtonEmoji);
        mEmojIcon.setIconsIds(R.drawable.keyboard, R.drawable.emoticon);
        mEmojIcon.ShowEmojIcon();
    }

    private void initReceiverUser() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUserReceiver = extras.getString(AppConstants.CHAT_ACTIVITY_RECEIVER);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.image_button_chat_send) {
            String content = mEditTextMessage.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                return;
            }

            long time = System.currentTimeMillis();
            Message message = new Message(content, mFirebaseUser.getUid(), mUserReceiver, time);
            UserConnection senderConnection = new UserConnection(mFirebaseUser.getDisplayName(), messageId,
                    mFirebaseUser.getUid(), content, time);

            mGlobalMessageReference.push().setValue(message);

            mSenderMessageReference.child(mUserReceiver).child("lastMessage").setValue(content);
            mSenderMessageReference.child(mUserReceiver).child("timestamps").setValue(time);
            mSenderMessageReference.child(mUserReceiver).child("senderId").setValue(mFirebaseUser.getUid());
            mReceiverMessageReference.child(mFirebaseUser.getUid()).setValue(senderConnection);

            mEditTextMessage.setText(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGlobalMessageReference.addChildEventListener(mMessaseChildEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGlobalMessageReference != null)
            mGlobalMessageReference.removeEventListener(mMessaseChildEventListener);
        mMessageList.clear();
        mMessageListAdapter.notifyDataSetChanged();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MessageActivity.class);
    }
}
