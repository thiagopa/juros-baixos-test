package br.com.thiagopagonha.jurosbaixostest.factory;

import br.com.thiagopagonha.jurosbaixostest.api.JurosBaixosApi;
import br.com.thiagopagonha.jurosbaixostest.config.ApiConfig;
import com.esotericsoftware.yamlbeans.YamlReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;

/**
 * Factory to create a new Rest Client for the Api calls
 * It uses a interceptor to include the apikey in the header of each request
 *
 * @author Thiago Pagonha
 */
public class JurosBaixosApiFactory {
    /**
     * Static server api URL
     */
    private static final String API_URL;

    // -- Reading the server api url from the YAML file
    static {
        try {
            InputStream resource = JurosBaixosApiFactory.class.getClassLoader().getResourceAsStream("application.yml");
            YamlReader reader = new YamlReader(new InputStreamReader(resource));
            ApiConfig apiConfig = reader.read(ApiConfig.class);
            API_URL = apiConfig.getApiUrl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Given an apiKey, initialize retrofit rest client
     */
    public static JurosBaixosApi createApi(String apiKey) {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(newHttpClient(apiKey))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JurosBaixosApi.class);
    }

    /**
     * Initialize okhttp client with custom rules
     */
    private static OkHttpClient newHttpClient(String apiKey) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        // -- No timeout
        builder.callTimeout(Duration.ofSeconds(60l));
        builder.connectTimeout(Duration.ofSeconds(60l));
        // -- Log http requests for easy debug
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        // -- Add api key to each request
        builder.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("X-API-KEY", apiKey).build();
            return chain.proceed(request);
        });

        return builder.build();
    }
}
