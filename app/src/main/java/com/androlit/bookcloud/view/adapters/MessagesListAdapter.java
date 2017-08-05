package com.androlit.bookcloud.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.data.model.UserConnection;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by rubel on 8/6/2017.
 */

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.ChatsViewHolder> {

    List<UserConnection> mUserConnections = null;
    Context mContext;

    private MessageOnItemClickListener listener;

    public MessagesListAdapter(List<UserConnection> mUserConnections, Context mContext) {
        this.mUserConnections = mUserConnections;
        this.mContext = mContext;
    }

    // method to set ChatItemClickListener to this adapter from activity/fragment using it
    public void setMessageOnItemClickListener(MessageOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ChatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View currentView = LayoutInflater.from(mContext).inflate(R.layout.message_list_item, parent, false);
        return new ChatsViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(ChatsViewHolder holder, int position) {
        UserConnection connection = mUserConnections.get(position);
        holder.getIvProfile().setImageResource(R.drawable.user);
        holder.getTvSenderName().setText(connection.getName());
        holder.getTvLastMessage().setText(connection.getLastMessage());
        holder.getTvTimestamps().setText(getDate(connection.getTimestamps()));
    }

    private String getDate(Long timestamps) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamps);
        return android.text.format.DateFormat.format("EEE, MMM yy",cal).toString();
    }

    @Override
    public int getItemCount() {
        return mUserConnections.size();
    }

    public UserConnection getItem(int position) {
        return mUserConnections.get(position);
    }

    // chat item click listener interface
    public interface MessageOnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public class ChatsViewHolder extends RecyclerView.ViewHolder {
        TextView tvSenderName;
        TextView tvLastMessage;
        TextView tvTimestamps;
        ImageView ivProfile;

        public ChatsViewHolder(final View view) {
            super(view);
            this.tvSenderName = (TextView) view.findViewById(R.id.text_view_chat_item_name);
            this.tvLastMessage = (TextView) view.findViewById(R.id.text_view_chat_item_message);
            this.tvTimestamps = (TextView) view.findViewById(R.id.text_view_chat_item_time);
            this.ivProfile = (ImageView) view.findViewById(R.id.image_view_chat_item_thumb);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }

        public TextView getTvSenderName() {
            return tvSenderName;
        }

        public void setTvSenderName(TextView tvSenderName) {
            this.tvSenderName = tvSenderName;
        }

        public TextView getTvLastMessage() {
            return tvLastMessage;
        }

        public void setTvLastMessage(TextView tvLastMesage) {
            this.tvLastMessage = tvLastMesage;
        }

        public TextView getTvTimestamps() {
            return tvTimestamps;
        }

        public void setTvTimestamps(TextView tvTimestamps) {
            this.tvTimestamps = tvTimestamps;
        }

        public ImageView getIvProfile() {
            return ivProfile;
        }

        public void setIvProfile(ImageView ivProfile) {
            this.ivProfile = ivProfile;
        }
    }
}
