package com.zogamonline.laisiangthou.providers.esv;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.zogamonline.laisiangthou.items.esv.Books;
import com.zogamonline.laisiangthou.items.esv.Chapters;
import com.zogamonline.laisiangthou.items.esv.Testaments;
import com.zogamonline.laisiangthou.items.esv.Verses;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class KJVLibrary {
    private static final String TAG = "KJVLibrary";

    // Get a list of the testaments
    public static List<Testaments> getTestaments(final ContentResolver resolver) {
        List<Testaments> testaments = new ArrayList<>();
        Cursor cursor = null;
        try {
            try {
                cursor = getTestamentsCursor(resolver);

                while (cursor.moveToNext()) {
                    testaments
                            .add(new Testaments(cursor.getInt(cursor
                                    .getColumnIndex(Testaments.ID)), cursor
                                    .getString(cursor
                                            .getColumnIndex(Testaments.NAME))));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return testaments;
    }

    public static Cursor getTestamentsCursor(final ContentResolver resolver) {
        return resolver.query(Testaments.CONTENT_URI, new String[]{
                        Testaments.ID, Testaments.NAME}, null, null,
                Testaments.DEFAULT_SORT_ORDER);
    }

    // Get all books in the Bible
    public static List<Books> getBooks(final ContentResolver resolver) {
        List<Books> books = new ArrayList<>();

        Cursor cursor = null;
        try {
            try {
                cursor = getBooksCursor(resolver);

                while (cursor.moveToNext()) {
                    books.add(new Books(cursor.getInt(cursor
                            .getColumnIndex(Books.ID)), cursor.getString(cursor
                            .getColumnIndex(Books.NAME))));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return books;
    }

    public static Cursor getBooksCursor(final ContentResolver resolver) {
        return resolver.query(Books.CONTENT_URI, new String[]{Books.ID,
                Books.NAME}, null, null, Books.DEFAULT_SORT_ORDER);
    }

    // Get the books for a testament
    public static List<Books> getBooks(final ContentResolver resolver,
                                       final Testaments testament) {
        return getBooks(resolver, testament.id);
    }

    public static List<Books> getBooks(final ContentResolver resolver,
                                       final int testamentId) {
        List<Books> books = new ArrayList<>();

        Cursor cursor = null;
        try {
            try {
                cursor = getBooksCursor(resolver, testamentId);

                while (cursor.moveToNext()) {
                    books.add(new Books(cursor.getInt(cursor
                            .getColumnIndex(Books.ID)), cursor.getString(cursor
                            .getColumnIndex(Books.NAME))));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return books;
    }

    public static Cursor getBooksCursor(final ContentResolver resolver,
                                        final Testaments testament) {
        return getBooksCursor(resolver, testament.id);
    }

    public static Cursor getBooksCursor(final ContentResolver resolver,
                                        final int testamentId) {
        return resolver.query(Books.CONTENT_URI, new String[]{Books.ID,
                        Books.NAME}, "TestamentID = " + testamentId, null,
                Books.DEFAULT_SORT_ORDER);
    }

    public static Books getBook(final ContentResolver resolver, final int bookId) {
        Books book = null;

        Cursor cursor = null;
        try {
            try {
                cursor = resolver.query(Books.getContentUri(bookId),
                        new String[]{Books.ID, Books.NAME},
                        Books.getWhereClause(bookId), null,
                        Books.DEFAULT_SORT_ORDER);

                //noinspection ConstantConditions
                while (cursor.moveToNext()) {
                    book = new Books(cursor.getInt(cursor
                            .getColumnIndex(Books.ID)), cursor.getString(cursor
                            .getColumnIndex(Books.NAME)));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return book;
    }

    // Get the number of chapters in a book (this may be the only useful
    // Chapter-based method)
    public static int getChapterCount(final ContentResolver resolver,
                                      final Books book) {
        return getChapterCount(resolver, book.id);
    }

    public static int getChapterCount(final ContentResolver resolver,
                                      final int bookId) {
        int count = 0;

        Cursor cursor = null;
        try {
            try {
                cursor = resolver.query(Chapters.getCountUri(bookId),
                        new String[]{"MAX(Chapter) AS count"},
                        Chapters.getWhereClause(bookId), null, "count");

                //noinspection ConstantConditions
                while (cursor.moveToNext()) {
                    count = cursor.getInt(cursor.getColumnIndex("count"));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return count;
    }

    // Get all the chapters in a book
    public static List<Chapters> getChapters(final ContentResolver resolver,
                                             final Books book) {
        return getChapters(resolver, book.id);
    }

    public static List<Chapters> getChapters(final ContentResolver resolver,
                                             final int bookId) {
        List<Chapters> chapters = new ArrayList<>();

        Cursor cursor = null;
        try {
            try {
                cursor = getChaptersCursor(resolver, bookId);

                while (cursor.moveToNext()) {
                    chapters.add(new Chapters(cursor.getInt(cursor
                            .getColumnIndex(Chapters.ID)), cursor.getInt(cursor
                            .getColumnIndex(Chapters.BOOK_ID))));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return chapters;
    }

    public static Cursor getChaptersCursor(final ContentResolver resolver,
                                           final Books book) {
        return getChaptersCursor(resolver, book.id);
    }

    public static Cursor getChaptersCursor(final ContentResolver resolver,
                                           final int bookId) {
        return resolver.query(Chapters.getContentUri(bookId), new String[]{
                        Chapters.ID, Chapters.BOOK_ID},
                Chapters.getWhereClause(bookId), null,
                Chapters.DEFAULT_SORT_ORDER);
    }

    // Get one chapter in a book
    public static Chapters getChapter(final ContentResolver resolver,
                                      final Books book, final int chapter) {
        return getChapter(resolver, book.id, chapter);
    }

    public static Chapters getChapter(final ContentResolver resolver,
                                      final int bookId, final int chapter) {
        return null;
    }

    // Get the number of verses in a chapter
    public static int getVerseCount(final ContentResolver resolver,
                                    final Books book, final int chapter) {
        return getVerseCount(resolver, book.id, chapter);
    }

    public static int getVerseCount(final ContentResolver resolver,
                                    final int bookId, final int chapter) {
        int count = 0;

        Cursor cursor = null;
        try {
            try {
                cursor = resolver.query(Verses.getCountUri(bookId, chapter),
                        new String[]{"MAX(Verse) AS count"},
                        Verses.getWhereClause(bookId, chapter), null, "count");

                //noinspection ConstantConditions
                while (cursor.moveToNext()) {
                    count = cursor.getInt(cursor.getColumnIndex("count"));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return count;
    }

    // Get all the verses for a chapter
    public static List<Verses> getVerses(final ContentResolver resolver,
                                         final Books book, final int chapter) {
        return getVerses(resolver, book.id, chapter);
    }

    public static List<Verses> getVerses(final ContentResolver resolver,
                                         final int bookId, final int chapter) {
        List<Verses> verses = new ArrayList<>();

        Cursor cursor = null;
        try {
            try {
                cursor = getVersesCursor(resolver, bookId, chapter);

                while (cursor.moveToNext()) {
                    verses.add(new Verses(
                            cursor.getInt(cursor.getColumnIndex(Verses.ID)),
                            cursor.getInt(cursor.getColumnIndex(Verses.NUMBER)),
                            cursor.getString(cursor.getColumnIndex(Verses.TEXT)),
                            cursor.getInt(cursor.getColumnIndex(Verses.BOOK_ID)),
                            cursor.getInt(cursor.getColumnIndex(Verses.CHAPTER))));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return verses;
    }

    public static Cursor getVersesCursor(final ContentResolver resolver,
                                         final Books book, final int chapter) {
        return getVersesCursor(resolver, book.id, chapter);
    }

    public static Cursor getVersesCursor(final ContentResolver resolver,
                                         final int bookId, final int chapter) {
        return resolver.query(Verses.getContentUri(bookId, chapter),
                new String[]{Verses.ID, Verses.NUMBER, Verses.TEXT,
                        Verses.BOOK_ID, Verses.CHAPTER},
                Verses.getWhereClause(bookId, chapter), null,
                Verses.DEFAULT_SORT_ORDER);
    }

    // Get one verse for a chapter
    public static Verses getVerse(final ContentResolver resolver,
                                  final Books book, final int chapter, final int verse) {
        return getVerse(resolver, book.id, chapter, verse);
    }

    public static Verses getVerse(final ContentResolver resolver,
                                  final int bookId, final int chapter, final int verse) {
        Verses v = null;

        Cursor cursor = null;
        try {
            try {
                cursor = resolver.query(
                        Verses.getContentUri(bookId, chapter, verse),
                        new String[]{Verses.ID, Verses.NUMBER, Verses.TEXT,
                                Verses.BOOK_ID, Verses.CHAPTER},
                        Verses.getWhereClause(bookId, chapter, verse), null,
                        Verses.DEFAULT_SORT_ORDER);

                //noinspection ConstantConditions
                while (cursor.moveToNext()) {
                    v = new Verses(
                            cursor.getInt(cursor.getColumnIndex(Verses.ID)),
                            cursor.getInt(cursor.getColumnIndex(Verses.NUMBER)),
                            cursor.getString(cursor.getColumnIndex(Verses.TEXT)),
                            cursor.getInt(cursor.getColumnIndex(Verses.BOOK_ID)),
                            cursor.getInt(cursor.getColumnIndex(Verses.CHAPTER)));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return v;
    }

    public static Verses getVerse(final ContentResolver resolver,
                                  final int verseId) {
        Verses v = null;

        Cursor cursor = null;
        try {
            try {
                cursor = resolver.query(Verses.getContentUri(verseId),
                        new String[]{Verses.ID, Verses.NUMBER, Verses.TEXT,
                                Verses.BOOK_ID, Verses.CHAPTER},
                        Verses.getWhereClause(verseId), null,
                        Verses.DEFAULT_SORT_ORDER);

                //noinspection ConstantConditions
                while (cursor.moveToNext()) {
                    v = new Verses(
                            cursor.getInt(cursor.getColumnIndex(Verses.ID)),
                            cursor.getInt(cursor.getColumnIndex(Verses.NUMBER)),
                            cursor.getString(cursor.getColumnIndex(Verses.TEXT)),
                            cursor.getInt(cursor.getColumnIndex(Verses.BOOK_ID)),
                            cursor.getInt(cursor.getColumnIndex(Verses.CHAPTER)));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return v;
    }

    public static List<Verses> getVerses(final ContentResolver resolver,
                                         final String where) {
        List<Verses> verses = new ArrayList<>();

        Cursor cursor = null;
        try {
            try {
                cursor = getVersesCursor(resolver, where);

                while (cursor.moveToNext()) {
                    verses.add(new Verses(
                            cursor.getInt(cursor.getColumnIndex(Verses.ID)),
                            cursor.getInt(cursor.getColumnIndex(Verses.NUMBER)),
                            cursor.getString(cursor.getColumnIndex(Verses.TEXT)),
                            cursor.getInt(cursor.getColumnIndex(Verses.BOOK_ID)),
                            cursor.getInt(cursor.getColumnIndex(Verses.CHAPTER))));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return verses;
    }

    public static Cursor getVersesCursor(final ContentResolver resolver,
                                         final String where) {
        return resolver.query(Verses.getContentUri(), new String[]{Verses.ID,
                        Verses.NUMBER, Verses.TEXT, Verses.BOOK_ID, Verses.CHAPTER},
                where, null, Verses.DEFAULT_SORT_ORDER);
    }
}
