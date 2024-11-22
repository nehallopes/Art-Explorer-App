package com.nehalmarshallopes.assignment3;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nehalmarshallopes.assignment3.databinding.ActivityImageBinding;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    private ActivityImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Here this underlines the gallery title
        binding.imageView3.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // This will get the title, artist, and imageId from the intent and display it in the ImageActivity
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String artist = intent.getStringExtra("artist_display");
        String imageId = intent.getStringExtra("image_id");

        binding.titleTextView3.setText(title);
        binding.artistTextView2.setText(artist);

        // This if condition will check if the imageId is empty or null, if it is then it will display the not_available image
        if (imageId == null || imageId.isEmpty() || imageId == "") {
            binding.imageView5.setImageResource(R.drawable.not_available);
        } else {
            String fullImageUrl = "https://www.artic.edu/iiif/2/" + imageId + "/full/843,/0/default.jpg";
            Picasso.get().load(fullImageUrl).into(binding.imageView5);
        }

        // This is the maximum, medium, and minimum scale of the image
        binding.imageView5.setMaximumScale(10f);
        binding.imageView5.setMediumScale(5f);
        binding.imageView5.setMinimumScale(2.5f);

        // Below code will display the scale of the image in percentage
        binding.scaleTextView.setText("Scale: 100%");
        binding.imageView5.setOnScaleChangeListener((scaleFactor, focusX, focusY) -> {
                binding.scaleTextView.setText(String.format("Scale: %.0f%%", binding.imageView5.getScale() * 100));});
    }
}