package com.aj.socialImages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.ProgressBar;

import com.aj.socialImages.widgets.GalleryRecyclerViewAdapter;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ActivityFacebookImages extends AppCompatActivity {

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.imageRecyclerView)
    RecyclerView imageRecyclerView;
    private String facebookId;
    GalleryRecyclerViewAdapter galleryRecyclerViewAdapter;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_images);
        ButterKnife.bind(this);

//        imageRecyclerView = (RecyclerView) findViewById(R.id.imageRecyclerView);

        Display display = this.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int columns = (Math.round(dpWidth / 100) - 1);
        imageRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this, columns);
        imageRecyclerView.setLayoutManager(gridLayoutManager);

        galleryRecyclerViewAdapter = new GalleryRecyclerViewAdapter(this);
        imageRecyclerView.setAdapter(galleryRecyclerViewAdapter);

        facebookId = getIntent().getStringExtra("facebookId");
        GetFacebookImages(facebookId);
    }

    public void GetFacebookImages(final String userId) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "picture");
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + userId + "/photos/uploaded",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        Log.v("TAG", "Facebook Photos response: " + response);
                        try {
                            if (response.getError() == null) {
                                JSONObject joMain = response.getJSONObject();
                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data");
                                    ArrayList<String> fbImages = new ArrayList<>();
                                    for (int i = 0; i < jaData.length(); i++)//Get no. of images
                                    {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        fbImages.add(joAlbum.getString("picture"));
                                    }
                                    galleryRecyclerViewAdapter.addAll(fbImages);
                                }
                            } else {
                                Log.v("TAG", response.getError().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }
}
