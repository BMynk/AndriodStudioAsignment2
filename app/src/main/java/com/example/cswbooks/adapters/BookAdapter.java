package com.example.cswbooks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cswbooks.R;
import com.example.cswbooks.models.Book;
import java.util.List;

/**
 * BookAdapter — populates the RecyclerView grid with book cards.
 * Owner: Browse Feature Developer (Branch 2)
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final Context context;
    private List<Book> bookList;

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    private final OnBookClickListener listener;

    public BookAdapter(Context context, List<Book> bookList, OnBookClickListener listener) {
        this.context  = context;
        this.bookList = bookList;
        this.listener = listener;
    }

    /** Called by SearchFragment and BrowseFragment to refresh the list. */
    public void updateData(List<Book> newList) {
        this.bookList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_book_card, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.coverImage.setImageResource(book.getCoverResId());
        holder.titleText.setText(book.getTitle());
        holder.sellerText.setText("Sold by " + book.getSellerName());

        // ── Rands currency (ZAR) ──────────────────────────────────────────────
        holder.priceText.setText("R" + String.format("%.2f", book.getPrice()));

        holder.copiesText.setText(book.getCopiesAvailable() + " copies");
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onBookClick(book);
        });
    }

    @Override
    public int getItemCount() {
        return bookList != null ? bookList.size() : 0;
    }

    // ── ViewHolder ────────────────────────────────────────────────────────────

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImage;
        TextView titleText, sellerText, priceText, copiesText;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.book_cover_image);
            titleText  = itemView.findViewById(R.id.book_title);
            sellerText = itemView.findViewById(R.id.book_seller);
            priceText  = itemView.findViewById(R.id.book_price);
            copiesText = itemView.findViewById(R.id.book_copies);
        }
    }
}