package br.com.thiagopagonha.jurosbaixostest.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Representation of the jurosbaixos api
 *
 * @author Thiago Pagonha
 */
public interface JurosBaixosApi {
    /**
     * Returns an array of numbers
     *
     * @return, Retrofit Call expecting a response of a list of numbers
     */
    @GET("fizzbuzz")
    Call<List<Integer>> retrieveNumbers();

    /**
     * Consumes the fizzbuzz answer from the earlier provided range of numbers
     * The numbers must be translated and hashed in a compact JSON with the Sha256 algorithm
     * Example the ["fizz","buzz","fizzbuzz"]
     * results in the following hash c66a63862cf416c2acfe81ae697c066cff80b430af31fc9cae70957f355ded7d.
     *
     * @param sha256Hash,        the hash of the translated numbers
     * @param translatedNumbers, the translated numbers the were previously hashed
     * @return, Retrofit Call to be executed
     * @see JurosBaixosApi#retrieveNumbers
     */
    @POST("fizzbuzz/{sha256Hash}")
    Call<ResponseBody> consumeAnswer(@Path("sha256Hash") String sha256Hash, @Body List<String> translatedNumbers);

    /**
     * Check if the api wants to give you the treasure yet
     *
     * @param sha256Hash, the hash of the translated numbers
     * @return Retrofit Call to be executed
     */
    @GET("fizzbuzz/{sha256Hash}/canihastreasure")
    Call<ResponseBody> openTreasureChest(@Path("sha256Hash") String sha256Hash);

    /**
     * Flush the current range and unlock a new range
     *
     * @param sha256Hash, the hash of the translated numbers
     * @return Retrofit Call to be executed
     */
    @DELETE("fizzbuzz/{sha256Hash}")
    Call<ResponseBody> deleteAnswer(@Path("sha256Hash") String sha256Hash);
}
