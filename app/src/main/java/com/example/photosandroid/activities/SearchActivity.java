//package com.example.photosandroid.activities;
//
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Spinner;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.photosandroid.R;
//import com.example.photosandroid.adapters.PhotoAdapter;
//import com.example.photosandroid.models.Album;
//import com.example.photosandroid.models.Photo;
//import com.example.photosandroid.utils.StorageUtil;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class SearchActivity extends AppCompatActivity {
//    private Spinner spinner1, spinner2;
//    private AutoCompleteTextView auto1, auto2;
//    private RadioGroup radioAndOr;
//    private RadioButton radioAnd, radioOr;
//    private Button btnSearch;
//    private RecyclerView resultsView;
//    private PhotoAdapter photoAdapter;
//    private List<Photo> searchResults = new ArrayList<>();
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        // Toolbar setup
//        Toolbar toolbar = findViewById(R.id.toolbar_search);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        // Use the search icon on left and title "Search"
//        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_search);
//        getSupportActionBar().setTitle("Search");
//        toolbar.setNavigationOnClickListener(v -> finish());
//
//        // Bind views
//        spinner1     = findViewById(R.id.spinner_tag_type_1);
//        spinner2     = findViewById(R.id.spinner_tag_type_2);
//        auto1        = findViewById(R.id.auto_value_1);
//        auto2        = findViewById(R.id.auto_value_2);
//        radioAndOr   = findViewById(R.id.radio_and_or);
//        radioAnd     = findViewById(R.id.radio_and);
//        radioOr      = findViewById(R.id.radio_or);
//        btnSearch    = findViewById(R.id.btn_search);
//        resultsView  = findViewById(R.id.search_results);
//
//        // RecyclerView setup
//        photoAdapter = new PhotoAdapter(searchResults, /* listener if any */ null);
//        resultsView.setLayoutManager(new GridLayoutManager(this, 2));
//        resultsView.setAdapter(photoAdapter);
//
//        // Populate autocomplete suggestions for both fields
//        setupAutoComplete(auto1, spinner1);
//        setupAutoComplete(auto2, spinner2);
//
//        btnSearch.setOnClickListener(v -> performSearch());
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle < icon press
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void setupAutoComplete(AutoCompleteTextView autoField, Spinner spinnerType) {
//        // Collect **exact** tag values for this type
//        String type = spinnerType.getSelectedItem().toString().toLowerCase();
//        List<Album> albums = StorageUtil.loadAlbums(this);
//        Set<String> suggestions = new HashSet<>();
//        for (Album a : albums) {
//            for (Photo p : a.getPhotos()) {
//                String tagVal = p.getTags().get(type);
//                if (tagVal != null) {
//                    suggestions.add(tagVal);
//                }
//            }
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_dropdown_item_1line,
//                new ArrayList<>(suggestions)
//        );
//        autoField.setAdapter(adapter);
//        autoField.setThreshold(1);
//    }
//
//    private void performSearch() {
//        // Read inputs
//        String type1 = spinner1.getSelectedItem().toString().toLowerCase();
//        String val1  = auto1.getText().toString().trim();
//        String type2 = spinner2.getSelectedItem().toString().toLowerCase();
//        String val2  = auto2.getText().toString().trim();
//        boolean useAnd = radioAnd.isChecked();
//
//        searchResults.clear();
//
//        // 1) No input at all => no results
//        if (val1.isEmpty() && val2.isEmpty()) {
//            photoAdapter.notifyDataSetChanged();
//            return;
//        }
//
//        List<Album> albums = StorageUtil.loadAlbums(this);
//        for (Album album : albums) {
//            for (Photo photo : album.getPhotos()) {
//                // exact (case-insensitive) match only if user typed something
//                String tag1 = photo.getTags().get(type1);
//                String tag2 = photo.getTags().get(type2);
//
//                boolean match1 = !val1.isEmpty() && tag1 != null
//                        && tag1.equalsIgnoreCase(val1);
//                boolean match2 = !val2.isEmpty() && tag2 != null
//                        && tag2.equalsIgnoreCase(val2);
//
//                // If only one field filled, use that
//                if (val2.isEmpty() && match1) {
//                    searchResults.add(photo);
//                } else if (val1.isEmpty() && match2) {
//                    searchResults.add(photo);
//                }
//                // If both fields filled, apply AND/OR
//                else if (!val1.isEmpty() && !val2.isEmpty()) {
//                    if ((useAnd && match1 && match2) ||
//                            (!useAnd && (match1 || match2))) {
//                        searchResults.add(photo);
//                    }
//                }
//            }
//        }
//
//        photoAdapter.notifyDataSetChanged();
//    }
//}

package com.example.photosandroid.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photosandroid.R;
import com.example.photosandroid.adapters.PhotoAdapter;
import com.example.photosandroid.models.Album;
import com.example.photosandroid.models.Photo;
import com.example.photosandroid.utils.StorageUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    private Spinner spinner1, spinner2;
    private AutoCompleteTextView auto1, auto2;
    private RadioButton radioAnd, radioOr;
    private Button btnSearch;
    private RecyclerView resultsView;
    private PhotoAdapter photoAdapter;
    private List<Photo> searchResults = new ArrayList<>();

    private List<String> suggestionMasterList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_search);
        getSupportActionBar().setTitle("Search");
        toolbar.setNavigationOnClickListener(v -> finish());

        // bind views
        spinner1    = findViewById(R.id.spinner_tag_type_1);
        spinner2    = findViewById(R.id.spinner_tag_type_2);
        auto1       = findViewById(R.id.auto_value_1);
        auto2       = findViewById(R.id.auto_value_2);
        radioAnd    = findViewById(R.id.radio_and);
        radioOr     = findViewById(R.id.radio_or);
        btnSearch   = findViewById(R.id.btn_search);
        resultsView = findViewById(R.id.search_results);

        // RecyclerView setup
        photoAdapter = new PhotoAdapter(searchResults, /* optional click listener */ null);
        resultsView.setLayoutManager(new GridLayoutManager(this, 2));
        resultsView.setAdapter(photoAdapter);

        // When the user switches tag types, update suggestions
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                setupAutoComplete(auto1, spinner1);
                auto1.setText("");
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                setupAutoComplete(auto2, spinner2);
                auto2.setText("");
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });

        // Initial population
        setupAutoComplete(auto1, spinner1);
        setupAutoComplete(auto2, spinner2);

        btnSearch.setOnClickListener(v -> performSearch());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the left icon (search) as “Up”
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Sets up an AutoCompleteTextView to suggest only tags of the selected type. */
    private void setupAutoComplete(AutoCompleteTextView autoField, Spinner spinnerType) {
        String type = spinnerType.getSelectedItem().toString().toLowerCase();
        Set<String> suggestions = new HashSet<>();

        for (Album album : StorageUtil.loadAlbums(this)) {
            for (Photo p : album.getPhotos()) {
                String val = p.getTags().get(type);
                if (val != null) suggestions.add(val);
            }
        }

        suggestionMasterList = new ArrayList<>(suggestions);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                new ArrayList<>(suggestionMasterList)
        );
        autoField.setAdapter(adapter);
        autoField.setThreshold(1);

        // Attach the TextWatcher to re-sort on each keystroke
        autoField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                // Remove only leading whitespace, leave everything else
                String raw = s.toString();
                // strip only leading spaces
                String cleaned = raw.replaceFirst("^\\s+", "");
                if (!raw.equals(cleaned)) {
                    // we had leading spaces → update the field and bail
                    autoField.removeTextChangedListener(this);
                    autoField.setText(cleaned);
                    autoField.setSelection(cleaned.length());
                    autoField.addTextChangedListener(this);
                    return;
                }
                // no leading spaces now → do your ranked suggestions
                String prefix = cleaned.toLowerCase();
                List<String> ranked = rankSuggestions(suggestionMasterList, prefix);

                adapter.clear();
                adapter.addAll(ranked);
                adapter.notifyDataSetChanged();
                autoField.showDropDown();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

    }

    /**
     * Ranks the master list by:
     * 1. exact equals
     * 2. startsWith
     * 3. contains
     */
    private List<String> rankSuggestions(List<String> master, String prefix) {
        if (prefix.isEmpty()) {
            // no prefix ⇒ no suggestions
            return Collections.emptyList();
        }

        List<String> exact    = new ArrayList<>();
        List<String> starts   = new ArrayList<>();
        List<String> contains = new ArrayList<>();

        for (String item : master) {
            String lower = item.toLowerCase();
            if (lower.equals(prefix)) {
                exact.add(item);
            } else if (lower.startsWith(prefix)) {
                starts.add(item);
            } else if (lower.contains(prefix)) {
                contains.add(item);
            }
        }

        List<String> out = new ArrayList<>(exact.size() + starts.size() + contains.size());
        out.addAll(exact);
        out.addAll(starts);
        out.addAll(contains);
        return out;
    }

    private void performSearch() {
        String type1 = spinner1.getSelectedItem().toString().toLowerCase();
        String val1  = auto1.getText().toString().trim();
        String type2 = spinner2.getSelectedItem().toString().toLowerCase();
        String val2  = auto2.getText().toString().trim();
        boolean useAnd = radioAnd.isChecked();

        searchResults.clear();

        // No input ⇒ no results
        if (val1.isEmpty() && val2.isEmpty()) {
            photoAdapter.notifyDataSetChanged();
            return;
        }

        for (Album album : StorageUtil.loadAlbums(this)) {
            for (Photo p : album.getPhotos()) {
                String t1 = p.getTags().get(type1);
                String t2 = p.getTags().get(type2);

                boolean match1 = !val1.isEmpty() && t1 != null
                        && t1.equalsIgnoreCase(val1);
                boolean match2 = !val2.isEmpty() && t2 != null
                        && t2.equalsIgnoreCase(val2);

                // One tag only
                if (val2.isEmpty() && match1) {
                    searchResults.add(p);
                } else if (val1.isEmpty() && match2) {
                    searchResults.add(p);
                }
                // Two tags
                else if (!val1.isEmpty() && !val2.isEmpty()) {
                    if ((useAnd && match1 && match2) ||
                            (!useAnd && (match1 || match2))) {
                        searchResults.add(p);
                    }
                }
            }
        }

        photoAdapter.notifyDataSetChanged();
    }

}

