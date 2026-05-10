package com.example.cswbooks.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cswbooks.R;
import com.example.cswbooks.adapters.BookAdapter;
import com.example.cswbooks.data.ListingRepository;
import com.example.cswbooks.models.Book;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchFragment — real-time search and category filtering.
 * Owner: Search Feature Developer (Branch 3)
 * Updated: Branch 5 — uses ListingRepository + Searchable interface.
 *
 * Category chips now work correctly because filtering delegates
 * to Book.matchesCategory() via the Searchable interface.
 */
public class SearchFragment extends Fragment {

    private BookAdapter adapter;
    private List<Book> allBooks;
    private String activeCategory = "";
    private String activeQuery    = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ── Load from repository ──────────────────────────────────────────────
        allBooks = ListingRepository.getInstance().getAllBooks();

        // ── RecyclerView ──────────────────────────────────────────────────────
        RecyclerView recyclerView = view.findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new BookAdapter(getContext(), new ArrayList<>(allBooks), book -> {});
        recyclerView.setAdapter(adapter);

        // ── Search bar ────────────────────────────────────────────────────────
        EditText searchBar = view.findViewById(R.id.search_edit_text);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                activeQuery = s.toString().trim();
                filterBooks();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // ── Category chips ────────────────────────────────────────────────────
        ChipGroup chipGroup = view.findViewById(R.id.chip_group_categories);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                activeCategory = "";
            } else {
                Chip chip = group.findViewById(checkedIds.get(0));
                activeCategory = chip != null ? chip.getText().toString() : "";
            }
            filterBooks();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Picks up any books added via Sell form
        allBooks = ListingRepository.getInstance().getAllBooks();
        filterBooks();
    }

    /**
     * Filters using the Searchable interface on Book.
     * Both conditions must be true for a book to show.
     *
     * matchesQuery()    — title, author, or seller contains the text
     * matchesCategory() — keyword match on title per category
     */
    private void filterBooks() {
        List<Book> filtered = new ArrayList<>();
        for (Book book : allBooks) {
            if (book.matchesQuery(activeQuery)
                    && book.matchesCategory(activeCategory)) {
                filtered.add(book);
            }
        }
        adapter.updateData(filtered);
    }
}