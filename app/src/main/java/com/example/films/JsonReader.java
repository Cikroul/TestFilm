package com.example.films;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JsonReader {
    private static final String TAG = "Server";


    public String getJSONString(String UrlSpec) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(UrlSpec)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        return result;
    }

    public List<Films> fetchItems() {
        List<Films> galleryItems = new ArrayList<>();
        try {
            String jsonString = getJSONString("https://s3-eu-west-1.amazonaws.com/sequeniatesttask/films.json");
            JSONObject jsonBody = new JSONObject(jsonString);
            parsItems(galleryItems, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "ошибка загрузки данных", ioe);
        } catch (JSONException joe) {
            Log.e(TAG, "Ошибка парсинга JSON", joe);
        }
        Collections.sort(galleryItems);
        return galleryItems;
    }


    private static void parsItems(List<Films> items, JSONObject jsonBody) throws IOException, JSONException {

        JSONArray photoJsonArray = jsonBody.getJSONArray("films");
        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
            Films item = new Films();
            item.setId(photoJsonObject.getInt("id"));
            item.setLocalized_name(photoJsonObject.getString("localized_name"));
            item.setName(photoJsonObject.getString("name"));
            item.setYear(photoJsonObject.getInt("year"));

            if (photoJsonObject.has("genres") && !(photoJsonObject.getString("genres")).equals("[]")) {
                String line = photoJsonObject.getString("genres");

                HashSet<String> list = new HashSet<String>();
                Pattern pattern = Pattern.compile("\\w+");
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    list.add(matcher.group());
                }
                item.setGenres(list);
            }
            if (photoJsonObject.has("genres") && (photoJsonObject.getString("genres")).equals("[]")) {
                HashSet<String> list = new HashSet<String>();
                item.setGenres(list);
            }
            if (photoJsonObject.has("image_url") && !(photoJsonObject.getString("image_url")).equals("null")) {
                item.setImage_url(photoJsonObject.getString("image_url"));
            }
            if (photoJsonObject.has("image_url") && (photoJsonObject.getString("image_url")).equals("null"))
                item.setImage_url("https://portal-keramika.ru/wp-content/uploads/000005313.jpg");
            if (photoJsonObject.has("rating") && !(photoJsonObject.getString("rating")).equals("null")) {
                item.setRating(photoJsonObject.getDouble("rating"));
            }
            if (photoJsonObject.has("rating") && (photoJsonObject.getString("rating")).equals("null")) {
                item.setRating(0.0);
            }

            if (photoJsonObject.has("description")) {
                item.setDescription(photoJsonObject.getString("description"));
            }
            items.add(item);
        }
    }
}
