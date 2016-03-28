package com.zogamonline.laisiangthou.MaterialPreferenceLib.custom_preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;

import com.zogamonline.laisiangthou.R;

public class SwitchPreference extends TwoStatePreference {
    private final Listener mListener = new Listener();

    // Switch text for on and off states
    private CharSequence mSwitchOn;
    private CharSequence mSwitchOff;

    public SwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwitchPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.switchPreferenceStyle);
    }

    public SwitchPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void init(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super.init(context, attrs, defStyleAttr, defStyleRes);
        this.setWidgetLayoutResource(R.layout.mpl__switch_preference);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SwitchPreference, defStyleAttr, defStyleRes);
        setSummaryOn(a.getString(R.styleable.SwitchPreference_summaryOn));
        setSummaryOff(a.getString(R.styleable.SwitchPreference_summaryOff));
        setSwitchTextOn(a.getString(
                R.styleable.SwitchPreference_switchTextOn));
        setSwitchTextOff(a.getString(
                R.styleable.SwitchPreference_switchTextOff));
        setDisableDependentsState(a.getBoolean(
                R.styleable.SwitchPreference_disableDependentsState, false));
        a.recycle();
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        View checkableView = view.findViewById(android.R.id.checkbox);
        if (checkableView != null && checkableView instanceof Checkable) {
            if (checkableView instanceof SwitchCompat) {
                final SwitchCompat switchView = (SwitchCompat) checkableView;
                switchView.setOnCheckedChangeListener(null);
            }

            ((Checkable) checkableView).setChecked(mChecked);

            if (checkableView instanceof SwitchCompat) {
                final SwitchCompat switchView = (SwitchCompat) checkableView;
                switchView.setTextOn(mSwitchOn);
                switchView.setTextOff(mSwitchOff);
                switchView.setOnCheckedChangeListener(mListener);
            }
        }

        syncSummaryView(view);
    }

    public void setSwitchTextOn(CharSequence onText) {
        mSwitchOn = onText;
        notifyChanged();
    }

    public void setSwitchTextOff(CharSequence offText) {
        mSwitchOff = offText;
        notifyChanged();
    }

    public CharSequence getSwitchTextOn() {
        return mSwitchOn;
    }

    public void setSwitchTextOn(int resId) {
        setSwitchTextOn(getContext().getString(resId));
    }

    public CharSequence getSwitchTextOff() {
        return mSwitchOff;
    }

    public void setSwitchTextOff(int resId) {
        setSwitchTextOff(getContext().getString(resId));
    }

    private class Listener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!callChangeListener(isChecked)) {
// Listener didn't like it, change it back.
// CompoundButton will make sure we don't recurse.
                buttonView.setChecked(!isChecked);
                return;
            }

            SwitchPreference.this.setChecked(isChecked);
        }
    }
}