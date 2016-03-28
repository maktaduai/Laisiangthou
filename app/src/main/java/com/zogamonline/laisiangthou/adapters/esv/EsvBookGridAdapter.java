package com.zogamonline.laisiangthou.adapters.esv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.items.esv.Books;

import java.util.List;

/**
 * Creator: Maktaduai on 2/19/2016.
 */
@SuppressLint("InflateParams")
public class EsvBookGridAdapter extends ArrayAdapter<Books>{

    private static LayoutInflater inflater = null;
    private final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

    public EsvBookGridAdapter(FragmentActivity thukhunluiFragment, List<Books> books) {
        super(thukhunluiFragment, R.layout.book_row_grid, books);
        inflater = (LayoutInflater) thukhunluiFragment
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder {
        public TextView bookName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.book_row_grid, null);
            holder = new ViewHolder();
            holder.bookName = (TextView) row.findViewById(R.id.book_name);
            row.setTag(holder);
        } else
            holder = (ViewHolder) row.getTag();
        final Books entry = super.getItem(position);
        String booknameTrimmed = entry.name.replaceAll("\\s+", "");
        holder.bookName.setText(booknameTrimmed.substring(0, 3));

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

        return row;
    }
}
