package com.zogamonline.laisiangthou.adapters.common;

import android.annotation.SuppressLint;
import android.app.Activity;
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

/**
 * Creator: Maktaduai on 05-08-2015.
 */
@SuppressLint("InflateParams")
public class GridAdapter extends ArrayAdapter<String> {
    private static LayoutInflater inflater = null;

    public GridAdapter(Activity context, String[] chapters) {
        super(context, R.layout.list_chapter_grid, chapters);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public static class ViewHolder {
        public TextView bungNo;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.list_chapter_grid, null);
            holder = new ViewHolder();
            holder.bungNo = (TextView) row.findViewById(R.id.bung_no);
            row.setTag(holder);
        }else
            holder = (ViewHolder) row.getTag();
        final String entry = super.getItem(position);
        holder.bungNo.setText(entry);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String font = sharedPreferences.getString(getContext().getString(R.string.preference_font_face), getContext().getString(R.string.font_face_default_value));
        holder.bungNo.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
        return row;
    }
}
