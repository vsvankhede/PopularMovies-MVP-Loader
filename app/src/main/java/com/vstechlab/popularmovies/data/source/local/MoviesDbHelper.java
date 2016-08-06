package com.vstechlab.popularmovies.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vstechlab.popularmovies.data.source.local.MoviesPersistancContract.MovieEntry;

public class MoviesDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "Movies.db";

    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";

    private static final String NUMBER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_MOVIE =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    MovieEntry.COLUMN_ID + NUMBER_TYPE + COMMA_SEP +
                    MovieEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    MovieEntry.COLUMN_POSTER + TEXT_TYPE + COMMA_SEP +
            ")";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
