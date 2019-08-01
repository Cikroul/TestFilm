package com.example.films;


import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.films.Fragment.DescriptionFragment;
import com.example.films.Fragment.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends FragmentActivity implements DataAdapter.OnItemClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView photoRecyclerView;
    private ListView listView;
    private List<Films> mItems = new ArrayList<>();
    private List<Films> genreFilms = mItems;
    private DataAdapter dataAdapter;
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_NAME = "Name";
    public static final String EXTRA_YEAR = "year";
    public static final String EXTRA_Rating= "rating";
    public static final String EXTRA_DESCRIPTION= "description";
    public static final String EXTRA_localized_name = "localized name";
    private RequestQueue mRequestQueue;

    private HomeFragment homeFragment;
    private DescriptionFragment descriptionFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //контейнер жанров
        listView = findViewById(R.id.films_Genre);
        //контейнер списка фильмов
        photoRecyclerView = findViewById(R.id.films_photo_recycler_view);
        photoRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Films clickedItem = genreFilms.get(position);
        detailIntent.putExtra(EXTRA_URL, clickedItem.getImage_url());
        detailIntent.putExtra(EXTRA_NAME, clickedItem.getName());
        detailIntent.putExtra(EXTRA_localized_name, clickedItem.getLocalized_name());
        detailIntent.putExtra(EXTRA_YEAR, clickedItem.getYear());
        detailIntent.putExtra(EXTRA_Rating, clickedItem.getRating());
        detailIntent.putExtra(EXTRA_DESCRIPTION, clickedItem.getDescription());
        startActivity(detailIntent);
    }

    private void parseJSON() {
        String url = "https://s3-eu-west-1.amazonaws.com/sequeniatesttask/films.json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,

                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("films");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                Films item = new Films();
                                item.setId(hit.getInt("id"));
                                item.setLocalized_name(hit.getString("localized_name"));
                                item.setName(hit.getString("name"));
                                item.setYear(hit.getInt("year"));

                                if (hit.has("genres") && !(hit.getString("genres")).equals("[]")) {
                                    String line = hit.getString("genres");

                                    HashSet<String> list = new HashSet<String>();
                                    Pattern pattern = Pattern.compile("\\w+");
                                    Matcher matcher = pattern.matcher(line);
                                    while (matcher.find()) {
                                        list.add(matcher.group());
                                    }
                                    item.setGenres(list);
                                }
                                if (hit.has("genres") && (hit.getString("genres")).equals("[]")) {
                                    HashSet<String> list = new HashSet<String>();
                                    item.setGenres(list);
                                }
                                if (hit.has("image_url") && !(hit.getString("image_url")).equals("null")) {
                                    item.setImage_url(hit.getString("image_url"));
                                }
                                if (hit.has("image_url") && (hit.getString("image_url")).equals("null"))
                                    item.setImage_url("https://portal-keramika.ru/wp-content/uploads/000005313.jpg");
                                if (hit.has("rating") && !(hit.getString("rating")).equals("null")) {
                                    item.setRating(hit.getDouble("rating"));
                                }
                                if (hit.has("rating") && (hit.getString("rating")).equals("null")) {
                                    item.setRating(0.0);
                                }

                                if (hit.has("description")) {
                                    item.setDescription(hit.getString("description"));
                                }
                                mItems.add(item);

                            }

                            // адаптер recyclerView(список фильмов)
                            setDataAdapter();
                            // адаптер списка жанров
                            setAdapterGenre(allGenres(mItems));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    private void setDataAdapter() {
        DataAdapter adapterData = new DataAdapter(this, genreFilms);
        adapterData.setOnItemClickListener(this);
        photoRecyclerView.setAdapter(adapterData);
    }

    private void setAdapterGenre(final String[] genre) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, genre);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                genreFilms = new ArrayList<>();
                genreFilms.clear();

                    System.out.println(genre[position]);
                    for (int i = 0; i < mItems.size(); i++) {
                        if (mItems.get(i).getGenres().contains(genre[position])) {
                            genreFilms.add(mItems.get(i));
                        }
                    }
                DataAdapter adapterData = new DataAdapter(MainActivity.this, genreFilms);
                adapterData.setOnItemClickListener(MainActivity.this);
                photoRecyclerView.setAdapter(adapterData);
                //photoRecyclerView.setAdapter(new DataAdapter(MainActivity.this, genreFilms));

                photoRecyclerView.invalidate();

            }
        });

        listView.setAdapter(adapter);
    }


    private String[] allGenres(List<Films> items) {
        Set<String> set = new HashSet<>();
        // проход по всем элементам списка Films для собрание всех жанров.
        for (int i = 0; i < items.size(); i++) {
            set.addAll(items.get(i).getGenres());
        }
        String[] array = set.toArray(new String[0]);
        return array;
    }
}
