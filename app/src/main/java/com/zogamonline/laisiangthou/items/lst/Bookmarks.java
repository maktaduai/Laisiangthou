package com.zogamonline.laisiangthou.items.lst;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creator: Maktaduai on 05-08-2015.
 */
public class Bookmarks {

    public final List<Integer> bookmarks = new ArrayList<>();
    private final FragmentActivity context;

    public Bookmarks(FragmentActivity thukhunluiFragment) {
        this.context = thukhunluiFragment;
    }

    public void loadBookmarks() {
        // Remove old bookmarks previously loaded
        bookmarks.clear();

        SharedPreferences prefs = context.getSharedPreferences("Bookmarks", 0);
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

    public void saveBookmarks() {
        SharedPreferences prefs = context.getSharedPreferences("Bookmarks", 0);
        SharedPreferences.Editor editor = prefs.edit();
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
