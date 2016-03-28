package com.zogamonline.laisiangthou.activities.esv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.activities.BaseActivity;
import com.zogamonline.laisiangthou.adapters.common.GridAdapter;
import com.zogamonline.laisiangthou.items.esv.Books;
import com.zogamonline.laisiangthou.providers.esv.KJVLibrary;

@SuppressWarnings("MismatchedReadAndWriteOfArray")
public class KJVBungActivity extends BaseActivity{

    public static final String TITLE = "KJVBungActivity.title";
    public static final String BOOK_ID = "KJVBungActivity.book_id";
    public static final String CHAPTER = "KJVBungActivity.chapter";
    private int bookId;
    private String book;
    private int chapter;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bung);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(this.setBook(getIntent().getStringExtra(TITLE)));
        drawerToggle.setDrawerIndicatorEnabled(false);

        this.bookId = getIntent().getIntExtra(BOOK_ID, 1);
        this.chapter = getIntent().getIntExtra(CHAPTER, 1);
        GridView gridView = (GridView) findViewById(R.id.chapterList);
        final Books book = KJVLibrary
                .getBook(getContentResolver(), this.bookId);
        int count = KJVLibrary.getChapterCount(getContentResolver(), book);
        final String[] chapterNames = new String[count];
        for (int i = 0; i < count; i++) {
            chapterNames[i] = " " + (i + 1);
        }
        gridView.setAdapter(new GridAdapter(this, chapterNames));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                KJVBungActivity.this.chapter = position + 1;
                selectChapter();
                Intent intent = new Intent(KJVBungActivity.this,
                        VerseActivity.class);
                intent.putExtra(VerseActivity.TITLE, book.name);
                intent.putExtra(VerseActivity.BOOK_ID, book.id);
                intent.putExtra(VerseActivity.CHAPTER, chapter);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        });
        int chapterCount = KJVLibrary.getChapterCount(getContentResolver(),
                this.bookId);
        if (this.chapter == chapterCount) {
            this.bookId++;
            this.chapter = 1;
            final Books nextBook = KJVLibrary.getBook(getContentResolver(),
                    this.bookId);
            if (nextBook != null) {
                this.setBook(nextBook.name);
            }
            selectChapter();
        } else {
            // Go to the next chapter
            this.chapter++;
            selectChapter();
        }
    }

    private void selectChapter() {
        final Books book = KJVLibrary
                .getBook(getContentResolver(), this.bookId);
        int count = KJVLibrary.getChapterCount(getContentResolver(), book);
        final String[] chapterNames = new String[count];
        for (int i = 0; i < count; i++) {
            chapterNames[i] = " " + (i + 1);
        }
    }

    public String getBook() {
        return book;
    }

    private CharSequence setBook(String book) {
        return this.book = book;
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
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPreferences();
    }

}
