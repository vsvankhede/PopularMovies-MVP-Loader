package com.vstechlab.popularmovies.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.vstechlab.popularmovies.data.Movie;
import com.vstechlab.popularmovies.data.source.MoviesDataSource;
import com.vstechlab.popularmovies.data.source.local.MoviesPersistancContract.MovieEntry;

import java.util.ArrayList;
import java.util.List;

public class MoviesLocalDataSource implements MoviesDataSource {
    private static MoviesLocalDataSource INSTANCE;

    private MoviesDbHelper mDbHelper;

    private SQLiteDatabase mDb;

    private MoviesLocalDataSource(@NonNull Context context) {
        mDbHelper = new MoviesDbHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    public static MoviesLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public List<Movie> getMovies() {
        List<Movie> movies = new ArrayList<>();
        try {
            String[] projection = {
                    MovieEntry.COLUMN_ID,
                    MovieEntry.COLUMN_TITLE,
                    MovieEntry.COLUMN_POSTER

            };
            Cursor c = mDb.query(MovieEntry.TABLE_NAME, projection, null, null, null, null, null);

            if (c != null && c.getCount() > 0) {
                while(c.moveToNext()) {
                    int movieId = c.getInt(c.getColumnIndex(MovieEntry.COLUMN_ID));
                    String title = c.getString(c.getColumnIndex(MovieEntry.COLUMN_TITLE));
                    String poster = c.getString(c.getColumnIndex(MovieEntry.COLUMN_POSTER));

                    Movie movie = new Movie(movieId, title, poster);
                    movies.add(movie);
                }
            }
            if (c != null) {
                c.close();
            }
        } catch (IllegalStateException e) {

        }

        return movies;
    }

    @Override
    public void saveMovie(Movie movies) {
        try {
            ContentValues values = new ContentValues();
            values.put(MovieEntry.COLUMN_ID, movies.getId());
            values.put(MovieEntry.COLUMN_TITLE, movies.getTitle());
            values.put(MovieEntry.COLUMN_POSTER, movies.getPosterPath());

            mDb.insert(MovieEntry.TABLE_NAME, null, values);
        } catch (IllegalStateException e) {

        }
    }
}
