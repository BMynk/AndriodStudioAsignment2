package com.example.cswbooks.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.cswbooks.R;
import com.example.cswbooks.data.ListingRepository;
import com.example.cswbooks.models.Book;
import com.example.cswbooks.models.TextbookListing;
import java.util.List;

/**
 * SettingsFragment — app settings and My Listings summary.
 * Owner: Settings + Integration (Branch 6)
 *
 * My Listings: shows all active listings with title, price, copies.
 * Other rows: wired up with "coming soon" placeholders.
 */
public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ── My Listings — shows real data from repository ─────────────────────
        view.findViewById(R.id.settings_my_listings)
                .setOnClickListener(v -> showMyListings());

        // ── Theme Preferences ─────────────────────────────────────────────────
        view.findViewById(R.id.settings_theme)
                .setOnClickListener(v -> showComingSoon("Theme Preferences"));

        // ── Notifications ─────────────────────────────────────────────────────
        view.findViewById(R.id.settings_notifications)
                .setOnClickListener(v -> showComingSoon("Notifications"));

        // ── Account Details ───────────────────────────────────────────────────
        view.findViewById(R.id.settings_account)
                .setOnClickListener(v -> showComingSoon("Account Details"));

        // ── Help & Support ────────────────────────────────────────────────────
        view.findViewById(R.id.settings_help)
                .setOnClickListener(v -> showHelpDialog());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Nothing to refresh visually here yet —
        // listing count shown dynamically when row is tapped
    }

    // ── My Listings ───────────────────────────────────────────────────────────

    /**
     * Displays all active listings in an AlertDialog.
     * Shows title, price in Rands, copies, and condition.
     */
    private void showMyListings() {
        List<TextbookListing> listings =
                ListingRepository.getInstance().getAllListings();

        if (listings.isEmpty()) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("My Listings")
                    .setMessage("No listings yet.\n\nTap the + Sell button on "
                            + "the Browse screen to add your first listing.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        // Build a readable summary of every active listing
        StringBuilder sb = new StringBuilder();
        sb.append("Active listings: ")
                .append(listings.size())
                .append("\n")
                .append("─────────────────────────\n\n");

        for (TextbookListing listing : listings) {
            Book book = listing.getBook();
            sb.append("📚 ").append(book.getTitle()).append("\n");
            sb.append("    by ").append(book.getAuthor()).append("\n");
            sb.append("    Seller:  ").append(book.getSellerName()).append("\n");
            sb.append("    Price:   R")
                    .append(String.format("%.2f", book.getPrice())).append("\n");
            sb.append("    Copies:  ")
                    .append(book.getCopiesAvailable()).append("\n");
            sb.append("    Condition: ")
                    .append(book.getCondition()).append("\n\n");
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("My Listings ("
                        + listings.size() + " active)")
                .setMessage(sb.toString())
                .setPositiveButton("Close", null)
                .show();
    }

    // ── Helper dialogs ────────────────────────────────────────────────────────

    private void showComingSoon(String feature) {
        Toast.makeText(getContext(),
                feature + " — coming soon",
                Toast.LENGTH_SHORT).show();
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Help & Support")
                .setMessage("CSW Books — Textbook Marketplace\n\n"
                        + "Browse: View all available textbooks\n\n"
                        + "Search: Filter by title, author, seller "
                        + "or category\n\n"
                        + "Sell: Tap + Sell to list your textbook. "
                        + "Fill in all fields. If your listing already "
                        + "exists you can update the copies or price.\n\n"
                        + "My Listings: View all active listings in Settings")
                .setPositiveButton("Got it", null)
                .show();
    }
}