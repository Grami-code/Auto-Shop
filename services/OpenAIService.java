package com.serviceauto.services;

import okhttp3.*;
import org.json.JSONObject;

/**
 * Serviciu pentru conectarea la API-ul OpenAI.
 *
 * Clasa OpenAIService trimite prompt-uri catre AI
 * si primeste raspunsuri generate automat.
 * Poate fi folosita pentru generarea automata de descrieri,
 * sugestii sau alte texte legate de reparatii.
 */

public class OpenAIService {

    private static final String API_KEY = "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";
    private static final String URL = "https://api.openai.com/v1/chat/completions";

    public static String cereAI(String prompt) {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        json.put("model", "gpt-4o-mini");
        json.put("max_tokens", 300);

        json.put("messages", new org.json.JSONArray()
                .put(new JSONObject()
                        .put("role", "user")
                        .put("content", prompt)));

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(URL)
                .header("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) return "Eroare AI";

            String rasp = response.body().string();
            JSONObject obj = new JSONObject(rasp);

            return obj
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "Eroare la conexiunea cu AI";
        }
    }
}

//sa adaug ceva cu ai , gen sa bagi specificatiile masinii si el sa iti dea tipul de ulei , etc


