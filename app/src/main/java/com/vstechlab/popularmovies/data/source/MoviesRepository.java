package com.vstechlab.popularmovies.data.source;

import android.support.annotation.NonNull;

import com.vstechlab.popularmovies.data.Movie;
import com.vstechlab.popularmovies.data.Movies;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MoviesRepository implements MoviesDataSource{
    private static MoviesRepository INSTANCE = null;

    private final MoviesDataSource mMoviesLocalDataSource;
    private final MoviesDataSource mMoviesRemoteDataSource;

    private List<MoviesRepositoryObserver> mObserver = new ArrayList<MoviesRepositoryObserver>();

    Map<Integer, Movie> mCachedMovies;

    boolean mCacheIsDirty;
    private List<Movie> cachedMovies;

    public static MoviesRepository getInstance(MoviesDataSource moviesLocalDataSource,
                                               MoviesDataSource moviesRemoteDatasource) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository(moviesLocalDataSource, moviesRemoteDatasource);
        }
        return INSTANCE;
    }

    public void destroyInstance() {
        INSTANCE = null;
    }

    private MoviesRepository(@NonNull MoviesDataSource moviesLocalDataSource,
                             @NonNull MoviesDataSource moviesRemoteDataSource) {
        mMoviesLocalDataSource = moviesLocalDataSource;
        mMoviesRemoteDataSource = moviesRemoteDataSource;
    }

    public void addContentObserver(MoviesRepositoryObserver observer) {
        if (!mObserver.contains(observer)) {
            mObserver.add(observer);
        }
    }

    public void removeContentObserver(MoviesRepositoryObserver observer) {
        if (mObserver.contains(observer)) {
            mObserver.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for (MoviesRepositoryObserver observer : mObserver) {
            observer.onMoviesChanged();
        }
    }

    /**
     * Get movies from cache, SQLite or remote data source, whichever is
     * available first. This is done synchronously because it's used by the
     * MoviesLoader which implements async mechanism.
     */
    @Override
    public List<Movie> getMovies() {
        List<Movie> movies = null;

        if (!mCacheIsDirty) {
            if (mCachedMovies != null) {
                movies = getCachedMovies();
                return movies;
            } else {
                // Query local data storage for available movies
                movies = mMoviesLocalDataSource.getMovies();
            }
        }

        // If movies in local storage not available get it from remote data storage.
        if (movies == null || movies.isEmpty()) {
            movies = mMoviesRemoteDataSource.getMovies();
            // Copy the data to the device so we don't need to query the network next time
            saveMoviesInLocalDataSource(movies);
        }

        processLoadedMovies(movies);
        return getCachedMovies();
    }

    public boolean cachedMovieAvailable() {
        return mCachedMovies != null && !mCacheIsDirty;
    }

    private void processLoadedMovies(List<Movie> movies) {
        if (movies == null) {
            mCachedMovies = null;
            mCacheIsDirty = false;
            return;
        }

        if (mCachedMovies == null) {
            mCachedMovies = new LinkedHashMap<>();
        }
        mCachedMovies.clear();
        for (Movie movie : movies) {
            mCachedMovies.put(movie.getId(), movie);
        }
        mCacheIsDirty = false;
    }

    @Override
    public void saveMovie(Movie movie) {
        // Todo later
    }

    private void saveMoviesInLocalDataSource(List<Movie> movies) {
        if (movies != null) {
            for (Movie movie : movies) {
                mMoviesLocalDataSource.saveMovie(movie);
            }
        }
    }

    public List<Movie> getCachedMovies() {
        return mCachedMovies == null ? null : new ArrayList<>(mCachedMovies.values());
    }

    public interface MoviesRepositoryObserver {
        void onMoviesChanged();
    }
}
