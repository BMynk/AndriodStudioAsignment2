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

public class SearchFragment extends Fragment {

    private BookAdapter adapter;
    private List<Book> allBooks;
    private String activeCategory = "";
    private String activeQuery = "";

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

        allBooks = SampleData.getSampleBooks();

        RecyclerView recyclerView = view.findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new BookAdapter(getContext(), new ArrayList<>(allBooks), book -> {});
        recyclerView.setAdapter(adapter);

        EditText searchBar = view.findViewById(R.id.search_edit_text);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                activeQuery = s.toString().toLowerCase().trim();
                filterBooks();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

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

    private void filterBooks() {
        List<Book> filtered = new ArrayList<>();
        for (Book book : allBooks) {
            boolean matchesQuery = activeQuery.isEmpty()
                    || book.getTitle().toLowerCase().contains(activeQuery)
                    || book.getSellerName().toLowerCase().contains(activeQuery);
            boolean matchesCategory;
            switch (activeCategory) {
                case "Science":
                    matchesCategory = book.getTitle().toLowerCase().contains("bio")
                            || book.getTitle().toLowerCase().contains("chem")
                            || book.getTitle().toLowerCase().contains("psych")
                            || book.getTitle().toLowerCase().contains("organ");
                    break;
                case "History":
                    matchesCategory = book.getTitle().toLowerCase().contains("hist");
                    break;
                case "Business":
                    matchesCategory = book.getTitle().toLowerCase().contains("econ")
                            || book.getTitle().toLowerCase().contains("micro")
                            || book.getTitle().toLowerCase().contains("business");
                    break;
                default:
                    matchesCategory = true;
            }
            if (matchesQuery && matchesCategory) filtered.add(book);
        }
        adapter.updateData(filtered);
    }
}