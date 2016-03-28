package com.zogamonline.laisiangthou.items.lst;

import android.net.Uri;
import android.provider.BaseColumns;

import com.zogamonline.laisiangthou.providers.alui.LaisiangthouProvider;

import java.util.ArrayList;

/**
 * Creator Maktaduai on 05-08-2015.
 */
public class Book implements BaseColumns {
    public static final Uri CONTENT_URI = Uri
            .parse(LaisiangthouProvider.CONTENT_URI + "/books");

    public static final String DEFAULT_SORT_ORDER = "id";

    public static final String ID = "id";
    public static final String NAME = "Book";

    public Integer id = null;
    public String name = null;
    public ArrayList<Chapter> chapters = null;

    public Book(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public static Uri getContentUri(final int bookId) {
        return Uri.parse(LaisiangthouProvider.CONTENT_URI + "/books/" + bookId);
    }

    public static String getWhereClause(final int bookId) {
        return "id = " + bookId;
    }

    @Override
    public String toString() {
        return name;
    }
}
