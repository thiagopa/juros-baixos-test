package br.com.thiagopagonha.jurosbaixostest.api.response;

public class ApiError {
    private String message;
    private String path;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
