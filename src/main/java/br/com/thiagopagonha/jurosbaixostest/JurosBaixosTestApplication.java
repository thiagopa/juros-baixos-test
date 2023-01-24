package br.com.thiagopagonha.jurosbaixostest;

import br.com.thiagopagonha.jurosbaixostest.api.JurosBaixosApi;
import br.com.thiagopagonha.jurosbaixostest.factory.JurosBaixosApiFactory;
import retrofit2.Response;

public class JurosBaixosTestApplication {
    public static void main(String[] args) throws Exception{
        validateArgument(args);
        JurosBaixosApi jurosBaixosApi = JurosBaixosApiFactory.createApi(args[0]);
        Response response = jurosBaixosApi.retrieveNumbers().execute();
        System.out.println(response.body());
    }

    private static void validateArgument(String[] args) {
        if(args.length == 0) {
            throw new IllegalArgumentException("Api Key is missing");
        }
        System.out.println("Application Key: " + args[0]);
    }
}
