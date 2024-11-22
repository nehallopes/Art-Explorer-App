package com.nehalmarshallopes.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nehalmarshallopes.assignment3.databinding.ActivityCopyrightBinding;

public class CopyrightActivity extends AppCompatActivity {

    private ActivityCopyrightBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCopyrightBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Here this will take the user back to the main activity when the image is clicked
        binding.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        Linkify.addLinks(binding.textView4, Linkify.WEB_URLS); // This will make the textView url clickable

        Linkify.addLinks(binding.textView6, Linkify.WEB_URLS); // This will make the textView url clickable
    }
}