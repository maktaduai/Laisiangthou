package com.zogamonline.laisiangthou.items.esv;

import android.net.Uri;
import android.provider.BaseColumns;

import com.zogamonline.laisiangthou.providers.esv.KJVProvider;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Chapters implements BaseColumns {
    public static final String DEFAULT_SORT_ORDER = "Chapter ASC, Verse ASC";

    public static final String BOOK_ID = "BookID";
    public static final String ID = "Chapter";

    public Integer id = null;
    public Integer bookId = null;
    public ArrayList<Verses> verses = null;

    public Chapters(Integer id, Integer bookId) {
        super();
        this.id = id;
        this.bookId = bookId;
    }

    public static Uri getContentUri(final Books book) {
        return getContentUri(book.id);
    }

    public static Uri getContentUri(final int bookId) {
        return Uri.parse(KJVProvider.CONTENT_URI + "/books/" + bookId
                + "/chapters");
    }

    public static Uri getContentUri(final Books book, final int chapter) {
        return getContentUri(book.id);
    }

    public static Uri getContentUri(final int bookId, final int chapter) {
        return Uri.parse(KJVProvider.CONTENT_URI + "/books/" + bookId
                + "/chapters/" + chapter);
    }

    public static Uri getCountUri(final Books book) {
        return getCountUri(book.id);
    }

    public static Uri getCountUri(final int bookId) {
        return Uri.parse(KJVProvider.CONTENT_URI + "/books/" + bookId
                + "/chapters/count");
    }

    public static String getWhereClause(final Books book) {
        return getWhereClause(book.id);
    }

    public static String getWhereClause(final int bookId) {
        return "BookID = " + bookId;
    }
}
