package com.vstechlab.popularmovies.data.source.remote;

import com.squareup.okhttp.OkHttpClient;
import com.vstechlab.popularmovies.BuildConfig;
import com.vstechlab.popularmovies.data.Movie;
import com.vstechlab.popularmovies.data.Movies;
import com.vstechlab.popularmovies.data.source.MoviesDataSource;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MoviesRemoteDataSource implements MoviesDataSource {
    private static final String API_KEY = "42940bbff0d5d9073a2800563a42925d";

    private static MoviesRemoteDataSource INSTANCE;
    private final MoviesAPI mService;

    static List<Movie> movieList = new ArrayList<>();

    public static MoviesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRemoteDataSource();
        }
        return INSTANCE;
    }

    private MoviesRemoteDataSource() {
        OkHttpClient client = new OkHttpClient();
        SignedInterceptor interceptor = new SignedInterceptor(API_KEY);
        client.interceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MoviesAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(MoviesAPI.class);
    }

    @Override
    public List<Movie> getMovies() {
        Call<Movies> call = mService.getMoviesSortByPopularity(MoviesAPI.sortByHighRated);
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Response<Movies> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Movies movies = response.body();
                    movieList =  movies.getResults();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // Todo handle exception
            }
        });
        return movieList;
    }

    @Override
    public void saveMovie(Movie movie) {
        // No need to save on remote
    }
}
