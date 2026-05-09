package com.example.cswbooks;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.cswbooks.fragments.BrowseFragment;
import com.example.cswbooks.fragments.SearchFragment;
import com.example.cswbooks.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            loadFragment(new BrowseFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected;
            int id = item.getItemId();
            if (id == R.id.nav_browse) {
                selected = new BrowseFragment();
            } else if (id == R.id.nav_search) {
                selected = new SearchFragment();
            } else if (id == R.id.nav_settings) {
                selected = new SettingsFragment();
            } else {
                return false;
            }
            loadFragment(selected);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}