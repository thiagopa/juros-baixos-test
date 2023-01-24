package br.com.thiagopagonha.jurosbaixostest.factory;

import br.com.thiagopagonha.jurosbaixostest.api.JurosBaixosApi;
import br.com.thiagopagonha.jurosbaixostest.config.ApiConfig;
import com.esotericsoftware.yamlbeans.YamlReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;


public class JurosBaixosApiFactory {
    private static final String API_URL;

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

    public static JurosBaixosApi createApi(String apiKey) {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(newHttpClient(apiKey))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JurosBaixosApi.class);
    }

    private static OkHttpClient newHttpClient(String apiKey) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

        builder.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("X-API-KEY", apiKey).build();
            return chain.proceed(request);
        });

        return builder.build();
    }
}
