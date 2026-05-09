package com.example.cswbooks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.cswbooks.R;

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

        int[] rowIds = { R.id.settings_my_listings, R.id.settings_theme,
                R.id.settings_notifications, R.id.settings_account, R.id.settings_help };
        String[] labels = { "My Listings", "Theme Preferences",
                "Notifications", "Account Details", "Help & Support" };

        for (int i = 0; i < rowIds.length; i++) {
            final String label = labels[i];
            View row = view.findViewById(rowIds[i]);
            if (row != null) row.setOnClickListener(v ->
                    Toast.makeText(getContext(), label + " tapped", Toast.LENGTH_SHORT).show());
        }
    }
}