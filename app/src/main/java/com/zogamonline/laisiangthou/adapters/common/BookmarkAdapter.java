package com.zogamonline.laisiangthou.adapters.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zogamonline.laisiangthou.R;

/**
 * Bawl tu Maktaduai on 05-08-2015.
 */
@SuppressLint("InflateParams")
public class BookmarkAdapter extends ArrayAdapter<String> {
    private static LayoutInflater inflater = null;
    private final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

    public BookmarkAdapter(FragmentActivity activity, String[] bookmark) {
        super(activity, R.layout.bookmark_row, bookmark);
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.bookmark_row, null);
            holder = new ViewHolder();
            holder.bookName = (TextView) row.findViewById(R.id.bookmark_name);
            holder.animation = (ImageView)row.findViewById(R.id.kaImage);
            row.setTag(holder);
        } else
            holder = (ViewHolder) row.getTag();
        final String entry = super.getItem(position);
        holder.bookName.setText(entry);
        holder.animation.setAnimation(AnimationUtils.loadAnimation(getContext(), (R.anim.animation)));
        final String font = sharedPreferences.getString(getContext().getString(R.string.preference_font_face), getContext().getString(R.string.font_face_default_value));
        holder.bookName.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
        return row;
    }

    public static class ViewHolder {
        public TextView bookName;
        public ImageView animation;
    }
}
