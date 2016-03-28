/**
 * @author Maktaduai
 */
/**
 * @author Maktaduai
 *
 */
package com.zogamonline.laisiangthou.tasks;

import android.os.AsyncTask;

import com.zogamonline.laisiangthou.items.esv.Verses;
import com.zogamonline.laisiangthou.providers.esv.KJVLibrary;
import com.zogamonline.laisiangthou.activities.esv.Search;
import com.zogamonline.laisiangthou.activities.esv.SearchResults;

import java.util.List;
import java.util.StringTokenizer;

public class KJVSearchTask extends AsyncTask<Void, Void, List<Verses>> {

    private final SearchResults activity;

    public KJVSearchTask(final SearchResults activity) {
        this.activity = activity;
    }

    @Override
    protected List<Verses> doInBackground(Void... arg0) {

        StringBuilder builder = new StringBuilder();
        switch (activity.method) {
            case Search.SEARCH_METHOD_EXACT_PHRASE:
                builder.append("VerseText LIKE '%").append(activity.searchTerm).append("%'");
                break;
            case Search.SEARCH_METHOD_ALL_WORDS: {
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
            case Search.SEARCH_METHOD_ANY_WORDS: {
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
            case Search.SEARCH_SCOPE_NT_BOOKS:
                builder.append(" AND BookID IN (SELECT id FROM Books WHERE TestamentID = 2)");
                break;
            case Search.SEARCH_SCOPE_OT_BOOKS:
                builder.append(" AND BookID IN (SELECT id FROM Books WHERE TestamentID = 1)");
                break;
            case Search.SEARCH_SCOPE_SELECTED_BOOKS:
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

        return KJVLibrary.getVerses(
                activity.getContentResolver(), builder.toString());
    }

    @Override
    protected void onPostExecute(List<Verses> verses) {
        activity.searchCompleted(verses);
        super.onPostExecute(verses);
    }
}