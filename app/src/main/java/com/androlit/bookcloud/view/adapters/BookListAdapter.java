package com.androlit.bookcloud.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.data.model.FirebaseBook;

import java.util.List;

/**
 * Created by ${farhanarnob} on ${06-Oct-16}.
 */

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookListViewHolder> {
    private List<FirebaseBook> mFirebaseBooks;

    public BookListAdapter(List<FirebaseBook> firebaseBooks) {
        mFirebaseBooks = firebaseBooks;
    }

    @Override
    public BookListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View bookView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_view_in_recycle_view, parent, false);
        return new BookListViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(BookListViewHolder holder, int position) {
        FirebaseBook firebaseBook = mFirebaseBooks.get(position);
    }

    @Override
    public int getItemCount() {
        return mFirebaseBooks.size();
    }

    public class BookListViewHolder extends RecyclerView.ViewHolder {
        private ImageView bookThumbs;
        private TextView bookName;
        private TextView author;
        private TextView locationDistance;
        private TextView price;

        public BookListViewHolder(View itemView) {
            super(itemView);
            bookThumbs = (ImageView) itemView.findViewById(R.id.book_thumbs);
            bookName = (TextView) itemView.findViewById(R.id.book_name);
            author = (TextView) itemView.findViewById(R.id.author_name);
            locationDistance = (TextView) itemView.findViewById(R.id.location_distance);
            price = (TextView) itemView.findViewById(R.id.price_tag);
        }
    }
}
