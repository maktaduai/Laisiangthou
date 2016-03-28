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
 * Bawl tu Maktaduai on 10-09-2015.
 */
public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about, container, false);

        TextView alupen = (TextView) rootView.findViewById(R.id.alupen);
        TextView about_text = (TextView) rootView.findViewById(R.id.about_text);
        TextView about_copyright = (TextView) rootView.findViewById(R.id.about_copyright);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String font = sharedPreferences.getString(getContext().getString(R.string.preference_font_face), getContext().getString(R.string.font_face_default_value));
        alupen.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
        about_text.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
        about_copyright.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Pacifico.ttf"));
        return rootView;
    }
}
