/**
 * @author Maktaduai
 */
/**
 * @author Maktaduai
 *
 */
package com.zogamonline.laisiangthou.items.esv;

import android.net.Uri;
import android.provider.BaseColumns;

import com.zogamonline.laisiangthou.providers.esv.KJVProvider;

import java.util.ArrayList;

public class Books implements BaseColumns {
    public static final Uri CONTENT_URI = Uri.parse(KJVProvider.CONTENT_URI
            + "/books");

    public static final String DEFAULT_SORT_ORDER = "id";

    public static final String ID = "id";
    public static final String NAME = "Book";

    public Integer id = null;
    public String name = null;
    public ArrayList<Chapters> chapters = null;

    public Books(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public static Uri getContentUri(final int bookId) {
        return Uri.parse(KJVProvider.CONTENT_URI + "/books/" + bookId);
    }

    public static String getWhereClause(final int bookId) {
        return "id = " + bookId;
    }

    @Override
    public String toString() {
        return name;
    }
}