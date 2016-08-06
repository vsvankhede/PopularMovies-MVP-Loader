package com.vstechlab.popularmovies.data.source;

import com.vstechlab.popularmovies.data.Movie;

import java.util.List;

public interface MoviesDataSource {
    interface MoviesCallback{
        void onMoviesLoaded(List<Movie> movies);
        void onDataNotAvailable();
    }

    List<Movie> getMovies();

    void saveMovie(Movie movie);

}
