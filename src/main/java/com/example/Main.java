package com.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String USERS_API = "https://fake-json-api.mock.beeceptor.com/users";
    private static final String COMPANIES_API = "https://fake-json-api.mock.beeceptor.com/companies";

    public static void main(String[] args) {
        try {
            // 1. Получаем данные от API
            String usersJson = fetchJsonFromApi(USERS_API);
            String companiesJson = fetchJsonFromApi(COMPANIES_API);

            // 2. Парсим данные
            List<Map<String, Object>> users = parseJson(usersJson);
            List<Map<String, Object>> companies = parseJson(companiesJson);

            // 3. Выводим данные с форматированием
            System.out.println("=== Users ===");
            printFormattedData(users);

            System.out.println("\n=== Companies ===");
            printFormattedData(companies);
        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }

    private static String fetchJsonFromApi(String apiUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new IOException("Ошибка подключения к API: " + response.statusCode());
        }
    }

    private static List<Map<String, Object>> parseJson(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    private static void printFormattedData(List<Map<String, Object>> data) {
        for (int i = 0; i < data.size(); i++) {
            System.out.println("Item " + (i + 1) + ":");
            for (Map.Entry<String, Object> entry : data.get(i).entrySet()) {
                System.out.printf("  %s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
    }
}