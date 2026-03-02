package com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.scene.shape.Line;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Base64;

public class ApiService {

    private static final Dotenv dotenv = Dotenv.load();

    public static String getData(String endpoint) throws Exception {
        String apiUrl = dotenv.get("API_BASE_URL") + endpoint;
        String username = dotenv.get("API_USERNAME");
        String password = dotenv.get("API_PASSWORD");

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        conn.setRequestProperty("Authorization", "Basic " + encodedAuth);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        return content.toString();
    }

    public static String updateData(String endpoint, String jsonInputString) throws Exception {
        String apiUrl = dotenv.get("API_BASE_URL") + endpoint;
        String username = dotenv.get("API_USERNAME");
        String password = dotenv.get("API_PASSWORD");

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                responseCode >= 200 && responseCode < 300 ? conn.getInputStream() : conn.getErrorStream()))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        conn.disconnect();

        if (responseCode >= 200 && responseCode < 300) {
            return "Success";
        } else {
            return response.toString();
        }
    }
    public static String postData(String endpoint, String jsonInputString) throws Exception {
        String apiUrl = dotenv.get("API_BASE_URL") + endpoint;
        String username = dotenv.get("API_USERNAME");
        String password = dotenv.get("API_PASSWORD");

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                responseCode >= 200 && responseCode < 300 ? conn.getInputStream() : conn.getErrorStream()))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        conn.disconnect();

        if (responseCode >= 200 && responseCode < 300) {
            return "Success";
        } else {
            return response.toString();
        }
    }

}