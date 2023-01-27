package br.com.thiagopagonha.jurosbaixostest.api.execution;

import br.com.thiagopagonha.jurosbaixostest.api.exception.ApiException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * Trying to handle all the api errors in a generic way
 *
 * @author Thiago Pagonha
 */
public final class ApiExecution {
    private ApiExecution() {
    }

    /**
     * Synchronous call of the http request and error handling
     *
     * @param apiCall, to be executed
     * @param <T>,     Parametrized type
     * @return The api response
     * @throws ApiException, throws the error response body of the http request
     * @throws IOException,  in case the connection is not sucessful
     */
    public static <T> Response<T> safeExecute(Call<T> apiCall) throws ApiException, IOException {
        Response<T> response = null;

        try {
            response = apiCall.execute();
        } catch (Exception e) {
            throw new ApiException(
                    String.format("API EXCEPTION=%s", e.getMessage())
            );
        }

        if (!response.isSuccessful()) {
            String apiErrorResponse = extractRawResponseError(response);
            throw new ApiException(
                    String.format("API ERROR=%s", apiErrorResponse)
            );
        }

        return response;
    }

    /**
     * Extract the raw error from the http response
     *
     * @param apiResponse, the api call response
     * @param <T>,         Parametrized type
     * @return a string containing the server error message
     * @throws IOException, in case the connection is not sucessful
     */
    private static <T> String extractRawResponseError(Response<T> apiResponse) throws IOException {
        try (ResponseBody errorResponseBody = apiResponse.errorBody()) {
            if (errorResponseBody != null) {
                return errorResponseBody.string();
            }
        }
        return null;
    }
}
