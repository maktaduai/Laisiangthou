package com.zogamonline.laisiangthou.adapters.esv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.items.esv.Books;
import com.zogamonline.laisiangthou.items.esv.Verses;
import com.zogamonline.laisiangthou.providers.esv.KJVLibrary;
import com.zogamonline.laisiangthou.activities.esv.Search;
import com.zogamonline.laisiangthou.activities.esv.SearchResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

@SuppressLint({"UseSparseArrays", "InflateParams", "DefaultLocale"})
public class ResultsAdapter extends ArrayAdapter<Verses> {
    private static LayoutInflater inflater = null;
    private final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    private SearchResults activity;
    private HashMap<Integer, String> bookNameLookupMap;
    private List<String> searchTokens;

    public ResultsAdapter(SearchResults context, List<Verses> verses) {
        super(context, R.layout.search_results_row, verses);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = context;
        populateSearchTokens();
        populateBookNameMap();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.search_results_row, null);
            holder = new ViewHolder();
            holder.bookName = (TextView) row
                    .findViewById(R.id.search_result_row_book);
            holder.chapterNumber = (TextView) row
                    .findViewById(R.id.search_result_row_chapter);
            holder.verseNumber = (TextView) row
                    .findViewById(R.id.search_result_row_verse_number);
            holder.verseText = (TextView) row
                    .findViewById(R.id.search_result_row_verse_text);
            row.setTag(holder);
        } else
            holder = (ViewHolder) row.getTag();
        final Verses entry = super.getItem(position);
        holder.bookName.setText(bookNameLookupMap.get(entry.bookId));
        holder.chapterNumber.setText("Chapter " + entry.chapter);
        holder.verseNumber.setText("[" + entry.number + "]");
        holder.verseText.setText(highlightSearchString(entry.text),
                BufferType.SPANNABLE);
        final String font = sharedPreferences.getString(getContext().getString(R.string.preference_font_face), getContext().getString(R.string.font_face_default_value));
        holder.bookName.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
        holder.chapterNumber.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
        holder.verseNumber.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
        holder.verseText.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
        return row;
    }

    @SuppressLint("DefaultLocale")
    private SpannableString highlightSearchString(final String verseText) {
        SpannableString formattedString = new SpannableString(verseText);
        for (String token : searchTokens) {
            int index = verseText.toUpperCase().indexOf(token);
            while (index != -1) {
                formattedString.setSpan(new ForegroundColorSpan(Color.RED),
                        index, index + token.length(), 0);
                index = verseText.toUpperCase().indexOf(token, index + 1);
            }
        }
        return formattedString;
    }

    private void populateSearchTokens() {
        searchTokens = new ArrayList<>();
        if (activity.method == Search.SEARCH_METHOD_EXACT_PHRASE) {
            searchTokens.add(activity.searchTerm.toUpperCase());
        } else {
            StringTokenizer tokenizer = new StringTokenizer(
                    activity.searchTerm, " ,.");
            while (tokenizer.hasMoreTokens()) {
                searchTokens.add(tokenizer.nextToken().toUpperCase());
            }
        }
    }

    private void populateBookNameMap() {
        bookNameLookupMap = new HashMap<>();
        List<Books> books = KJVLibrary.getBooks(activity.getContentResolver());
        for (Books book : books) {
            bookNameLookupMap.put(book.id, book.name);
        }
    }

    public static class ViewHolder {
        public TextView bookName;
        public TextView chapterNumber;
        public TextView verseNumber;
        public TextView verseText;
    }
}