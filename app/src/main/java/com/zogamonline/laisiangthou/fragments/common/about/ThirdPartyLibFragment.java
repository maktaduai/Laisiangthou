package com.zogamonline.laisiangthou.fragments.common.about;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zogamonline.laisiangthou.R;

/**
 * Author: Maktaduai on 10-09-2015.
 */
public class ThirdPartyLibFragment extends Fragment {

    TextView header, lib1, lib2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2, container, false);

        header = (TextView)rootView.findViewById(R.id.header);
        lib1 = (TextView)rootView.findViewById(R.id.lib1);
        lib2 = (TextView)rootView.findViewById(R.id.lib2);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String font = sharedPreferences.getString(getContext().getString(R.string.preference_font_face), getContext().getString(R.string.font_face_default_value));
        header.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
        lib1.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
        lib2.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));

        return rootView;
    }
}
