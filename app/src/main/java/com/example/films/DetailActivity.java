package com.example.films;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import static com.example.films.MainActivity.EXTRA_NAME;
import static com.example.films.MainActivity.EXTRA_YEAR;
import static com.example.films.MainActivity.EXTRA_URL;
import static com.example.films.MainActivity.EXTRA_Rating;
import static com.example.films.MainActivity.EXTRA_DESCRIPTION;
import static com.example.films.MainActivity.EXTRA_localized_name;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_description);
        Intent intent = getIntent();

        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String name = intent.getStringExtra(EXTRA_NAME);
        String localizedName = intent.getStringExtra(EXTRA_localized_name);
        String year = String.valueOf(intent.getIntExtra(EXTRA_YEAR, 0));
        String rating = String.valueOf(intent.getDoubleExtra(EXTRA_Rating, 0));
        String description = intent.getStringExtra(EXTRA_DESCRIPTION);

        ImageView imageView = findViewById(R.id.image_film);
        TextView nameView = findViewById(R.id.name_film);
        TextView localizedView = findViewById(R.id.localized_name);
        TextView yearView = findViewById(R.id.year);
        TextView ratingView = findViewById(R.id.rating);
        TextView descriptionView = findViewById(R.id.description);

        Picasso.get().load(imageUrl).fit().centerInside().into(imageView);
        nameView.setText(name);
        localizedView.setText(localizedName);
        yearView.setText("Год: " + year);
        ratingView.setText("Рейтинг: " + rating);
        descriptionView.setText(description);

    }
}
