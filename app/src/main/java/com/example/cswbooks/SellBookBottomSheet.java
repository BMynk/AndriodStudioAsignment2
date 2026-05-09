package com.example.cswbooks;

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class SellBookBottomSheet extends BottomSheetDialogFragment {

    public static final String TAG = "SellBookBottomSheet";

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

        AutoCompleteTextView conditionDropdown = view.findViewById(R.id.sell_condition);
        String[] conditions = {"New", "Like New", "Good", "Fair", "Poor"};
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                conditions);
        conditionDropdown.setAdapter(dropdownAdapter);

        View uploadArea = view.findViewById(R.id.sell_upload_area);
        if (uploadArea != null) {
            uploadArea.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Image picker coming soon", Toast.LENGTH_SHORT).show());
        }

        Button postButton = view.findViewById(R.id.sell_post_button);
        postButton.setOnClickListener(v -> {
            TextInputEditText titleField  = view.findViewById(R.id.sell_title);
            TextInputEditText authorField = view.findViewById(R.id.sell_author);
            TextInputEditText priceField  = view.findViewById(R.id.sell_price);

            String title  = titleField  != null && titleField.getText()  != null ? titleField.getText().toString().trim()  : "";
            String author = authorField != null && authorField.getText() != null ? authorField.getText().toString().trim() : "";
            String price  = priceField  != null && priceField.getText()  != null ? priceField.getText().toString().trim()  : "";

            if (title.isEmpty() || author.isEmpty() || price.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in Title, Author, and Price", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "Listing posted!", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        View closeBtn = view.findViewById(R.id.sell_close_button);
        if (closeBtn != null) closeBtn.setOnClickListener(v -> dismiss());
    }
}