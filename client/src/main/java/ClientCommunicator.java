import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class ClientCommunicator {
    public static Object doPost(String urlString, String body, String authToken) throws IOException {
        return doServerMethod(getConnection(urlString), "POST", body, authToken);
    }

    public static Object doDelete(String urlString, String authToken) throws IOException {
        return doServerMethod(getConnection(urlString), "DELETE", null, authToken);
    }

    public static Object doPut(String urlString, String body, String authToken) throws IOException {
        return doServerMethod(getConnection(urlString), "PUT", body, authToken);
    }

    public static Object doGet(String urlString, String authToken) throws IOException {
        return doServerMethod(getConnection(urlString), "GET", null, authToken);
    }

    private static Object doServerMethod(HttpURLConnection connection, String method, String body, String authToken) throws IOException {
        connection.setConnectTimeout(5000);
        connection.setRequestMethod(method);
        if (authToken != null) {
            connection.addRequestProperty("Authorization", authToken);
        }
        if (body != null) {
            connection.setDoOutput(true);
            try (OutputStream requestBody = connection.getOutputStream()) {
                requestBody.write(body.getBytes());
            }
        }

        connection.connect();

        InputStreamReader reader;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream responseBody = connection.getInputStream()) {
                reader = new InputStreamReader(responseBody);
            }
        } else {
            try (InputStream responseBody = connection.getErrorStream()) {
                reader = new InputStreamReader(responseBody);
            }
        }
        return new Gson().fromJson(reader, Map.class);
    }

    private static HttpURLConnection getConnection(String urlString) throws IOException {
        return (HttpURLConnection) URI.create(urlString).toURL().openConnection();
    }
}
