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
import com.example.cswbooks.data.SampleData;
import com.example.cswbooks.models.Book;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchFragment — real-time search and category filtering.
 * Owner: Search Feature Developer (Branch 3)
 *
 * Filtering strategy:
 *   - Text query:  matches title, author, or seller name
 *   - Category:    matches subject area via keyword detection
 *
 * Branch 5 will add the Searchable interface to Book.java,
 * at which point matchesQuery() and matchesCategory() calls
 * below will delegate directly to Book — no changes needed here.
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

        // ── Load books ────────────────────────────────────────────────────────
        // Branch 5 upgrades this to ListingRepository.getInstance().getAllBooks()
        allBooks = SampleData.getSampleBooks();

        // ── RecyclerView — reuses same adapter and card layout as Browse ──────
        RecyclerView recyclerView = view.findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new BookAdapter(getContext(), new ArrayList<>(allBooks), book -> {});
        recyclerView.setAdapter(adapter);

        // ── Search bar — filters on every keystroke ───────────────────────────
        EditText searchBar = view.findViewById(R.id.search_edit_text);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                activeQuery = s.toString().trim();
                filterBooks();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // ── Category chips — single selection, deselectable ───────────────────
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
        // Refresh source list in case new books were added via Sell form
        // Branch 5 replaces SampleData with ListingRepository here
        allBooks = SampleData.getSampleBooks();
        filterBooks();
    }

    // ── Filtering ─────────────────────────────────────────────────────────────

    /**
     * Applies both the text query and the active category chip filter.
     * Each book is checked against both conditions — must satisfy both to show.
     *
     * When Branch 5 adds Searchable to Book, replace the two helper calls
     * below with:
     *     book.matchesQuery(activeQuery)
     *     book.matchesCategory(activeCategory)
     */
    private void filterBooks() {
        List<Book> filtered = new ArrayList<>();
        for (Book book : allBooks) {
            if (matchesQuery(book, activeQuery)
                    && matchesCategory(book, activeCategory)) {
                filtered.add(book);
            }
        }
        adapter.updateData(filtered);
    }

    /**
     * Returns true if the book title, author, or seller
     * contains the query string (case-insensitive).
     * Empty query always returns true (show all).
     */
    private boolean matchesQuery(Book book, String query) {
        if (query == null || query.isEmpty()) return true;
        String q = query.toLowerCase();
        return book.getTitle().toLowerCase().contains(q)
                || book.getSellerName().toLowerCase().contains(q)
                || book.getAuthor().toLowerCase().contains(q);
    }

    /**
     * Returns true if the book belongs to the selected category.
     * Category matching is keyword-based on the title.
     * Empty category always returns true (show all).
     *
     * Categories: Science | History | Business | Computing
     */
    private boolean matchesCategory(Book book, String category) {
        if (category == null || category.isEmpty()) return true;
        String title = book.getTitle().toLowerCase();
        switch (category) {
            case "Science":
                return title.contains("bio")
                        || title.contains("chem")
                        || title.contains("psych")
                        || title.contains("organ");
            case "History":
                return title.contains("hist");
            case "Business":
                return title.contains("econ")
                        || title.contains("micro")
                        || title.contains("business");
            case "Computing":
                return title.contains("algorithm")
                        || title.contains("comput")
                        || title.contains("calculus");
            default:
                return true;
        }
    }
}