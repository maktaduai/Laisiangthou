package com.zogamonline.laisiangthou.providers.esv;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.zogamonline.laisiangthou.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Creator Maktaduai on 18-09-2015.
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "unused"})
public class KjvDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "esv.db";
    private final Context context;
    private SQLiteDatabase database;

    public KjvDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public String databasePath() {
        return context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
    }

    public void createDatabase() throws IOException {
        boolean exists = checkDatabase();
        if (!exists) {
            database = this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
            if (database != null) {
                database.close();
                database = null;
            }
        }
    }

    public boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String path = databasePath();
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // Database doesn't exist
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return (checkDB != null);
    }

    private void copyDatabase() throws IOException {
        String destination = databasePath();
        OutputStream output = new FileOutputStream(destination);
        /*ZipInputStream zipInputStream = getFileFromZip(context.getResources().openRawResource(R.raw.esv));
        if (zipInputStream == null){
            throw new SQLiteAssetException("Archive khat omlou");
        }
        byte[] buffer = new byte[1024];
        int length;
        while ((length = zipInputStream.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }*/
        int[] resources = new int[]{R.raw.eaa, R.raw.eab, R.raw.eac, R.raw.ead, R.raw.eae, R.raw.eaf};
        for (int resource : resources) {
            InputStream input = context.getResources().openRawResource(resource);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            input.close();
        }
        output.close();
    }
    /*private ZipInputStream getFileFromZip(InputStream inputStream) throws IOException{
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        if (zipEntry != null){
            return zipInputStream;
        }else {
            return null;
        }
    }*/
    public SQLiteDatabase openDatabase() throws SQLException {
        String path = databasePath();
        database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        return database;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }
    /*public class SQLiteAssetException extends SQLiteException {
        private static final long serialVersionUID = 1L;
        public SQLiteAssetException() {
        }
        public SQLiteAssetException(String s) {
            super(s);
        }
    }*/
}