package com.zogamonline.laisiangthou.adapters.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zogamonline.laisiangthou.R;

/**
 * Creator: Maktaduai on 2/8/2016.
 */
public class ThemeAdapter extends ArrayAdapter<String>{

    private final Activity mContext;
    private final String[] mThemename;
    private final Integer[] mImgid;

    public ThemeAdapter(Activity context, String[] themename, Integer[] imgid){
        super(context, R.layout.list_theme, themename);

        this.mContext = context;
        this.mThemename = themename;
        this.mImgid = imgid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(R.layout.list_theme, null, true);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.theme_icon);
        TextView textView = (TextView)rowView.findViewById(R.id.theme_text);

        imageView.setImageResource(mImgid[position]);
        textView.setText(mThemename[position]);
        return rowView;
    }
}
