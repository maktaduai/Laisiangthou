package com.zogamonline.laisiangthou.activities.lst;

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
import android.widget.ListView;
import android.widget.TextView;

import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.activities.BaseActivity;
import com.zogamonline.laisiangthou.adapters.lst.SearchResultsAdapter;
import com.zogamonline.laisiangthou.items.lst.Book;
import com.zogamonline.laisiangthou.items.lst.Verse;
import com.zogamonline.laisiangthou.providers.alui.LaisiangthouLibrary;
import com.zogamonline.laisiangthou.tasks.LaisiangthouSearchTask;

import java.util.List;

public class SearchResultsActivity extends BaseActivity implements
        AdapterView.OnItemClickListener {

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
    protected void onCreate(Bundle savedInstanceState) {
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
                SearchActivity.SEARCH_METHOD_EXACT_PHRASE);
        this.scope = getIntent().getIntExtra(SEARCH_SCOPE,
                SearchActivity.SEARCH_SCOPE_ALL_BOOKS);
        this.selectedBooks = getIntent().getIntArrayExtra(
                SEARCH_SCOPE_SELECTED_BOOKS);

        ((TextView) findViewById(R.id.search_results_header))
                .setText("Na thilzon \"" + this.searchTerm + "\" "
                        + "toh kisai hiaizah mu:");
        search();
    }

    private void search() {
        progress = ProgressDialog.show(this, null, "Zong lellel...", true);
        LaisiangthouSearchTask searcher = new LaisiangthouSearchTask(this);
        searcher.execute();
    }

    public void searchCompleted(final List<Verse> verses) {
        ListView searchList = (ListView) findViewById(R.id.searchList);
        SearchResultsAdapter adapter = new SearchResultsAdapter(this, verses);
        searchList.setAdapter(adapter);
        if (progress != null)
            progress.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ListView searchList = (ListView) findViewById(R.id.searchList);
        Verse verse = ((SearchResultsAdapter) searchList.getAdapter())
                .getItem(position);
        Book book = LaisiangthouLibrary.getBook(getContentResolver(),
                verse.bookId);
        Intent intent = new Intent(this, ChapterActivity.class);
        intent.putExtra(ChapterActivity.TITLE, book.name);
        intent.putExtra(ChapterActivity.BOOK_ID, book.id);
        intent.putExtra(ChapterActivity.CHAPTER, verse.chapter);
        intent.putExtra(ChapterActivity.VERSE, verse.number);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
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
