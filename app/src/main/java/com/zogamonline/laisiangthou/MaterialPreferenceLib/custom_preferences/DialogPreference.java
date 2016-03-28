/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zogamonline.laisiangthou.MaterialPreferenceLib.custom_preferences;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zogamonline.laisiangthou.R;

public abstract class DialogPreference extends Preference implements
        DialogInterface.OnClickListener, DialogInterface.OnDismissListener,
        PreferenceManager.OnActivityDestroyListener {
    private AlertDialog.Builder mBuilder;

    private CharSequence mDialogTitle;
    private CharSequence mDialogMessage;
    private Drawable mDialogIcon;
    private CharSequence mPositiveButtonText;
    private CharSequence mNegativeButtonText;
    private int mDialogLayoutResId;
    private Dialog mDialog;
    private int mWhichButtonClicked;

    public DialogPreference(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DialogPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public DialogPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void init(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super.init(context, attrs, defStyleAttr, defStyleRes);
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DialogPreference, defStyleAttr, defStyleRes);
        mDialogTitle = a.getString(R.styleable.DialogPreference_dialogTitle);
        if (mDialogTitle == null) {
// Fallback on the regular title of the preference
// (the one that is seen in the list)
            mDialogTitle = getTitle();
        }

        mDialogMessage = a.getString(R.styleable.DialogPreference_dialogMessage);
        mDialogIcon = a.getDrawable(R.styleable.DialogPreference_dialogIcon);
        mPositiveButtonText = a.getString(R.styleable.DialogPreference_positiveButtonText);
        mNegativeButtonText = a.getString(R.styleable.DialogPreference_negativeButtonText);
        mDialogLayoutResId = a.getResourceId(R.styleable.DialogPreference_dialogLayout, mDialogLayoutResId);
        a.recycle();
    }

    public void setDialogTitle(CharSequence dialogTitle) {
        mDialogTitle = dialogTitle;
    }

    public CharSequence getDialogTitle() {
        return mDialogTitle;
    }

    public void setDialogTitle(int dialogTitleResId) {
        setDialogTitle(getContext().getString(dialogTitleResId));
    }

    public void setDialogMessage(CharSequence dialogMessage) {
        mDialogMessage = dialogMessage;
    }

    public CharSequence getDialogMessage() {
        return mDialogMessage;
    }

    public void setDialogMessage(int dialogMessageResId) {
        setDialogMessage(getContext().getString(dialogMessageResId));
    }

    public void setDialogIcon(Drawable dialogIcon) {
        mDialogIcon = dialogIcon;
    }


    public Drawable getDialogIcon() {
        return mDialogIcon;
    }

    public void setDialogIcon(int dialogIconRes) {
        mDialogIcon = ResourcesCompat.getDrawable(getContext().getResources(), dialogIconRes, null);
    }

    public void setPositiveButtonText(CharSequence positiveButtonText) {
        mPositiveButtonText = positiveButtonText;
    }

    public CharSequence getPositiveButtonText() {
        return mPositiveButtonText;
    }

    public void setPositiveButtonText(int positiveButtonTextResId) {
        setPositiveButtonText(getContext().getString(positiveButtonTextResId));
    }

    public void setNegativeButtonText(CharSequence negativeButtonText) {
        mNegativeButtonText = negativeButtonText;
    }

    public CharSequence getNegativeButtonText() {
        return mNegativeButtonText;
    }

    public void setNegativeButtonText(int negativeButtonTextResId) {
        setNegativeButtonText(getContext().getString(negativeButtonTextResId));
    }

    public int getDialogLayoutResource() {
        return mDialogLayoutResId;
    }

    public void setDialogLayoutResource(int dialogLayoutResId) {
        mDialogLayoutResId = dialogLayoutResId;
    }

    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
    }

    @Override
    protected void onClick() {
        if (mDialog != null && mDialog.isShowing()) return;

        showDialog(null);
    }

    protected void showDialog(Bundle state) {
        Context context = getContext();

        mWhichButtonClicked = DialogInterface.BUTTON_NEGATIVE;
        mBuilder = new AlertDialog.Builder(context)
                .setTitle(mDialogTitle)
                .setIcon(mDialogIcon)
                .setPositiveButton(mPositiveButtonText, this)
                .setNegativeButton(mNegativeButtonText, this);
        View contentView = onCreateDialogView();
        if (contentView != null) {
            onBindDialogView(contentView);
            mBuilder.setView(contentView);
        } else {
            mBuilder.setMessage(mDialogMessage);
        }

        onPrepareDialogBuilder(mBuilder);
        PreferenceManagerEx.getInstance().registerOnActivityDestroyListener(getPreferenceManager(), this);

// Create the dialog
        final Dialog dialog = mDialog = mBuilder.create();
        if (state != null) {
            dialog.onRestoreInstanceState(state);
        }
        if (needInputMethod()) {
            requestInputMethod(dialog);
        }
        dialog.setOnDismissListener(this);
        dialog.show();
    }

    protected boolean needInputMethod() {
        return false;
    }

    private void requestInputMethod(Dialog dialog) {
        Window window = dialog.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    protected View onCreateDialogView() {
        if (mDialogLayoutResId == 0) {
            return null;
        }

        LayoutInflater inflater = LayoutInflater.from(mBuilder.getContext());
        return inflater.inflate(mDialogLayoutResId, null);
    }

    protected void onBindDialogView(View view) {
        View dialogMessageView = view.findViewById(android.R.id.message);

        if (dialogMessageView != null) {
            final CharSequence message = getDialogMessage();
            int newVisibility = View.GONE;

            if (!TextUtils.isEmpty(message)) {
                if (dialogMessageView instanceof TextView) {
                    ((TextView) dialogMessageView).setText(message);
                }

                newVisibility = View.VISIBLE;
            }

            if (dialogMessageView.getVisibility() != newVisibility) {
                dialogMessageView.setVisibility(newVisibility);
            }
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        mWhichButtonClicked = which;
    }

    public void onDismiss(DialogInterface dialog) {
        PreferenceManagerEx.getInstance().unregisterOnActivityDestroyListener(getPreferenceManager(), this);

        mDialog = null;
        onDialogClosed(mWhichButtonClicked == DialogInterface.BUTTON_POSITIVE);
    }

    protected void onDialogClosed(boolean positiveResult) {
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public void onActivityDestroy() {

        if (mDialog == null || !mDialog.isShowing()) {
            return;
        }

        mDialog.dismiss();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (mDialog == null || !mDialog.isShowing()) {
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.isDialogShowing = true;
        myState.dialogBundle = mDialog.onSaveInstanceState();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
// Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        if (myState.isDialogShowing) {
            showDialog(myState.dialogBundle);
        }
    }

    private static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        boolean isDialogShowing;
        Bundle dialogBundle;

        public SavedState(Parcel source) {
            super(source);
            isDialogShowing = source.readInt() == 1;
            dialogBundle = source.readBundle();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(isDialogShowing ? 1 : 0);
            dest.writeBundle(dialogBundle);
        }
    }
}
