package com.sample.contentproviderexample;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

public class MyProvider extends ContentProvider {
    SQLiteDatabase mdb;

    static final Uri CONTENT_URI = Uri.parse("content://com.sample.contentproviderexample/world");
    static final int ALLDATA = 1;
    static final int ONEDATA = 2;

    static final UriMatcher Matcher;
    static {
        Matcher = new UriMatcher(UriMatcher.NO_MATCH);
        Matcher.addURI("com.sample.contentproviderexample", "world", ALLDATA);
        Matcher.addURI("com.sample.contentproviderexample", "world/*", ONEDATA);
    }

    public MyProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int count = 0;
        switch(Matcher.match(uri)) {
            case ALLDATA:
                count = mdb.delete("world", selection, selectionArgs);
                break;
            case ONEDATA:
                String where;
                where = "country = '" + uri.getPathSegments().get(1) + "'";
                if(!TextUtils.isEmpty(selection)) {
                    where += " AND" + selection;
                }
                count = mdb.delete("world", where, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
//        throw new UnsupportedOperationException("Not yet implemented");
        if(Matcher.match(uri) == ONEDATA) {
            return "vnd.Jenn.cursor.item/country";
        }
        else if(Matcher.match(uri) == ALLDATA) {
            return "cnd.Jenn.cursor.dir/world";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
//        throw new UnsupportedOperationException("Not yet implemented");
        long row = mdb.insert("world", null, values);
        if(row > 0) {
            Uri result_uri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(result_uri, null);
            return result_uri;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        MyDBOpenHelper helper = new MyDBOpenHelper(getContext());
        mdb = helper.getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
//        throw new UnsupportedOperationException("Not yet implemented");
        String sql = "SELECT * FROM WORLD";
        if(Matcher.match(uri) == ONEDATA) {
            sql += " WHERE country = '" + uri.getPathSegments().get(1) + "'";
        }
        return mdb.rawQuery(sql, null);
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
//        throw new UnsupportedOperationException("Not yet implemented");
        mdb.update("world", values, selection, selectionArgs);
        return 0;
    }

}


class MyDBOpenHelper extends SQLiteOpenHelper {

    public MyDBOpenHelper(Context context) {
        super(context, "mydb.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE world (_id INTEGER PRIMARY KEY AUTOINCREMENT, country  TEXT, capital TEXT);");
        sqLiteDatabase.execSQL("INSERT INTO world VALUES (null, 'korea', 'seoul');");
        sqLiteDatabase.execSQL("INSERT INTO world VALUES (null, 'france', 'paris');");
        sqLiteDatabase.execSQL("INSERT INTO world VALUES (null, 'japan', 'tokyo');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}