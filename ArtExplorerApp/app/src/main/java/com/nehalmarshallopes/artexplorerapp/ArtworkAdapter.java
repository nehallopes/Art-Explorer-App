package com.nehalmarshallopes.assignment3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkViewHolder> {

    private List<Artwork> artList; //This list will store the artwork objects
    private final OnItemClickListener listen;

    public interface OnItemClickListener {
        void onItemClick(Artwork art); //This method will be called when an item is clicked
    }

    //This is the constructor for the adapter
    public ArtworkAdapter(List<Artwork> artworkList, OnItemClickListener listener) {
        this.artList = artworkList;
        this.listen = listener;
    }

    // Here this method will create the view holder
    @NonNull
    @Override
    public ArtworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artwork_list, parent, false);
        return new ArtworkViewHolder(view);
    }

    // This method will bind the data to the view holder
    @Override
    public void onBindViewHolder(@NonNull ArtworkViewHolder holder, int position) {

        Artwork artwork = artList.get(position);
        holder.bind(artwork);
        holder.itemView.setOnClickListener(v -> listen.onItemClick(artwork));
    }

    //This method will return the number of items in the list
    @Override
    public int getItemCount() {
        return artList.size();
    }
}
