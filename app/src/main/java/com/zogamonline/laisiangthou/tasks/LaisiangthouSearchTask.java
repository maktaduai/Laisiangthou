package com.zogamonline.laisiangthou.tasks;

import android.os.AsyncTask;

import com.zogamonline.laisiangthou.items.lst.Verse;
import com.zogamonline.laisiangthou.providers.alui.LaisiangthouLibrary;
import com.zogamonline.laisiangthou.activities.lst.SearchActivity;
import com.zogamonline.laisiangthou.activities.lst.SearchResultsActivity;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Creator Maktaduai on 05-08-2015.
 */
public class LaisiangthouSearchTask extends AsyncTask<Void, Void, List<Verse>> {

    private final SearchResultsActivity activity;

    public LaisiangthouSearchTask(final SearchResultsActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<Verse> doInBackground(Void... arg0) {

        StringBuilder builder = new StringBuilder();
        switch (activity.method) {
            case SearchActivity.SEARCH_METHOD_EXACT_PHRASE:
                builder.append("VerseText LIKE '%").append(activity.searchTerm).append("%'");
                break;
            case SearchActivity.SEARCH_METHOD_ALL_WORDS: {
                StringTokenizer tokenizer = new StringTokenizer(
                        activity.searchTerm, " ,.");
                while (tokenizer.hasMoreTokens()) {
                    builder.append("VerseText LIKE '%").append(tokenizer.nextToken()).append("%'");
                    if (tokenizer.hasMoreTokens()) {
                        builder.append(" AND ");
                    }
                }
            }
            break;
            case SearchActivity.SEARCH_METHOD_ANY_WORDS: {
                StringTokenizer tokenizer = new StringTokenizer(
                        activity.searchTerm, " ,.");
                while (tokenizer.hasMoreTokens()) {
                    builder.append("VerseText LIKE '%").append(tokenizer.nextToken()).append("%'");
                    if (tokenizer.hasMoreTokens()) {
                        builder.append(" OR ");
                    }
                }
            }
            break;
        }

        switch (activity.scope) {
            case SearchActivity.SEARCH_SCOPE_NT_BOOKS:
                builder.append(" AND BookID IN (SELECT id FROM Books WHERE TestamentID = 2)");
                break;
            case SearchActivity.SEARCH_SCOPE_OT_BOOKS:
                builder.append(" AND BookID IN (SELECT id FROM Books WHERE TestamentID = 1)");
                break;
            case SearchActivity.SEARCH_SCOPE_SELECTED_BOOKS:
                builder.append(" AND BookID IN (");
                for (int i = 0; i < activity.selectedBooks.length; i++) {
                    builder.append(activity.selectedBooks[i]);
                    if (i < (activity.selectedBooks.length - 1)) {
                        builder.append(", ");
                    }
                }
                builder.append(")");
                break;
        }

        return LaisiangthouLibrary.getVerses(
                activity.getContentResolver(), builder.toString());
    }

    @Override
    protected void onPostExecute(List<Verse> verses) {
        activity.searchCompleted(verses);
        super.onPostExecute(verses);
    }
}
