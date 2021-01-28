package ua.com.nikiforov.error_holder;

import java.net.HttpURLConnection;

public  class ErrorMessage {

    public static final int CODE_200 = HttpURLConnection.HTTP_OK;
    public static final int CODE_201 = HttpURLConnection.HTTP_CREATED;
    public static final int CODE_204 = HttpURLConnection.HTTP_NO_CONTENT;
    public static final int CODE_400 = HttpURLConnection.HTTP_BAD_REQUEST;
    public static final int CODE_401 = HttpURLConnection.HTTP_UNAUTHORIZED;
    public static final int CODE_404 = HttpURLConnection.HTTP_NOT_FOUND;

    public static final String ERROR_400 = "Request is wrong. Check for errors";
    public static final String ERROR_401 = "Authorization information is missing or invalid";
    public static final String ERROR_404 = "Cannot find resource according to request";
}
