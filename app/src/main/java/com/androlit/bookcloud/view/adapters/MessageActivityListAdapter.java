package com.androlit.bookcloud.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.data.model.Message;

import java.util.List;

/**
 * Created by rubel on 8/6/2017.
 */

public class MessageActivityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TEXT_LEFT = 101, TEXT_RIGHT = 102;
    private List<Message> mMessagesList;
    private Context mContext;

    public MessageActivityListAdapter(List<Message> mMessagesList, Context mContext) {
        this.mMessagesList = mMessagesList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case TEXT_LEFT: {
                View viewLeft = LayoutInflater.from(mContext).inflate(R.layout.message_item_left,
                        parent, false);
                viewHolder = new TextViewHolder(viewLeft);
                break;
            }
            case TEXT_RIGHT: {
                View viewRight = LayoutInflater.from(mContext).inflate(R.layout.message_item_right,
                        parent, false);
                viewHolder = new TextViewHolder(viewRight);
                break;
            }
            default: {
                View viewDefault = LayoutInflater.from(mContext).inflate(R.layout.message_item_left,
                        parent, false);
                viewHolder = new TextViewHolder(viewDefault);
                break;
            }
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TEXT_RIGHT: {
                TextViewHolder rightTextViewHolder = (TextViewHolder) holder;
                configureRightTextViewHolder(rightTextViewHolder, position);
                break;
            }
            case TEXT_LEFT:
            default: {
                TextViewHolder leftTextViewHolder = (TextViewHolder) holder;
                configureLeftTextViewHolder(leftTextViewHolder, position);
                break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    @Override
    public int getItemViewType(int position) {

        Message msg = mMessagesList.get(position);

        if (msg.isLeft())
            return TEXT_LEFT;

        return TEXT_RIGHT;
    }

    private void configureLeftTextViewHolder(TextViewHolder textViewHolder, int position) {
        Message msg = mMessagesList.get(position);
        textViewHolder.getTvMessage().setText(msg.getContent());
        textViewHolder.getIvProfileIcon().setImageResource(R.drawable.arrow_up);
    }

    private void configureRightTextViewHolder(TextViewHolder textViewHolder, int position) {
        Message msg = mMessagesList.get(position);
        textViewHolder.getTvMessage().setText(msg.getContent());
        textViewHolder.getIvProfileIcon().setImageResource(R.drawable.arrow_down);
    }


    public class TextViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMessage;
        private ImageView ivProfileIcon;

        public TextViewHolder(View itemView) {
            super(itemView);
            tvMessage = (TextView) itemView.findViewById(R.id.text_view_message_content);
            ivProfileIcon = (ImageView) itemView.findViewById(R.id.image_view_message_sender);
        }

        public TextView getTvMessage() {
            return tvMessage;
        }

        public void setTvMessage(TextView tvMessage) {
            this.tvMessage = tvMessage;
        }

        public ImageView getIvProfileIcon() {
            return ivProfileIcon;
        }

        public void setIvProfileIcon(ImageView ivProfileIcon) {
            this.ivProfileIcon = ivProfileIcon;
        }
    }
}
