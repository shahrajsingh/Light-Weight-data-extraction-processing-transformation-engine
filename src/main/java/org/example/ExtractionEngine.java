package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ExtractionEngine {
    private static final String API_KEY = "24e25730aa704640817770db652098f5Y";
    private static final String[] KEYWORDS = {
            "Canada", "University", "Dalhousie", "Halifax", "Canada Education", "Moncton", "hockey", "Fredericton", "celebration"
    };

    public static void main(String[] args) throws IOException {
        for (String keyword : KEYWORDS) {
            String rawData = fetchNewsData(keyword);
            DataProcessingEngine.processData(rawData);
        }
    }

    private static String fetchNewsData(String keyword) throws IOException {
        URL url = new URL("https://newsapi.org/v2/everything?q=" + keyword +"&apiKey=24e25730aa704640817770db652098f5");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed to fetch news data. HTTP error code: " + responseCode);
        }

        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuilder rawData = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            rawData.append(line);
        }
        bufferedReader.close();
        return rawData.toString();
    }
}
