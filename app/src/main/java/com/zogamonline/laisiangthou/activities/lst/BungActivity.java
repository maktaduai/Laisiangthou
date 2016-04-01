package com.zogamonline.laisiangthou.activities.lst;

/*
*Creator: Maktaduai
*/

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
import com.zogamonline.laisiangthou.items.lst.Book;
import com.zogamonline.laisiangthou.providers.alui.LaisiangthouLibrary;

@SuppressWarnings("MismatchedReadAndWriteOfArray")
public class BungActivity extends BaseActivity {
    public static final String TITLE = "BungActivity.title";
    public static final String BOOK_ID = "BungActivity.book_id";
    public static final String CHAPTER = "BungActivity.chapter";

    private int bookId;
    private String book;
    private int chapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bung);
        initBung();
    }

    private void initBung() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(this.setBook(getIntent().getStringExtra(TITLE)));
        drawerToggle.setDrawerIndicatorEnabled(false);

        this.bookId = getIntent().getIntExtra(BOOK_ID, 1);
        this.chapter = getIntent().getIntExtra(CHAPTER, 1);
        GridView gridView = (GridView) findViewById(R.id.chapterList);
        final Book book = LaisiangthouLibrary.getBook(getContentResolver(),
                this.bookId);
        int count = LaisiangthouLibrary.getChapterCount(getContentResolver(),
                book);
        final String[] chapterNames = new String[count];
        for (int i = 0; i < count; i++) {
            chapterNames[i] = " " + (i + 1);
        }
        gridView.setAdapter(new GridAdapter(this, chapterNames));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                BungActivity.this.chapter = position + 1;
                selectChapter();
                Intent intent = new Intent(BungActivity.this,
                        ChapterActivity.class);
                intent.putExtra(ChapterActivity.TITLE, book.name);
                intent.putExtra(ChapterActivity.BOOK_ID, book.id);
                intent.putExtra(ChapterActivity.CHAPTER, chapter);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        });
        int chapterCount = LaisiangthouLibrary.getChapterCount(
                getContentResolver(), this.bookId);
        if (this.chapter == chapterCount) {
            this.bookId++;
            this.chapter = 1;
            final Book nextBook = LaisiangthouLibrary.getBook(
                    getContentResolver(), this.bookId);
            if (nextBook != null) {
                this.setBook(nextBook.name);
            }
            selectChapter();
        } else {
            this.chapter++;
            selectChapter();
        }
    }

    private void selectChapter() {
        final Book book = LaisiangthouLibrary.getBook(getContentResolver(),
                this.bookId);
        int count = LaisiangthouLibrary.getChapterCount(getContentResolver(),
                book);
        final String[] chapterNames = new String[count];
        for (int i = 0; i < count; i++) {
            chapterNames[i] = " " + (i + 1);
        }
    }

    public String getBook() {
        return book;
    }

    public CharSequence setBook(String book) {
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
