package com.vstechlab.popularmovies.data.source.remote;

import com.vstechlab.popularmovies.data.Movies;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MoviesAPI {
    String BASE_URL = "http://api.themoviedb.org/3/";
    String PARAM_API_KEY = "api_key";

    String sortByPopularity = "popularity.desc";
    String sortByHighRated = "vote_average.desc";
    String favorite = "favorite";

    @GET("discover/movie")
    Call<Movies> getMoviesSortByPopularity(@Query("sort_by") String sortByPopularity);

    @GET("discover/movie")
    Call<Movies> getMoviesSortByRatting(@Query("sort_by") String sortByHighRated);

}
