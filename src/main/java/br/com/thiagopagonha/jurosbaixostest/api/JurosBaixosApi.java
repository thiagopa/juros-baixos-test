package br.com.thiagopagonha.jurosbaixostest.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface JurosBaixosApi {
    @GET("fizzbuzz")
    Call<List<Integer>> retrieveNumbers();

    @POST("fizzbuzz/{sha256Hash}")
    Call<ResponseBody> consumeAnswer(@Path("sha256Hash") String sha256Hash, @Body List<String> translatedNumbers);
    @GET("fizzbuzz/{sha256Hash}/canihastreasure")
    Call<ResponseBody> openTreasureChest(@Path("sha256Hash") String sha256Hash);
}
