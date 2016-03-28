package com.zogamonline.laisiangthou.items.esv;

import android.net.Uri;
import android.provider.BaseColumns;

import com.zogamonline.laisiangthou.providers.esv.KJVProvider;

@SuppressWarnings("unused")
public class Verses implements BaseColumns {
    public static final String DEFAULT_SORT_ORDER = "BookID ASC, Chapter ASC, Verse ASC";

    public static final String ID = "id";
    public static final String NUMBER = "Verse";
    public static final String TEXT = "VerseText";
    public static final String BOOK_ID = "BookID";
    public static final String CHAPTER = "Chapter";

    public Integer id = null;
    public Integer number = null;
    public String text = null;
    public Integer bookId = null;
    public Integer chapter = null;

    public Verses(Integer id, Integer number, final String text,
                  final Integer bookId, final Integer chapter) {
        super();
        this.id = id;
        this.number = number;
        this.text = text;
        this.bookId = bookId;
        this.chapter = chapter;
    }

    public static Uri getContentUri(final Books book, final int chapter) {
        return getContentUri(book.id, chapter);
    }

    public static Uri getContentUri(final int bookId, final int chapter) {
        return Uri.parse(KJVProvider.CONTENT_URI + "/books/" + bookId
                + "/chapters/" + chapter + "/verses");
    }

    public static Uri getContentUri(final Books book, final int chapter,
                                    final int verse) {
        return getContentUri(book.id, chapter, verse);
    }

    public static Uri getContentUri(final int bookId, final int chapter,
                                    final int verse) {
        return Uri.parse(KJVProvider.CONTENT_URI + "/books/" + bookId
                + "/chapters/" + chapter + "/verses/" + verse);
    }

    public static Uri getContentUri() {
        return Uri.parse(KJVProvider.CONTENT_URI + "/verses");
    }

    public static Uri getContentUri(final int verseId) {
        return Uri.parse(KJVProvider.CONTENT_URI + "/verses/" + verseId);
    }

    public static Uri getCountUri(final Books book, final int chapter) {
        return getCountUri(book.id, chapter);
    }

    public static Uri getCountUri(final int bookId, final int chapter) {
        return Uri.parse(KJVProvider.CONTENT_URI + "/books/" + bookId
                + "/chapters/" + chapter + "/verses/count");
    }

    public static String getWhereClause(final Books book, final int chapter) {
        return getWhereClause(book.id, chapter);
    }

    public static String getWhereClause(final int bookId, final int chapter) {
        return "BookID = " + bookId + " AND Chapter = " + chapter;
    }

    public static String getWhereClause(final Books book, final int chapter,
                                        final int verse) {
        return getWhereClause(book.id, chapter, verse);
    }

    public static String getWhereClause(final int bookId, final int chapter,
                                        final int verse) {
        return "BookID = " + bookId + " AND Chapter = " + chapter
                + " AND Verse = " + verse;
    }

    public static String getWhereClause(final int verseId) {
        return "id = " + verseId;
    }
}
