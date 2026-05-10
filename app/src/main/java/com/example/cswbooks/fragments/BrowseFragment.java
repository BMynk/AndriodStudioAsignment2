package com.example.cswbooks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cswbooks.R;
import com.example.cswbooks.SellBookBottomSheet;
import com.example.cswbooks.adapters.BookAdapter;
import com.example.cswbooks.data.SampleData;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

/**
 * BrowseFragment — displays all textbook listings in a 2-column grid.
 * Owner: Browse Feature Developer (Branch 2)
 *
 * Data source: SampleData (upgraded to ListingRepository in Branch 5)
 * Adapter:     BookAdapter (owned by this branch)
 */
public class BrowseFragment extends Fragment {

    // Stored as field so onResume() can refresh it
    private BookAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ── RecyclerView — 2-column grid ──────────────────────────────────────
        RecyclerView recyclerView = view.findViewById(R.id.browse_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new BookAdapter(
                getContext(),
                SampleData.getSampleBooks(),
                book -> { /* Book tap — detail screen added in future branch */ }
        );
        recyclerView.setAdapter(adapter);

        // ── FAB — opens Sell form ─────────────────────────────────────────────
        ExtendedFloatingActionButton fabSell = view.findViewById(R.id.fab_sell);
        fabSell.setOnClickListener(v -> {
            SellBookBottomSheet sheet = new SellBookBottomSheet();
            sheet.show(getParentFragmentManager(), SellBookBottomSheet.TAG);
        });

        // ── Shrink FAB when scrolling down, extend when scrolling up ──────────
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                if (dy > 0 && fabSell.isExtended()) fabSell.shrink();
                else if (dy < 0 && !fabSell.isExtended()) fabSell.extend();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh list when returning to this screen
        // Branch 5 will replace SampleData with ListingRepository here
        if (adapter != null) {
            adapter.updateData(SampleData.getSampleBooks());
        }
    }
}