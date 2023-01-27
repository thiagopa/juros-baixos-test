package br.com.thiagopagonha.jurosbaixostest.api.exception;

/**
 * Exception when Juros Baixos Api misbehave
 * @author Thiago Pagonha
 */
public class ApiException extends Exception {
    /**
     * ApiException constructor
     * @param message, why this api has failed
     */
    public ApiException(String message) {
        super(message);
    }
}
