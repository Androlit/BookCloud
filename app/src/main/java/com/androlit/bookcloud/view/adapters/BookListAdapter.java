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


package com.androlit.bookcloud.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androlit.bookcloud.R;
import com.androlit.bookcloud.data.model.FirebaseBook;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookListViewHolder> {
    private Context mContext;
    private List<FirebaseBook> mFirebaseBooks;

    private BookItemClickListener listener;
    private FirebaseAuth mAuth;

    public BookListAdapter(List<FirebaseBook> firebaseBooks, Context context) {
        mFirebaseBooks = firebaseBooks;
        mContext = context;
    }

    @Override
    public BookListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View bookView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_view_in_recycle_view, parent, false);
        return new BookListViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(BookListViewHolder holder, int position) {
        FirebaseBook firebaseBook = mFirebaseBooks.get(position);

        holder.bookName.setText(firebaseBook.getTitle());
        holder.author.setText(firebaseBook.getAuthor());
        String priceTag = "৳" + firebaseBook.getPrice();
        holder.price.setText(priceTag);
        Glide.with(mContext).load(firebaseBook.getPhotoUrl()).into(holder.bookThumbs);
        holder.locationDistance.setText(firebaseBook.getLocationName());
    }

    public void add(FirebaseBook book) {
        mFirebaseBooks.add(book);
        Log.d("COUNT", mFirebaseBooks.size() + "a");
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mFirebaseBooks.size();
    }

    public void clear() {
        int size = mFirebaseBooks.size();
        mFirebaseBooks.clear();
        notifyItemRangeRemoved(0, size);
    }

    // method to set SearchItemClickListener to this adapter from activity/fragment using it
    public void setBookItemClickListener(BookItemClickListener listener, FirebaseAuth auth) {
        this.listener = listener;
        mAuth = auth;
    }

    // search item click listener interface
    public interface BookItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public class BookListViewHolder extends RecyclerView.ViewHolder {
        private ImageView bookThumbs;
        private TextView bookName;
        private TextView author;
        private TextView locationDistance;
        private TextView price;
        private Button btnBuyNow;

        public BookListViewHolder(final View itemView) {
            super(itemView);
            bookThumbs = itemView.findViewById(R.id.book_thumbs);
            bookName = itemView.findViewById(R.id.book_name);
            author = itemView.findViewById(R.id.author_name);
            locationDistance = itemView.findViewById(R.id.location_distance);
            price = itemView.findViewById(R.id.price_tag);
            btnBuyNow = itemView.findViewById(R.id.btn_buy_now);

            btnBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAuth.getCurrentUser() == null) {
                        Toast.makeText(mContext, "Login to buy!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new MaterialDialog.Builder(mContext)
                            .title("Do You Want to Buy this book")
                            .content("This action will send message to user who has offered this book")
                            .positiveText("YES")
                            .negativeText("No")
                            .onPositive(new MaterialDialog.SingleButtonCallback(){

                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                                        listener.onItemClick(itemView, getAdapterPosition());
                                    }
                                }
                            })
                            .show();
                }
            });
        }
    }
}
