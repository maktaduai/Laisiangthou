package com.zogamonline.laisiangthou.adapters.esv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.items.esv.Verses;

import java.util.List;

@SuppressLint("InflateParams")
public class VerseAdapters extends ArrayAdapter<Verses> {

    public static final String FONT_PREFERENCE = "VerseAdapter.font";
    private static LayoutInflater inflater = null;
    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    @SuppressLint("UseSparseArrays")
    private float verseFontSize = 16.0f;

    public VerseAdapters(Context context, List<Verses> verses) {
        super(context, R.layout.verse_row, verses);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        SharedPreferences prefs = context.getSharedPreferences("VerseAdapter",
                0);
        if (prefs.contains(FONT_PREFERENCE)) {
            verseFontSize = prefs.getFloat(FONT_PREFERENCE, 16.0f);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.verse_row, null);
            holder = new ViewHolder();
            holder.verseNumber = (TextView) row.findViewById(R.id.verse_number);
            holder.verseText = (TextView) row.findViewById(R.id.verse_text);

            row.setTag(holder);
        } else
            holder = (ViewHolder) row.getTag();
        final Verses entry = super.getItem(position);
        holder.verseNumber.setText("[" + entry.number + "]");
        holder.verseText.setText(entry.text);
        final String font = sharedPreferences.getString(getContext().getString(R.string.preference_font_face), getContext().getString(R.string.font_face_default_value));
        holder.verseText.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
        if (verseFontSize > 0) {
            holder.verseText.setTextSize(verseFontSize);
        }
        return row;
    }

    public float getFontSize() {
        if (verseFontSize > 0) {
            return verseFontSize;
        } else {
            View row = inflater.inflate(R.layout.verse_row, null);
            return ((TextView) row.findViewById(R.id.verse_text)).getTextSize();
        }
    }

    public void setFontSize(final float fontSize) {
        this.verseFontSize = fontSize;
    }

    public static class ViewHolder {
        public TextView verseNumber;
        public TextView verseText;
    }
}