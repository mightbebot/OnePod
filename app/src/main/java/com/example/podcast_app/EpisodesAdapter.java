package com.example.podcast_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A UI class which maps episodes onto a RecyclerView
 */
// Following along from docs at https://developer.android.com/guide/topics/ui/layout/recyclerview#java
public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder> {
    /**
     * A quick little handler for clicking
     */
    public interface OnEpisodeListener {
        void onEpisodeClick(int position);
    }

    /**
     * Represents the individual cells of the RecyclerView
     */
    public static class EpisodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public static String TAG = "EpisodeViewHolder";

        public TextView name;
        public ImageView thumbnail;

        public Context context;

        OnEpisodeListener onEpisodeListener;

        /**
         * Instantiate a new EpisodeViewHolder
         *
         * @param v
         * @param ctx
         * @param onEpisodeListener
         */
        public EpisodeViewHolder(View v, Context ctx, OnEpisodeListener onEpisodeListener) {
            super(v);

            context = ctx;

            thumbnail = v.findViewById(R.id.episodeThumb);
            name = v.findViewById(R.id.episodeName);
            this.onEpisodeListener = onEpisodeListener;

            v.setOnClickListener(this);
        }

        public void set(Podcast.Episode ep) {
            name.setText(ep.getTitle());

            Picasso.get().load(ep.getImageURL()).into(thumbnail);
        }

        /**
         * A function which dispatches the onEpisodeClick event
         * @param v
         */
        @Override
        public void onClick(View v) {
            onEpisodeListener.onEpisodeClick(getAdapterPosition());
        }
    }

    private OnEpisodeListener onEpisodeListener;
    private Podcast podcast;

    public EpisodesAdapter(Podcast podcast, OnEpisodeListener onEpisodeListener) {
        super();

        this.podcast = podcast;
        this.onEpisodeListener = onEpisodeListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EpisodesAdapter.EpisodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);

        EpisodeViewHolder vh = new EpisodeViewHolder(v, parent.getContext(), onEpisodeListener);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EpisodeViewHolder holder, int position) {
        holder.set(podcast.getEpisodes().get(position));
    }

    @Override
    public int getItemCount() {
        return podcast.getEpisodes().size();
    }
}
