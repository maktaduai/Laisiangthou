package com.zogamonline.laisiangthou.adapters.esv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.activities.esv.Bible;
import com.zogamonline.laisiangthou.items.esv.Books;
import com.zogamonline.laisiangthou.utils.Util;

import java.util.List;

/**
 * Bawl tu Maktaduai on 05-08-2015.
 */
@SuppressWarnings("deprecation")
@SuppressLint("InflateParams")
public class KJVListAdapter extends ArrayAdapter<Books> {
    private static LayoutInflater inflater = null;
    private final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    final Bible context;

    public KJVListAdapter(Bible context, List<Books> books) {
        super(context, R.layout.book_row_list, books);
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder {
        public TextView bookName;
        public ImageView bungSelectna;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.book_row_list, null);
            holder = new ViewHolder();
            holder.bookName = (TextView) row.findViewById(R.id.book_name);
            holder.bungSelectna = (ImageView)row.findViewById(R.id.book_chapter_selection);
            row.setTag(holder);
        } else
            holder = (ViewHolder) row.getTag();
        final Books entry = super.getItem(position);
        holder.bookName.setText(entry.name);
        final String font = sharedPreferences.getString(getContext().getString(R.string.preference_font_face), getContext().getString(R.string.font_face_default_value));
        holder.bookName.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        int color = typedValue.data;

        if (position > 38){
            holder.bookName.setTextColor(Color.parseColor("#FF8e44ad"));
        }else {
            holder.bookName.setTextColor(color);
        }

        holder.bungSelectna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.selectChapter(entry);
            }
        });

        return row;
    }
}
