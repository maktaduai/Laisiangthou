package com.zogamonline.laisiangthou.items.esv;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KJVBookmarks {

    public final List<Integer> bookmarks = new ArrayList<>();
    private final Context context;

    public KJVBookmarks(Context context) {
        this.context = context;
    }

    public void loadBookmarks() {
        // Remove old bookmarks previously loaded
        bookmarks.clear();

        SharedPreferences prefs = context.getSharedPreferences("Bookmark", 0);
        int ctr = 0;
        String key = "bookmark" + ctr;
        while (prefs.contains(key)) {
            int verseId = prefs.getInt(key, 0);
            if (verseId != 0) {
                bookmarks.add(verseId);
            }

            ctr++;
            key = "bookmark" + ctr;
        }

        Collections.sort(bookmarks);
    }

    private void saveBookmarks() {
        SharedPreferences prefs = context.getSharedPreferences("Bookmark", 0);
        Editor editor = prefs.edit();
        editor.clear();

        for (int i = 0; i < bookmarks.size(); i++) {
            editor.putInt("bookmark" + i, bookmarks.get(i));
        }
        editor.apply();
    }

    public void addBookmark(final Integer verseId) {
        loadBookmarks();

        if (!bookmarks.contains(verseId)) {
            bookmarks.add(verseId);
            Collections.sort(bookmarks);
            saveBookmarks();
        }
    }

    public void removeBookmark(final Integer verseId) {
        loadBookmarks();

        if (bookmarks.contains(verseId)) {
            bookmarks.remove(verseId);
            Collections.sort(bookmarks);
            saveBookmarks();
        }
    }
}
