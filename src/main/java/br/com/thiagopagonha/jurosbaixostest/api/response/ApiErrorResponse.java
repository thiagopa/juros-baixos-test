package br.com.thiagopagonha.jurosbaixostest.api.response;

import java.util.List;

public class ApiErrorResponse {
    private String message;
    private List<ApiError> errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ApiError> getErrors() {
        return errors;
    }

    public void setErrors(List<ApiError> errors) {
        this.errors = errors;
    }
}
