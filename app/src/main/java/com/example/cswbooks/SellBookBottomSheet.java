package com.example.cswbooks;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.cswbooks.data.ListingRepository;
import com.example.cswbooks.models.Book;
import com.example.cswbooks.models.DuplicateListingException;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * SellBookBottomSheet — form for posting a new textbook listing.
 * Owner: Sell Feature Developer (Branch 4)
 * Updated: Branch 5 — uses ListingRepository + DuplicateListingException.
 *
 * Duplicate logic:
 *   Same title + author + seller = duplicate.
 *   User is offered to update copies or price instead of being blocked.
 */
public class SellBookBottomSheet extends BottomSheetDialogFragment {

    public static final String TAG = "SellBookBottomSheet";

    // ── Callback so BrowseFragment refreshes after posting ────────────────────
    public interface OnListingAddedListener {
        void onListingAdded();
    }

    private OnListingAddedListener listingAddedListener;

    public void setOnListingAddedListener(OnListingAddedListener listener) {
        this.listingAddedListener = listener;
    }

    // ── Views ─────────────────────────────────────────────────────────────────
    private TextInputLayout   tilTitle, tilAuthor, tilSeller, tilEdition,
            tilCondition, tilPrice, tilCopies, tilNotes;
    private TextInputEditText etTitle, etAuthor, etSeller, etEdition,
            etPrice, etCopies, etNotes;
    private AutoCompleteTextView conditionDropdown;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_sell, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ── Bind views ────────────────────────────────────────────────────────
        tilTitle     = view.findViewById(R.id.til_title);
        tilAuthor    = view.findViewById(R.id.til_author);
        tilSeller    = view.findViewById(R.id.til_seller);
        tilEdition   = view.findViewById(R.id.til_edition);
        tilCondition = view.findViewById(R.id.til_condition);
        tilPrice     = view.findViewById(R.id.til_price);
        tilCopies    = view.findViewById(R.id.til_copies);
        tilNotes     = view.findViewById(R.id.til_notes);

        etTitle   = view.findViewById(R.id.sell_title);
        etAuthor  = view.findViewById(R.id.sell_author);
        etSeller  = view.findViewById(R.id.sell_seller);
        etEdition = view.findViewById(R.id.sell_edition);
        etPrice   = view.findViewById(R.id.sell_price);
        etCopies  = view.findViewById(R.id.sell_copies);
        etNotes   = view.findViewById(R.id.sell_notes);

        // ── Condition dropdown ────────────────────────────────────────────────
        conditionDropdown = view.findViewById(R.id.sell_condition);
        String[] conditions = {"New", "Like New", "Good", "Fair", "Poor"};
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                conditions);
        conditionDropdown.setAdapter(dropdownAdapter);

        // ── Upload placeholder ────────────────────────────────────────────────
        View uploadArea = view.findViewById(R.id.sell_upload_area);
        if (uploadArea != null) {
            uploadArea.setOnClickListener(v ->
                    Toast.makeText(getContext(),
                            "Image picker — coming soon",
                            Toast.LENGTH_SHORT).show());
        }

        // ── Close button ──────────────────────────────────────────────────────
        View closeBtn = view.findViewById(R.id.sell_close_button);
        if (closeBtn != null) closeBtn.setOnClickListener(v -> dismiss());

        // ── Post Listing button ───────────────────────────────────────────────
        Button postButton = view.findViewById(R.id.sell_post_button);
        postButton.setOnClickListener(v -> attemptPostListing());
    }

    // ── Validation + Submission ───────────────────────────────────────────────

    private void attemptPostListing() {

        clearErrors();

        // Read fields
        String title     = getText(etTitle);
        String author    = getText(etAuthor);
        String seller    = getText(etSeller);
        String edition   = getText(etEdition);
        String condition = conditionDropdown.getText().toString().trim();
        String priceStr  = getText(etPrice);
        String copiesStr = getText(etCopies);
        String notes     = getText(etNotes);

        // Validate
        boolean hasError = false;

        if (title.isEmpty()) {
            tilTitle.setError("Title is required");
            hasError = true;
        }
        if (author.isEmpty()) {
            tilAuthor.setError("Author is required");
            hasError = true;
        }
        if (seller.isEmpty()) {
            tilSeller.setError("Your name is required");
            hasError = true;
        }
        if (condition.isEmpty()) {
            tilCondition.setError("Please select a condition");
            hasError = true;
        }

        double price = 0;
        if (priceStr.isEmpty()) {
            tilPrice.setError("Price is required");
            hasError = true;
        } else {
            try {
                price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    tilPrice.setError("Price must be greater than R0.00");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                tilPrice.setError("Enter a valid price e.g. 250.00");
                hasError = true;
            }
        }

        int copies = 0;
        if (copiesStr.isEmpty()) {
            tilCopies.setError("Number of copies is required");
            hasError = true;
        } else {
            try {
                copies = Integer.parseInt(copiesStr);
                if (copies <= 0) {
                    tilCopies.setError("Must have at least 1 copy");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                tilCopies.setError("Enter a valid number e.g. 2");
                hasError = true;
            }
        }

        if (hasError) return;

        // Build book from validated input
        final double finalPrice  = price;
        final int    finalCopies = copies;

        Book newBook = new Book(
                title, author, seller,
                finalPrice, finalCopies,
                R.drawable.cover_psychology,
                edition, condition, notes
        );

        // Check for duplicate via repository
        Book duplicate = ListingRepository.getInstance().findDuplicate(newBook);

        if (duplicate != null) {
            showDuplicateDialog(duplicate, finalPrice, finalCopies);
            return;
        }

        // No duplicate — add to repository
        try {
            ListingRepository.getInstance().addListing(newBook);
            Toast.makeText(getContext(),
                    "Listing posted! R" + String.format("%.2f", finalPrice)
                            + " | " + finalCopies
                            + (finalCopies == 1 ? " copy" : " copies"),
                    Toast.LENGTH_SHORT).show();

            if (listingAddedListener != null) listingAddedListener.onListingAdded();
            dismiss();

        } catch (DuplicateListingException e) {
            // Safety net — should be caught by findDuplicate above
            tilTitle.setError("Duplicate: " + e.getMessage());
        }
    }

    /**
     * Shows options when a duplicate listing is detected.
     * User can update copies, update price, or cancel.
     */
    private void showDuplicateDialog(Book existing,
                                     double newPrice,
                                     int newCopies) {
        String message = "You already have a listing for:\n\n"
                + "\"" + existing.getTitle() + "\"\n"
                + "by " + existing.getAuthor() + "\n\n"
                + "Current price:  R" + String.format("%.2f", existing.getPrice()) + "\n"
                + "Current copies: " + existing.getCopiesAvailable() + "\n\n"
                + "What would you like to do?";

        new AlertDialog.Builder(requireContext())
                .setTitle("Listing Already Exists")
                .setMessage(message)
                .setPositiveButton("Update Copies", (dialog, which) -> {
                    existing.setCopiesAvailable(newCopies);
                    Toast.makeText(getContext(),
                            "Copies updated to " + newCopies,
                            Toast.LENGTH_SHORT).show();
                    if (listingAddedListener != null) listingAddedListener.onListingAdded();
                    dismiss();
                })
                .setNeutralButton("Update Price", (dialog, which) -> {
                    existing.setPrice(newPrice);
                    Toast.makeText(getContext(),
                            "Price updated to R" + String.format("%.2f", newPrice),
                            Toast.LENGTH_SHORT).show();
                    if (listingAddedListener != null) listingAddedListener.onListingAdded();
                    dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String getText(TextInputEditText field) {
        return (field != null && field.getText() != null)
                ? field.getText().toString().trim()
                : "";
    }

    private void clearErrors() {
        tilTitle.setError(null);
        tilAuthor.setError(null);
        tilSeller.setError(null);
        tilCondition.setError(null);
        tilPrice.setError(null);
        tilCopies.setError(null);
    }
}