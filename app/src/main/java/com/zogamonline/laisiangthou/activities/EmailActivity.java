package com.zogamonline.laisiangthou.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zogamonline.laisiangthou.R;

/**
 * Creator: Maktaduai on 18-10-2015.
 */
public class EmailActivity extends BaseActivity {

    private EditText textTo;
    private EditText textSubject;
    private EditText textMessage;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        checkPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        drawerToggle.setDrawerIndicatorEnabled(false);

        TextView title = (TextView) findViewById(R.id.contact_form_title);
        textTo = (EditText) findViewById(R.id.editTextTo);
        textSubject = (EditText) findViewById(R.id.textSubject);
        textMessage = (EditText) findViewById(R.id.textMessage);
        textMessage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                /*if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    sendEmail();
                }*/

                if ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(
                            textMessage.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        textTo.setKeyListener(null);
        textTo.setVisibility(View.GONE);
        textSubject.setKeyListener(null);
        textSubject.setVisibility(View.GONE);
        Button imgSend = (Button) findViewById(R.id.imgSend);
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(textMessage.getText().toString())) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Na message ding type phot ou!",
                            Toast.LENGTH_SHORT);
                    TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                    if (null != tv) {
                        tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning, 0, 0, 0);
                        tv.setCompoundDrawablePadding(getApplicationContext().getResources().getDimensionPixelSize(R.dimen.toast_padding));
                        toast.show();
                    }
                } else {
                    sendEmail();
                }
            }
        });

        final String font = sharedPreferences.getString(this.getString(R.string.preference_font_face),
                this.getString(R.string.font_face_default_value));
        textTo.setTypeface(Typeface.createFromAsset(this.getAssets(), font));
        textMessage.setTypeface(Typeface.createFromAsset(this.getAssets(), font));
        textSubject.setTypeface(Typeface.createFromAsset(this.getAssets(), font));
        title.setTypeface(Typeface.createFromAsset(this.getAssets(), font));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
        String to = textTo.getText().toString();
        String subject = textSubject.getText().toString();
        String message = textMessage.getText().toString();

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
        //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);
        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Email app khat teel in :"));
    }

    private void checkPreferences() {
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("notifications_new_message", false)) {
            getWindow()
                    .addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
}
