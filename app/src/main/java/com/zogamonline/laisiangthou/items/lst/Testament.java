package com.zogamonline.laisiangthou.items.lst;

import android.net.Uri;
import android.provider.BaseColumns;

import com.zogamonline.laisiangthou.providers.alui.LaisiangthouProvider;

import java.util.ArrayList;

/**
 * Creator Maktaduai on 05-08-2015.
 */
public class Testament implements BaseColumns {
    public static final Uri CONTENT_URI = Uri
            .parse(LaisiangthouProvider.CONTENT_URI + "/testaments");

    public static final String DEFAULT_SORT_ORDER = "id ASC";

    public static final String ID = "id";
    public static final String NAME = "Testament";

    public Integer id = null;
    public String name = null;
    public ArrayList<Book> books = null;

    public Testament(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
}
