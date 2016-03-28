package com.zogamonline.laisiangthou.items.esv;

import android.net.Uri;
import android.provider.BaseColumns;

import com.zogamonline.laisiangthou.providers.esv.KJVProvider;

import java.util.ArrayList;

public class Testaments implements BaseColumns {
    public static final Uri CONTENT_URI = Uri.parse(KJVProvider.CONTENT_URI
            + "/testaments");

    public static final String DEFAULT_SORT_ORDER = "id ASC";

    public static final String ID = "id";
    public static final String NAME = "Testament";

    public Integer id = null;
    public String name = null;
    public ArrayList<Books> books = null;

    public Testaments(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
}
