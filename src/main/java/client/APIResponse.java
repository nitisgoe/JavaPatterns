package client;

import java.util.List;
import java.util.Map;

public class APIResponse<T> {
    private final int statusCode;
    private final T data;
    private final Map<String, List<String>> headers;

    public APIResponse(int statusCode, T data, Map<String, java.util.List<String>> headers) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
    }

    public int getStatusCode() { return statusCode; }
    public T getData() { return data; }
    public Map<String, java.util.List<String>> getHeaders() { return headers; }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

}
