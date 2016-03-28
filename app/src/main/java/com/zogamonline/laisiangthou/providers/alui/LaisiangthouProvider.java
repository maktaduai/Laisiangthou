package com.zogamonline.laisiangthou.providers.alui;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

/**
 * Creator Tuangoulal Phualte @Maktaduai on 05-08-2015.
 */
@SuppressWarnings("ConstantConditions")
public class LaisiangthouProvider extends ContentProvider {

    private static final String AUTHORITY = "com.zogamonline.laisiangthou.providers.alui.LaisiangthouProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final String TAG = "LaisiangthouProvider";
    private static final int TESTAMENTS = 1;
    private static final int TESTAMENT_ID = 2;
    private static final int BOOKS = 3;
    private static final int BOOK_ID = 4;
    private static final int CHAPTERS = 5;
    private static final int CHAPTER_ID = 6;
    private static final int CHAPTERS_COUNT = 7;
    private static final int VERSES = 8;
    private static final int VERSE_NUMBER = 9;
    private static final int VERSES_COUNT = 10;
    private static final int VERSE_ID = 11;
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, "testaments", TESTAMENTS);
        URI_MATCHER.addURI(AUTHORITY, "testaments/#", TESTAMENT_ID);
        URI_MATCHER.addURI(AUTHORITY, "books", BOOKS);
        URI_MATCHER.addURI(AUTHORITY, "books/#", BOOK_ID);
        URI_MATCHER.addURI(AUTHORITY, "books/#/chapters", CHAPTERS);
        URI_MATCHER.addURI(AUTHORITY, "books/#/chapters/#", CHAPTER_ID);
        URI_MATCHER.addURI(AUTHORITY, "books/#/chapters/count", CHAPTERS_COUNT);
        URI_MATCHER.addURI(AUTHORITY, "books/#/chapters/#/verses", VERSES);
        URI_MATCHER.addURI(AUTHORITY, "books/#/chapters/#/verses/#",
                VERSE_NUMBER);
        URI_MATCHER.addURI(AUTHORITY, "books/#/chapters/#/verses/count",
                VERSES_COUNT);
        URI_MATCHER.addURI(AUTHORITY, "verses", VERSES);
        URI_MATCHER.addURI(AUTHORITY, "verses/#", VERSE_ID);
    }

    private LstDatabase dbHelper;

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case TESTAMENTS:
                return "vnd.android.cursor.dir/vnd.zogamonline.laisiangthou.testament";
            case TESTAMENT_ID:
                return "vnd.android.cursor.item/vnd.zogamonline.laisiangthou.testament";
            case BOOKS:
                return "vnd.android.cursor.dir/vnd.zogamonline.laisiangthou.book";
            case BOOK_ID:
                return "vnd.android.cursor.item/vnd.zogamonline.laisiangthou.book";
            case CHAPTERS:
                return "vnd.android.cursor.dir/vnd.zogamonline.laisiangthou.chapter";
            case CHAPTER_ID:
                return "vnd.android.cursor.item/vnd.zogamonline.laisiangthou.chapter";
            case VERSES:
                return "vnd.android.cursor.dir/vnd.zogamonline.laisiangthou.verse";
            case VERSE_NUMBER:
            case VERSE_ID:
                return "vnd.android.cursor.item/vnd.zogamonline.laisiangthou.verse";
            default:
                return null;
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new LstDatabase(getContext());
        if (dbHelper != null) {
            try {
                dbHelper.createDatabase();
                return true;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
                return false;
            }
        }
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case TESTAMENTS:
            case TESTAMENT_ID: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                SQLiteDatabase sqlDB = dbHelper.getReadableDatabase();
                builder.setTables("Testaments");
                Cursor c = builder.query(sqlDB, projection, selection,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            }
            case BOOK_ID:
            case BOOKS: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                SQLiteDatabase sqlDB = dbHelper.getReadableDatabase();
                builder.setTables("Books");
                Cursor c = builder.query(sqlDB, projection, selection,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            }
            case CHAPTERS:
            case CHAPTER_ID:
            case CHAPTERS_COUNT: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                SQLiteDatabase sqlDB = dbHelper.getReadableDatabase();
                builder.setTables("Verses");
                Cursor c = builder.query(sqlDB, projection, selection,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            }
            case VERSES:
            case VERSE_NUMBER:
            case VERSE_ID:
            case VERSES_COUNT: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                SQLiteDatabase sqlDB = dbHelper.getReadableDatabase();
                builder.setTables("Verses");
                Cursor c = builder.query(sqlDB, projection, selection,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            }
        }

        return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }
}
