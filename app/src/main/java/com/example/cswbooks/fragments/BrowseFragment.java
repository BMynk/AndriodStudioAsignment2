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
import com.example.cswbooks.data.ListingRepository;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

/**
 * BrowseFragment — displays all active listings in a 2-column grid.
 * Owner: Browse Feature Developer (Branch 2)
 * Fixed: Branch 6 — grid now refreshes correctly after new listing added.
 */
public class BrowseFragment extends Fragment {

    private BookAdapter adapter;
    private RecyclerView recyclerView;

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

        // ── RecyclerView ──────────────────────────────────────────────────────
        recyclerView = view.findViewById(R.id.browse_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new BookAdapter(
                getContext(),
                ListingRepository.getInstance().getAllBooks(),
                book -> { /* future: book detail screen */ }
        );
        recyclerView.setAdapter(adapter);

        // ── FAB ───────────────────────────────────────────────────────────────
        ExtendedFloatingActionButton fabSell = view.findViewById(R.id.fab_sell);
        fabSell.setOnClickListener(v -> openSellSheet());

        // ── Shrink FAB on scroll ──────────────────────────────────────────────
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                if (dy > 0 && fabSell.isExtended()) fabSell.shrink();
                else if (dy < 0 && !fabSell.isExtended()) fabSell.extend();
            }
        });
    }

    /** Opens the sell sheet and guarantees the grid refreshes on any change. */
    private void openSellSheet() {
        SellBookBottomSheet sheet = new SellBookBottomSheet();
        sheet.setOnListingAddedListener(this::refreshGrid);
        sheet.show(getParentFragmentManager(), SellBookBottomSheet.TAG);
    }

    /** Reloads the full book list from the repository into the adapter. */
    private void refreshGrid() {
        if (adapter != null) {
            adapter.updateData(ListingRepository.getInstance().getAllBooks());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Always refresh when returning to this screen
        // Catches books added from any other screen
        refreshGrid();
    }
}