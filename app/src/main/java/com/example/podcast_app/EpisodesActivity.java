package com.example.podcast_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * The EpisodesActivity is where the user selects the episode of the podcast they want to play.
 *
 * Specify the URL of the podcast you want to select an episode of via the "URL" key in the Intent.
 */
public class EpisodesActivity extends AppCompatActivity implements EpisodesAdapter.OnEpisodeListener {
    public static String TAG = "EpisodesActivity";

    private RecyclerView episodes;
    private RecyclerView.Adapter episodesAdapter;
    private RecyclerView.LayoutManager episodesManager;

    private GetPodcast podcastGetter;
    private int requestCode = 1;

    /**
     * onCreate is a method called by the Android SDK when the Activity is started
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);

        // Setup the RecyclerView
        episodes = (RecyclerView) findViewById(R.id.episodes);
        episodesManager = new LinearLayoutManager(this);
        episodes.setLayoutManager(episodesManager);

        // Allow scrolling in the description text view
        TextView tv = findViewById(R.id.podcastDescriptionTextView);
        tv.setMovementMethod(new ScrollingMovementMethod());

        // Get URL string
        Intent i = getIntent();
        String url = i.getStringExtra("URL");

        // Start the asynchronous getting podcast tasks
        podcastGetter = new GetPodcast(this, url);
        podcastGetter.execute();
    }

    /**
     * onEpisodeClick is a function that gets called when an Episode is clicked on in the episodes recycler view
     * @param position
     */
    @Override
    public void onEpisodeClick(int position) {
        try {
            Podcast p = podcastGetter.get();

            Podcast.Episode ep = p.getEpisodes().get(position);

            Log.v("TAG", "clicked: " + position);
            Intent i = new Intent(getApplicationContext(), PlayerActivity.class);

            // Set up extras for the player class
            i.putExtra("audioURL", ep.getMP3URL());
            i.putExtra("imageURL", ep.getImageURL());
            i.putExtra("audioName", ep.getTitle());
            i.putExtra("audioDesc", ep.getDescription());
            i.putExtra("position", position);

            startActivityForResult(i, this.requestCode);
        } catch (Exception e) {
            Log.e(TAG, "Couldn't get episodes!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == this.requestCode){
            if (resultCode == RESULT_OK){
                int newPosition = data.getIntExtra("newPosition", 0);
                onEpisodeClick(newPosition);
            }
        }
    }

    /**
     * GetPodcast is a helper class which does the web request and parsing of a podcast URL.
     */
    private class GetPodcast extends AsyncTask<String, Void, Podcast> {
        EpisodesAdapter.OnEpisodeListener onEpisodeListener;
        String url;

        public GetPodcast(EpisodesAdapter.OnEpisodeListener onEpisodeListener, String url) {
            this.onEpisodeListener = onEpisodeListener;
            this.url = url;
        }

        @Override
        protected Podcast doInBackground(String... params) {
            Log.v(TAG, "Starting web request");

            Podcast p = Podcast.from(url);

            Log.v(TAG, p.toString());

            return p;
        }

        /**
         * When the networking is done, the podcast gets passed here and the rest of the EpisodesActivity is setup
         * @param podcast
         */
        @Override
        protected void onPostExecute(Podcast podcast) {
            super.onPostExecute(podcast);

            // Set the title
            TextView title = findViewById(R.id.podcastTitleTextView);
            title.setText(podcast.getTitle());

            // Set the description
            TextView description = findViewById(R.id.podcastDescriptionTextView);
            description.setText(podcast.getDescription());

            // Put episodes into the adapter
            episodesAdapter = new EpisodesAdapter(podcast, onEpisodeListener);
            episodes.setAdapter(episodesAdapter);

            // Begin the loading of the podcast image
            ImageView podcastImage = findViewById(R.id.podcastImageView);
            Picasso.get().load(podcast.getImageURL()).into(podcastImage);

            Log.v(TAG, "Got podcast:" + podcast.toString());
        }
    }
}