package com.nehalmarshallopes.assignment3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nehalmarshallopes.assignment3.databinding.ActivityArtworkBinding;
import com.squareup.picasso.Picasso;

public class ArtworkActivity extends AppCompatActivity {

    private ActivityArtworkBinding binding;
    public static final String galleryURL = "https://www.artic.edu/galleries/";  //This is the URL for the gallery page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtworkBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Here we are underlining the gallery title
        binding.galleryTitleTextView.setPaintFlags(binding.galleryTitleTextView.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);

        binding.imageView6.setOnClickListener(v -> { // Here this will take us back to the main activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // This onClickListener will take us to the image activity
        binding.imageView8.setOnClickListener(v -> {
            Intent intent = new Intent(this, ImageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("title", getIntent().getStringExtra("title"));
            intent.putExtra("artist_display", getIntent().getStringExtra("artist_display"));
            intent.putExtra("image_id", getIntent().getStringExtra("image_id"));
            startActivity(intent);
        });

        // Below we are getting the data from the intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date_display");
        String artist = intent.getStringExtra("artist_display");
        String imageId = intent.getStringExtra("image_id");
        String department = intent.getStringExtra("department_title");
        String gallery = intent.getStringExtra("gallery_title");
        String placeOfOrigin = intent.getStringExtra("place_of_origin");
        String type = intent.getStringExtra("artwork_type_title");
        String medium = intent.getStringExtra("medium_display");
        String dimensions = intent.getStringExtra("dimensions");
        String credit = intent.getStringExtra("credit_line");

        binding.titleTextView.setText(title);
        binding.dateTextView.setText(date);

        // Here we are splitting the artist name and details if it contains a new line
        if (artist.contains("\n")) {
            String[] artistParts = artist.split("\n", 2);
            binding.artistTextView.setText(artistParts[0]);
            binding.artistDetailsTextView.setText(artistParts[1]);
        } else {
            binding.artistTextView.setText(artist);
            binding.artistDetailsTextView.setText("");
        }

        // Here we are setting the image to the image view
        if (imageId == null || imageId.isEmpty() || imageId == "") {
            binding.imageView8.setImageResource(R.drawable.not_available);
        } else {
            String fullImageUrl = "https://www.artic.edu/iiif/2/" + imageId + "/full/843,/0/default.jpg";
            Picasso.get().load(fullImageUrl).into(binding.imageView8);
        }

        binding.departmentTitleTextView.setText(department);
        binding.galleryTitleTextView.setText(gallery);
        binding.placeTextView.setText(placeOfOrigin);
        binding.typeMediumTextView.setText(type + " - " + medium);
        binding.dimensionsTextView.setText(dimensions);
        binding.creditLineTextView.setText(credit);

        binding.galleryTitleTextView.setOnClickListener(this::browseGallery);
        binding.imageView9.setOnClickListener(this::browseGallery);
    }

    // This method will take us to the gallery page
    public void browseGallery(View v) {
        String galleryId = getIntent().getStringExtra("gallery_id");

        // Here we are creating an intent to open the gallery page
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(galleryURL + galleryId));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}