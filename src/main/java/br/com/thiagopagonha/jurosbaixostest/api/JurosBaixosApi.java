package br.com.thiagopagonha.jurosbaixostest.api;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface JurosBaixosApi {
    @GET("fizzbuzz")
    Call<List<Integer>> retrieveNumbers();
}
