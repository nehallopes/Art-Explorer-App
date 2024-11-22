package com.nehalmarshallopes.assignment3;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nehalmarshallopes.assignment3.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArtworkAdapter adapter;
    private List<Artwork> artworkList;
    private RequestQueue requestQueue;
    private ConnectivityManager connectivityManager;
    private List<String> galleryIds;
    private Random random;

    private String fields = "title, date_display, artist_display, medium_display, \n" +
            "artwork_type_title, image_id, dimensions, department_title, \n" +
            "credit_line, place_of_origin, gallery_title, gallery_id, id, api_link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        random = new Random();

        //Here we are setting the adapter with the recycler view
        artworkList = new ArrayList<>();
        adapter = new ArtworkAdapter(artworkList, this::artworkOnClick);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        //Here this will clear search editText
        binding.clearImage.setOnClickListener(v -> {
            binding.searchEditText.setText("");
        });

        //Here this will set underline to the text
        binding.copyrightText.setPaintFlags(binding.copyrightText.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
        binding.copyrightText.setOnClickListener(v -> {
            Intent intent = new Intent(this, CopyrightActivity.class);
            startActivity(intent);
        });

        //This here will create a request queue
        requestQueue = Volley.newRequestQueue(this);

        //This below is the search button to search for the artwork
        binding.searchBtn.setOnClickListener(v -> {
            String input = binding.searchEditText.getText().toString().trim();

            if (!input.isEmpty() && input.length() >= 3) { //This if condition will check the input if it is not empty and the length is greater than 3
                Network currentNetwork = connectivityManager.getActiveNetwork();
                if (currentNetwork == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("No Connection Error")
                            .setIcon(R.drawable.logo)
                            .setMessage("No network connection present - cannot contact Art Institute API server.")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .show();
                }
                else {
                    downloadArtwork(input);  //This will call the downloadArtwork method
                }
            }
            else if (input.length() < 3) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Search string too short")
                        .setIcon(R.drawable.logo)
                        .setMessage("Please try a longer search string")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        connectivityManager = getSystemService(ConnectivityManager.class); //This will get the connectivity manager

        //This below is the random button to get random artwork
        binding.randomBtn.setOnClickListener(v -> {
            Network currentNetwork = connectivityManager.getActiveNetwork();

            if (currentNetwork == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("No Connection Error")
                        .setIcon(R.drawable.logo)
                        .setMessage("No network connection present - cannot contact Art Institute API server.")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
            else {
                randomOnClick(v); //This will call the randomOnClick method
            }
        });
    }

    //This below method will download the artwork
    private void downloadArtwork(String input) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);

        String url = new Uri.Builder() //Here this uri builder will build the url
                .scheme("https")
                .authority("api.artic.edu")
                .appendPath("api")
                .appendPath("v1")
                .appendPath("artworks")
                .appendPath("search")
                .appendQueryParameter("q", input)
                .appendQueryParameter("limit", "15")
                .appendQueryParameter("page", "1")
                .appendQueryParameter("fields", fields)
                .build()
                .toString();

        //This below is the json object request
        JsonObjectRequest obj = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    parseArtwork(response); //This will call the parseArtwork method
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.recyclerView.setAlpha(1);
                    binding.recyclerView.setBackground(null);
                },
                null
        );

        requestQueue.add(obj); //Here this will add the request to the request queue
    }

    //Below is the method to parse the artwork
    private void parseArtwork(JSONObject response) {
        artworkList.clear(); //This will clear the artwork list
        try {
            JSONArray Arr = response.getJSONArray("data"); //Here we are getting the data array from the response

            if (Arr.length() == 0) { //This if condition will check if the length of the array is 0
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No search results found")
                        .setIcon(R.drawable.logo)
                        .setMessage("No results found for '" + binding.searchEditText.getText().toString().trim() + "'. Please try another search string.")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }

            for (int i = 0; i < Arr.length(); i++) {
                JSONObject artworkObj = Arr.getJSONObject(i); //Here we are getting the artwork object from the array

                String title = artworkObj.getString("title");  //This will get the title from the artwork object
                if (title.equals("null")) {
                    title = "";
                }

                String dateDisplay = artworkObj.getString("date_display"); //This will get the date display from the artwork object
                if (dateDisplay.equals("null")) {
                    dateDisplay = "";
                }

                String artistDisplay = artworkObj.getString("artist_display"); //This will get the artist display from the artwork object
                if (artistDisplay.equals("null")) {
                    artistDisplay = "";
                }

                String imageId = artworkObj.getString("image_id"); //This will get the image id from the artwork object
                if (imageId.equals("null")) {
                    imageId = "";
                }
                String departmentTitle = artworkObj.getString("department_title"); //This will get the department title from the artwork object
                if (departmentTitle.equals("null")) {
                    departmentTitle = "";
                }

                String galleryTitle = artworkObj.getString("gallery_title"); //This will get the gallery title from the artwork object
                String galleryId = artworkObj.getString("gallery_id");  //This will get the gallery id from the artwork object
                if (galleryTitle.equals("null") && galleryId.equals("null")) {
                    galleryTitle = "Not on Display";
                    galleryId = "null";
                }

                String placeOfOrigin = artworkObj.getString("place_of_origin"); //This will get the place of origin from the artwork object
                if (placeOfOrigin.equals("null")) {
                    placeOfOrigin = "";
                }

                String artworkTypeTitle = artworkObj.getString("artwork_type_title");  //This will get the artwork type title from the artwork object
                if (artworkTypeTitle.equals("null")) {
                    artworkTypeTitle = "";
                }

                String mediumDisplay = artworkObj.getString("medium_display");  //This will get the medium display from the artwork object
                if (mediumDisplay.equals("null")) {
                    mediumDisplay = "";
                }

                String dimensions = artworkObj.getString("dimensions"); //This will get the dimensions from the artwork object
                if (dimensions.equals("null")) {
                    dimensions = "";
                }

                String creditLine = artworkObj.getString("credit_line");  //This will get the credit line from the artwork object
                if (creditLine.equals("null")) {
                    creditLine = "";
                }

                String apiLink = artworkObj.getString("api_link"); //This will get the api link from the artwork object
                if (apiLink.equals("null")) {
                    apiLink = "";
                }

                String id = artworkObj.getString("id"); // This will get the id from the artwork object
                if (id.equals("null")) {
                    id = "";
                }

                //Here we are creating the artwork object
                Artwork artwork = new Artwork(
                        title, imageId, dateDisplay, artistDisplay,
                        departmentTitle, galleryTitle, galleryId,
                        placeOfOrigin, artworkTypeTitle, mediumDisplay,
                        creditLine, dimensions, apiLink, id
                );
                artworkList.add(artwork); //This will add the artwork to the artwork list
            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // This below method will open the artwork activity
    private void artworkOnClick(Artwork art) {

        Intent intent = new Intent(this, ArtworkActivity.class);

        intent.putExtra("title", art.getTitle());
        intent.putExtra("date_display", art.getDate_display());
        intent.putExtra("artist_display", art.getArtist_display());
        intent.putExtra("image_id", art.getImage_id());
        intent.putExtra("department_title", art.getDepartment_title());
        intent.putExtra("gallery_title", art.getGallery_title());
        intent.putExtra("gallery_id", art.getGallery_id());
        intent.putExtra("place_of_origin", art.getPlace_of_origin());
        intent.putExtra("artwork_type_title", art.getArtwork_type_title());
        intent.putExtra("medium_display", art.getMedium_display());
        intent.putExtra("dimensions", art.getDimensions());
        intent.putExtra("credit_line", art.getCredit_line());
        intent.putExtra("api_link", art.getApi_link());
        intent.putExtra("id", art.getId());

        startActivity(intent);
    }

    // This below method will get the random artwork
    public void randomOnClick(View v) {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);

        Uri uri = new Uri.Builder()
                .scheme("https")
                .authority("api.artic.edu")
                .appendPath("api")
                .appendPath("v1")
                .appendPath("galleries")
                .appendQueryParameter("limit", "100")
                .appendQueryParameter("fields", "id")
                .appendQueryParameter("page", "1")
                .build();

        String url = uri.toString(); //This here gets the url

        // Here we are creating the json object request
        JsonObjectRequest obj = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        galleryIds = new ArrayList<>();  //Here creating the gallery ids array list

                        JSONArray data = response.getJSONArray("data");  //Here we are getting the data array from the response

                        // Below this loop is for getting the gallery ids
                        for (int i = 0; i < data.length(); i++) {
                            String galleryId = data.getJSONObject(i).getString("id");
                            galleryIds.add(galleryId);
                        }

                        String randomId = galleryIds.get(random.nextInt(galleryIds.size()));
                        downloadRandomArtwork(randomId);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
               null
        );
        requestQueue.add(obj);
    }

    // This below method here will download the random artwork
    private void downloadRandomArtwork(String galleryId) {

        Uri uri = new Uri.Builder()
                .scheme("https")
                .authority("api.artic.edu")
                .appendPath("api")
                .appendPath("v1")
                .appendPath("artworks")
                .appendPath("search")
                .appendQueryParameter("query[term][gallery_id]", galleryId)
                .appendQueryParameter("limit", "100")
                .appendQueryParameter("fields", fields)
                .build();

        String url = uri.toString();

        JsonObjectRequest obj = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray arr = response.getJSONArray("data");
                        if (arr.length() == 0) {
                            String newRandomGalleryId = galleryIds.get(random.nextInt(galleryIds.size()));
                            downloadRandomArtwork(newRandomGalleryId);
                            return;
                        }

                        JSONObject artworkObj = arr.getJSONObject(random.nextInt(arr.length()));
                        Artwork randomArtwork = parseRandomArtwork(artworkObj);

                        artworkList.clear();
                        artworkList.add(randomArtwork);  //This will add the random artwork to the artwork list

                        adapter.notifyDataSetChanged();

                        binding.progressBar.setVisibility(View.GONE); //This will set the progress bar to gone
                        binding.recyclerView.setVisibility(View.VISIBLE); //This will set the recycler view to visible
                        binding.recyclerView.setAlpha(1);
                        binding.recyclerView.setBackground(null);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                null
        );

        requestQueue.add(obj);
    }

    private Artwork parseRandomArtwork(JSONObject artworkObj) {

        try {
            String title = artworkObj.getString("title"); //This will get the title from the artwork object
            if (title.equals("null")) {
                title = "";
            }

            String dateDisplay = artworkObj.getString("date_display");  //This will get the date display from the artwork object
            if (dateDisplay.equals("null")) {
                dateDisplay = "";
            }

            String artistDisplay = artworkObj.getString("artist_display");  //This will get the artist display from the artwork object
            if (artistDisplay.equals("null")) {
                artistDisplay = "";
            }

            String imageId = artworkObj.getString("image_id");  //This will get the image id from the artwork object
            if (imageId.equals("null")) {
                imageId = "";
            }
            String departmentTitle = artworkObj.getString("department_title");     //This will get the department title from the artwork object
            if (departmentTitle.equals("null")) {
                departmentTitle = "";
            }

            String galleryTitle = artworkObj.getString("gallery_title");    //This will get the gallery title from the artwork object
            String galleryId = artworkObj.getString("gallery_id");   //This will get the gallery id from the artwork object
            if (galleryTitle.equals("null") && galleryId.equals("null")) {
                galleryTitle = "Not on Display";
                galleryId = "null";
            }

            String placeOfOrigin = artworkObj.getString("place_of_origin");     //This will get the place of origin from the artwork object
            if (placeOfOrigin.equals("null")) {
                placeOfOrigin = "";
            }

            String artworkTypeTitle = artworkObj.getString("artwork_type_title");   //This will get the artwork type title from the artwork object
            if (artworkTypeTitle.equals("null")) {
                artworkTypeTitle = "";
            }

            String mediumDisplay = artworkObj.getString("medium_display");  //This will get the medium display from the artwork object
            if (mediumDisplay.equals("null")) {
                mediumDisplay = "";
            }

            String dimensions = artworkObj.getString("dimensions"); //This will get the dimensions from the artwork object
            if (dimensions.equals("null")) {
                dimensions = "";
            }

            String creditLine = artworkObj.getString("credit_line"); //This will get the credit line from the artwork object
            if (creditLine.equals("null")) {
                creditLine = "";
            }

            String apiLink = artworkObj.getString("api_link");  //This will get the api link from the artwork object
            if (apiLink.equals("null")) {
                apiLink = "";
            }

            String id = artworkObj.getString("id");     //This will get the id from the artwork object
            if (id.equals("null")) {
                id = "";
            }

            return new Artwork(
                    title, imageId, dateDisplay, artistDisplay,
                    departmentTitle, galleryTitle, galleryId,
                    placeOfOrigin, artworkTypeTitle, mediumDisplay,
                    creditLine, dimensions, apiLink, id
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}