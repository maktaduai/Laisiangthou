package com.zogamonline.laisiangthou.activities.lst;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.activities.BaseActivity;
import com.zogamonline.laisiangthou.adapters.lst.VerseAdapter;
import com.zogamonline.laisiangthou.items.lst.Book;
import com.zogamonline.laisiangthou.items.lst.Bookmarks;
import com.zogamonline.laisiangthou.items.lst.Verse;
import com.zogamonline.laisiangthou.listeners.ActivitySwipeDetector;
import com.zogamonline.laisiangthou.notepad.NoteEditActivity;
import com.zogamonline.laisiangthou.providers.alui.LaisiangthouLibrary;

import java.util.List;
//Creator: Tuangoulal Phualte @Maktaduai

@SuppressWarnings({"deprecation", "unused", "StringConcatenationInsideStringBufferAppend", "InflateParams", "ConstantConditions"})
public class ChapterActivity extends BaseActivity {

    public static final String TITLE = "ChapterActivity.title";
    public static final String BOOK_ID = "ChapterActivity.book_id";
    public static final String CHAPTER = "ChapterActivity.chapter";
    public static final String VERSE = "ChapterActivity.verse";
    public static ListView chaangte;
    private String book;
    private int bookId;
    private int chapter;
    private int verse;
    private List<Verse> verses;
    private RelativeLayout mainLayout;
    int progress = 0;
    private static boolean isFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        this.book = getIntent().getStringExtra(TITLE);
        this.bookId = getIntent().getIntExtra(BOOK_ID, 1);
        this.chapter = getIntent().getIntExtra(CHAPTER, 1);
        this.verse = getIntent().getIntExtra(VERSE, 1);

        chaangte = (ListView) findViewById(R.id.chaangte);
        chaangte = (ListView) findViewById(R.id.chaangte);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        setupNavigation();
        loadChapter();
        configActionMode();
        configTouch();
        ConfigToolbar();
        setUpScreen();
    }

    private void setUpScreen() {
        chaangte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getSupportActionBar().show();
                if (Build.VERSION.SDK_INT >= 19) {
                    mainLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
                isFullScreen = false;
            }
        });
    }

    private void setupNavigation() {
        ImageButton navigationPrevious = (ImageButton) findViewById(R.id.navigation_previous);
        ImageButton navigationNext = (ImageButton) findViewById(R.id.navigation_next);
        navigationNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forward();
            }
        });
        navigationPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backward();
            }
        });
    }

    private void configTouch() {
        chaangte.setOnTouchListener(new ActivitySwipeDetector(this) {
            public void onSwipeLeft() {
                forward();
            }

            public void onSwipeRight() {
                backward();
            }
        });
    }

    private void configActionMode() {
        chaangte.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private int nr = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                ((VerseAdapter) chaangte.getAdapter()).notifyDataSetChanged();
                if (checked) {
                    nr++;
                } else {
                    nr--;
                }
                mode.setTitle(nr + " " + "tak" + " " + "pha");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                nr = 0;
                switch (item.getItemId()) {
                    case R.id.copy:
                        getSelectedVerse();
                        mode.finish();
                        return true;
                    case R.id.share:
                        SparseBooleanArray check = chaangte.getCheckedItemPositions();
                        StringBuilder shareVerses = new StringBuilder();
                        int sharecounts = 0;
                        for (int i = 0; i < check.size(); i++) {
                            if (check.valueAt(i)) {
                                int pos = check.keyAt(i);
                                sharecounts++;
                                Verse tang = verses.get(pos);
                                shareVerses.append("[").append(ChapterActivity.this.book).append(":").append(ChapterActivity.this.chapter).append(":").append(pos + 1).append("]").append(tang.text).append("\n").append("\n");
                            }
                            getSelectedVerse();
                            String shareText = shareVerses.toString();
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                            shareIntent.setType("text/plain");
                            startActivity(Intent.createChooser(shareIntent,
                                    "Hiaite zangin share in:"));
                        }
                        mode.finish();
                        return true;
                    case R.id.bookmark:
                        SparseBooleanArray checkte = chaangte.getCheckedItemPositions();
                        final StringBuilder bookmarkVerses = new StringBuilder();
                        int countteng = 0;
                        for (int i = 0; i < checkte.size(); i++) {
                            if (checkte.valueAt(i)) {
                                final int posi = checkte.keyAt(i);
                                countteng++;
                                final Verse taang = verses.get(posi);
                                bookmarkVerses.append("[" + ChapterActivity.this.book + ":" + ChapterActivity.this.chapter
                                        + ":" + (posi + 1) + "]" + taang.text + "\n");
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChapterActivity.this);
                                builder.setTitle(R.string.chiamtehin);
                                builder.setIcon(R.drawable.ic_action_archive);
                                builder.setMessage("Chiamtehin:" + " " + bookmarkVerses);
                                builder.setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int whichButton) {
                                                Bookmarks bookmarks = new Bookmarks(
                                                        ChapterActivity.this);
                                                bookmarks.addBookmark(taang.id);
                                                dialog.cancel();
                                            }
                                        });
                                builder.setNegativeButton(android.R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int whichButton) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.show();
                            }
                        }
                        mode.finish();
                        return true;
                    case R.id.add2Note:
                        getSelectedVerse();
                        Intent noteIntent = new Intent(ChapterActivity.this, NoteEditActivity.class);
                        startActivity(noteIntent);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
    }

    private void ConfigToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        drawerToggle.setDrawerIndicatorEnabled(false);
    }

    private void getSelectedVerse() {
        SparseBooleanArray checked = chaangte.getCheckedItemPositions();
        StringBuilder copyVerses = new StringBuilder();
        int count = 0;
        for (int i = 0; i < checked.size(); i++) {
            if (checked.valueAt(i)) {
                int position = checked.keyAt(i);
                count++;
                Verse chang = verses.get(position);
                copyVerses.append("[" + ChapterActivity.this.book + ":" + ChapterActivity.this.chapter
                        + ":" + (position + 1) + "]" + chang.text + "\n" + "\n");
            }
            String text = copyVerses.toString();
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("copyVerse", text);
            clipboardManager.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Na chaangtel copy khin", Toast.LENGTH_SHORT).show();
        }
    }

    private void backward() {
        if (this.bookId == 1 && this.chapter == 1) {
            Toast.makeText(this, "Thukhun Lui bul ahi hiai", Toast.LENGTH_SHORT)
                    .show();
        } else {
            if (this.chapter > 1) {
                this.chapter--;
                loadChapter();
            } else {
                this.bookId--;
                final Book previousBook = LaisiangthouLibrary.getBook(
                        getContentResolver(), this.bookId);
                if (previousBook != null) {
                    this.book = previousBook.name;
                }
                this.chapter = LaisiangthouLibrary.getChapterCount(
                        getContentResolver(), previousBook);
                loadChapter();

            }
        }
    }

    private void forward() {
        if (this.bookId == 66 && this.chapter == 22) {
            Toast.makeText(this, "Thukhun Lui tawpna ahi hiai",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        int chapterCount = LaisiangthouLibrary.getChapterCount(
                getContentResolver(), this.bookId);
        if (this.chapter == chapterCount) {
            this.bookId++;
            this.chapter = 1;
            final Book nextBook = LaisiangthouLibrary.getBook(
                    getContentResolver(), this.bookId);
            if (nextBook != null) {
                this.book = nextBook.name;
            }
            loadChapter();
        } else {
            this.chapter++;
            loadChapter();
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadChapter() {
        if (this.book != null)
            this.setTitle(this.book);
        TextView header = (TextView) findViewById(R.id.chapter_heading);
        header.setText(this.book + " " + this.chapter);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String font = sharedPreferences.getString(this.getString(R.string.preference_font_face),
                this.getString(R.string.font_face_default_value));
        header.setTypeface(Typeface.createFromAsset(this.getAssets(), font));
        this.verses = LaisiangthouLibrary.getVerses(getContentResolver(),
                this.bookId, this.chapter);
        chaangte = (ListView) findViewById(R.id.chaangte);
        VerseAdapter adapter = new VerseAdapter(this, this.verses);
        chaangte.setAdapter(adapter);
        if (this.verse != 0)
            chaangte.setSelection(this.verse - 1);
        chaangte.setSelected(true);
        String toast = (this.book != null) ? (this.book + " " + this.chapter)
                : (" " + this.chapter);
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chapter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                break;
            case R.id.bungte:
                selectChapter();
                break;
            case R.id.change_font_menu_item:
                changeFont();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeFont() {
        final float MIN_FONT_SIZE = 14;
        final float MAX_FONT_SIZE = 26;

        chaangte = (ListView) findViewById(R.id.chaangte);
        final VerseAdapter adapter = (VerseAdapter) chaangte.getAdapter();
        final float originalFontSize = adapter.getFontSize();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View fontKhekna = getLayoutInflater().inflate(R.layout.dialog_font, null);
        TextView bungTitle = (TextView) fontKhekna.findViewById(R.id.dialog_font_title);
        bungTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_settings, 0, 0);
        bungTitle.setText(R.string.font_khekna);
        builder.setCustomTitle(fontKhekna);

        View view = getLayoutInflater().inflate(R.layout.change_font, null);
        final SeekBar sizerBar = (SeekBar) view
                .findViewById(R.id.change_font_seekbar);
        final SeekBar brightbar = (SeekBar) view.findViewById(R.id.brightbar);
        brightbar.setMax(255);
        final CheckBox themeToggle = (CheckBox) view.findViewById(R.id.themeToggle);
        themeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getSupportActionBar().hide();
                    if (Build.VERSION.SDK_INT >= 19) {
                        mainLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    }
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getSupportActionBar().show();
                    if (Build.VERSION.SDK_INT >= 19) {
                        mainLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            }
        });

        float curBrightnessValue = 0;
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(
                    getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        final int screen_brightness = (int) curBrightnessValue;
        brightbar.setProgress(screen_brightness);

        sizerBar.setMax((int) (MAX_FONT_SIZE - MIN_FONT_SIZE));
        sizerBar.setProgress((int) (originalFontSize - MIN_FONT_SIZE));
        sizerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                adapter.setFontSize(progress + MIN_FONT_SIZE);
                adapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(getBaseContext(), String.valueOf(progress + MIN_FONT_SIZE), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.START | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }

        });
        brightbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {

                progress = progressValue;

                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.screenBrightness = progress;
                getWindow().setAttributes(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                android.provider.Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        progress);
            }
        });

        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences prefs = ChapterActivity.this
                        .getSharedPreferences("VerseAdapter", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat(VerseAdapter.FONT_PREFERENCE,
                        sizerBar.getProgress() + MIN_FONT_SIZE);
                editor.apply();

            }
        });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.setFontSize(originalFontSize);
                        adapter.notifyDataSetChanged();
                        builder.setCancelable(true);
                    }
                });
        builder.show();
    }

    @SuppressLint("SetTextI18n")
    public void selectChapter() {
        final Book book = LaisiangthouLibrary.getBook(getContentResolver(),
                this.bookId);
        int count = LaisiangthouLibrary.getChapterCount(getContentResolver(),
                book);
        final String[] chapterNames = new String[count];
        for (int i = 0; i < count; i++) {
            chapterNames[i] = " " + (i + 1);
        }
        View chapterListView = LayoutInflater.from(ChapterActivity.this)
                .inflate(R.layout.bung_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(ChapterActivity.this).create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialog_Animation;
        dialog.setView(chapterListView);
        View bungte = getLayoutInflater().inflate(R.layout.dialog_bung, null);
        TextView bungTitle = (TextView) bungte.findViewById(R.id.dialog_bung_title);
        bungTitle.setText(this.book + " " + "Laibu");
        bungTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.book, 0, 0);
        dialog.setCustomTitle(bungte);
        final GridView gridView = (GridView) chapterListView
                .findViewById(R.id.bungList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ChapterActivity.this, R.layout.list_chapter, chapterNames);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                ChapterActivity.this.chapter = position + 1;
                loadChapter();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    private void checkPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("notifications_new_message", false)) {
            getWindow()
                    .addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}
