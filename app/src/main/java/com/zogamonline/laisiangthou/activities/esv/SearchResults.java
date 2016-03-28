package com.zogamonline.laisiangthou.activities.esv;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.activities.BaseActivity;
import com.zogamonline.laisiangthou.adapters.esv.ResultsAdapter;
import com.zogamonline.laisiangthou.items.esv.Books;
import com.zogamonline.laisiangthou.items.esv.Verses;
import com.zogamonline.laisiangthou.providers.esv.KJVLibrary;
import com.zogamonline.laisiangthou.tasks.KJVSearchTask;

import java.util.List;

public class SearchResults extends BaseActivity implements
        OnItemClickListener {

    public static final String SEARCH_METHOD = "search.method";
    public static final String SEARCH_SCOPE = "search.scope";
    public static final String SEARCH_SCOPE_SELECTED_BOOKS = "search.scope.selected.books";
    public static final String SEARCH_TERM = "search.term";

    public String searchTerm;
    public int method;
    public int scope;
    public int[] selectedBooks;
    private ProgressDialog progress;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //ThemeUtils.onActivityCreateSetTheme(this);
        checkPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        drawerToggle.setDrawerIndicatorEnabled(false);

        ListView searchList = (ListView) findViewById(R.id.searchList);
        searchList.setOnItemClickListener(this);

        this.searchTerm = getIntent().getStringExtra(SEARCH_TERM);
        this.method = getIntent().getIntExtra(SEARCH_METHOD,
                Search.SEARCH_METHOD_EXACT_PHRASE);
        this.scope = getIntent().getIntExtra(SEARCH_SCOPE,
                Search.SEARCH_SCOPE_ALL_BOOKS);
        this.selectedBooks = getIntent().getIntArrayExtra(
                SEARCH_SCOPE_SELECTED_BOOKS);

        ((TextView) findViewById(R.id.search_results_header))
                .setText("Na thilzon \"" + this.searchTerm + "\" "
                        + "toh kisai hiaizah mu:");
        search();
    }

    private void search() {
        progress = ProgressDialog.show(this, null, "Zong lellel...", true);
        KJVSearchTask searcher = new KJVSearchTask(this);
        searcher.execute();
    }

    public void searchCompleted(final List<Verses> verses) {
        ListView searchList = (ListView) findViewById(R.id.searchList);
        ResultsAdapter adapter = new ResultsAdapter(this, verses);
        searchList.setAdapter(adapter);
        if (progress != null)
            progress.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ListView searchList = (ListView) findViewById(R.id.searchList);
        Verses verse = ((ResultsAdapter) searchList.getAdapter())
                .getItem(position);
        Books book = KJVLibrary.getBook(getContentResolver(), verse.bookId);
        Intent intent = new Intent(this, VerseActivity.class);
        intent.putExtra(VerseActivity.TITLE, book.name);
        intent.putExtra(VerseActivity.BOOK_ID, book.id);
        intent.putExtra(VerseActivity.CHAPTER, verse.chapter);
        intent.putExtra(VerseActivity.VERSE, verse.number);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
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
}
