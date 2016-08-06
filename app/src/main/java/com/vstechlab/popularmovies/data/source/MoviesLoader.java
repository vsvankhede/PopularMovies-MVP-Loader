package com.vstechlab.popularmovies.data.source;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;

import com.vstechlab.popularmovies.data.Movie;

import java.util.List;

public class MoviesLoader extends AsyncTaskLoader<List<Movie>> implements MoviesRepository.MoviesRepositoryObserver{

    private MoviesRepository mRepository;

    public MoviesLoader(Context context, @NonNull MoviesRepository repository) {
        super(context);
        mRepository = repository;
    }

    @Override
    public List<Movie> loadInBackground() {
        return mRepository.getMovies();
    }

    @Override
    public void deliverResult(List<Movie> data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mRepository.cachedMovieAvailable()) {
            deliverResult(mRepository.getCachedMovies());
        }

        mRepository.addContentObserver(this);

        if (takeContentChanged() || !mRepository.cachedMovieAvailable()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mRepository.removeContentObserver(this);
    }

    @Override
    public void onMoviesChanged() {
        if (isStarted()) {
            forceLoad();
        }
    }
}
