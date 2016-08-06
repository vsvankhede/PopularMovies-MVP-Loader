package com.vstechlab.popularmovies.data.source.local;

import android.provider.BaseColumns;

public final class MoviesPersistancContract {
    public MoviesPersistancContract() {}

    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
    }

}
