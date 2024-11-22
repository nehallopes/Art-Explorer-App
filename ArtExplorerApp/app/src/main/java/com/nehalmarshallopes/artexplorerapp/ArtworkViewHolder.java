package com.nehalmarshallopes.assignment3;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ArtworkViewHolder extends RecyclerView.ViewHolder {
    private final TextView titleTextView;
    private final ImageView imageView;

    //This is the constructor for the ArtworkViewHolder
    public ArtworkViewHolder(@NonNull View v) {
        super(v);
        titleTextView = v.findViewById(R.id.textView9);
        imageView = v.findViewById(R.id.imageView4);
    }

    //Here we are binding the data to the recycler view
    public void bind(Artwork art) {
        titleTextView.setText(art.getTitle());  //This will set the title of the artwork in the recycler view

        if (art.getImage_id() == null || art.getImage_id().isEmpty() || art.getImage_id() == "") {
            imageView.setImageResource(R.drawable.not_available);  // Here if the image is not available, we will set the default image
        } else {
            Picasso.get().load(art.getThumbnailUrl()).into(imageView); // This will load the image from the URL
        }
    }
}
