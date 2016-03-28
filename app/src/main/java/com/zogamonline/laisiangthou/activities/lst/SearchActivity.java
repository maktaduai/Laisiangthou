package com.zogamonline.laisiangthou.activities.lst;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.activities.BaseActivity;
import com.zogamonline.laisiangthou.items.lst.Book;
import com.zogamonline.laisiangthou.providers.alui.LaisiangthouLibrary;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements
        View.OnClickListener {

    public static final int SEARCH_METHOD_EXACT_PHRASE = 0;
    public static final int SEARCH_METHOD_ALL_WORDS = 1;
    public static final int SEARCH_METHOD_ANY_WORDS = 2;

    public static final int SEARCH_SCOPE_ALL_BOOKS = 0;
    public static final int SEARCH_SCOPE_OT_BOOKS = 1;
    public static final int SEARCH_SCOPE_NT_BOOKS = 2;
    public static final int SEARCH_SCOPE_SELECTED_BOOKS = 3;

    private EditText searchField;
    private ListView selectedBooksListView;
    private Spinner searchMethodSpinner;
    private Spinner searchScopeSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        checkPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        drawerToggle.setDrawerIndicatorEnabled(false);

        searchField = (EditText) findViewById(R.id.search_term);
        List<Book> books = LaisiangthouLibrary.getBooks(getContentResolver());
        selectedBooksListView = (ListView) findViewById(R.id.search_selected_books_list);

        ArrayAdapter<Book> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, books);
        selectedBooksListView.setAdapter(adapter);
        selectedBooksListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        selectedBooksListView.setVisibility(View.INVISIBLE);

        searchMethodSpinner = (Spinner) findViewById(R.id.search_method);
        searchScopeSpinner = (Spinner) findViewById(R.id.search_scope);

        ArrayAdapter<CharSequence> methodsAdapter = ArrayAdapter
                .createFromResource(this, R.array.search_methods,
                        android.R.layout.simple_spinner_item);
        methodsAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchMethodSpinner.setAdapter(methodsAdapter);

        ArrayAdapter<CharSequence> scopeAdapter = ArrayAdapter
                .createFromResource(this, R.array.search_scope,
                        android.R.layout.simple_spinner_item);
        scopeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchScopeSpinner.setAdapter(scopeAdapter);
        searchScopeSpinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        selectedBooksListView
                                .setVisibility((SEARCH_SCOPE_SELECTED_BOOKS == position) ? View.VISIBLE
                                        : View.INVISIBLE);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedBooksListView.setVisibility(View.INVISIBLE);
                    }
                });
        selectedBooksListView.setFastScrollEnabled(true);
        findViewById(R.id.submit_search).setOnClickListener(this);
        searchField
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (event != null
                                && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(
                                    searchField.getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            return true;
                        }
                        return false;
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if ("".equals(searchField.getText().toString())) {
            Toast.makeText(this, "Thumal zon ding type masa in",
                    Toast.LENGTH_SHORT).show();
        } else if (SEARCH_SCOPE_SELECTED_BOOKS == searchScopeSpinner
                .getSelectedItemPosition() && getSelectedBooks().length < 1) {
            Toast.makeText(this, "Laibu bangmah ki teellou", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra(SearchResultsActivity.SEARCH_TERM, searchField
                    .getText().toString());
            intent.putExtra(SearchResultsActivity.SEARCH_METHOD,
                    searchMethodSpinner.getSelectedItemPosition());
            intent.putExtra(SearchResultsActivity.SEARCH_SCOPE,
                    searchScopeSpinner.getSelectedItemPosition());
            if (SEARCH_SCOPE_SELECTED_BOOKS == searchScopeSpinner
                    .getSelectedItemPosition()) {
                intent.putExtra(
                        SearchResultsActivity.SEARCH_SCOPE_SELECTED_BOOKS,
                        getSelectedBooks());
            }
            startActivity(intent);
        }
    }

    @SuppressWarnings("unchecked")
    private int[] getSelectedBooks() {
        ArrayList<Integer> selectedBooks = new ArrayList<>();
        SparseBooleanArray items = selectedBooksListView
                .getCheckedItemPositions();
        ArrayAdapter<Book> adapter = (ArrayAdapter<Book>) selectedBooksListView
                .getAdapter();
        for (int i = 0; i < items.size(); i++) {
            if (items.valueAt(i)) {
                selectedBooks.add(adapter.getItem(items.keyAt(i)).id);
            }
        }
        int[] asIntArray = new int[selectedBooks.size()];
        for (int i = 0; i < selectedBooks.size(); i++) {
            asIntArray[i] = selectedBooks.get(i);
        }
        return asIntArray;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    private void checkPreferences() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (sharedPrefs.getBoolean("notifications_new_message", false)) {
            getWindow()
                    .addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}
