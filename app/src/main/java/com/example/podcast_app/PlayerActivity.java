package com.example.podcast_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

public class PlayerActivity extends AppCompatActivity {
    private MediaPlayer audioPlayer;
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler = new Handler();
    private TextView audioCurrTime;
    private TextView audioTotalTime;
    private TextView audioTitle;
    private TextView audioDescription;
    private ImageView audioImage;
    private View v;
    private String audioURL;
    private String imageURL;
    private String audioName;
    private String audioDesc;
    private Button fastNext;
    private Button fastPrevious;
    private int position;

    /**
     *
     * @param someURL input any working url
     * @return output edited version of url ensuring it starts with https://
     */
    public String normalizeURL(String someURL){
        if (!someURL.substring(0, 7).equals("http://") && !someURL.substring(0, 8).equals("https://")){
            return "http://" + someURL;
        } else if (someURL.substring(0, 7).equals("http://")){
            return "http://" + someURL.substring(7);
        } else if (someURL.substring(0, 8).equals("https://")){
            return "http://" + someURL.substring(8);
        }
        return someURL;
    }

    /**
     *
     * @return output current timestamp of audio file being played in the format HH:MM:SS or MM:SS
     */
    public String getCurrentTime() {
        int milliSecs = audioPlayer.getCurrentPosition();
        int secs = (milliSecs / 1000) % 60;
        int mins = ((milliSecs / (60000)) % 60);
        int hrs = ((milliSecs / (3600000)) % 24);

        if (((audioPlayer.getDuration() / 3600000) % 24) == 0) {
            return (String.format("%02d", mins) + ":" + String.format("%02d", secs));
        } else {
            return (String.format("%02d", hrs) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
        }
    }

    /**
     *
     * @return output total timestamp of audio file being played in the format HH:MM:SS or MM:SS
     */
    public String getTotalTime() {
        int milliSecs = audioPlayer.getDuration();
        int secs = (milliSecs / 1000) % 60;
        int mins = ((milliSecs / (60000)) % 60);
        int hrs = ((milliSecs / (3600000)) % 24);
        if (hrs == 0) {
            return (String.format("%02d", mins) + ":" + String.format("%02d", secs));
        } else {
            return (String.format("%02d", hrs) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
        }
    }

    /**
     * resets the player view once audio file stops playing
     */
    public void playerStopped(){
        seekBar.setProgress(0);
        audioCurrTime.setText("00:00");
        audioTotalTime.setText("00:00");
        audioTitle.setText("Select Audio File To Load Title");
        audioDescription.setText("Select Audio File To Load Description");
        //PULL LOGO FOR IMAGE IF STOPPED
        audioImage.setImageResource(0);
    }

    /**
     *
     * assigns activity objects and starts playing selected audio file
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent i = getIntent();

        audioURL = i.getStringExtra("audioURL");
        imageURL = i.getStringExtra("imageURL");
        audioName = i.getStringExtra("audioName");
        audioDesc = i.getStringExtra("audioDesc");
        audioDesc = i.getStringExtra("audioDesc");
        position = i.getIntExtra("position",0);

        seekBar = findViewById(R.id.seekBar);
        audioCurrTime = findViewById(R.id.audioCurrTime);
        audioTotalTime = findViewById(R.id.audioTotalTime);
        audioTitle = findViewById(R.id.audioName);
        audioDescription = findViewById(R.id.audioDescription);
        audioImage = findViewById(R.id.audioImage);
        fastNext = findViewById(R.id.fastNext);
        fastPrevious = findViewById(R.id.fastPrevious);

        audioDescription.setMovementMethod(new ScrollingMovementMethod());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if ((audioPlayer != null) && b) {
                    audioPlayer.seekTo(i);
                    audioCurrTime.setText(getCurrentTime());
                } else if (audioPlayer == null) {
                    seekBar.setProgress(0);
                    Toast.makeText(PlayerActivity.this, "No Audio Selected!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        fastNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newPosition = Math.max(0,position-1);

                Intent i = new Intent();
                i.putExtra("newPosition", newPosition);

                setResult(RESULT_OK, i);
                finish();
            }
        });

        fastPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newPosition = position+1;

                Intent i = new Intent();
                i.putExtra("newPosition", newPosition);

                setResult(RESULT_OK, i);
                finish();
            }
        });

        play(v);
    }

    /**
     * sets position of seekbar every second
     */
    private void positionSeekBar() {
        if (audioPlayer != null) {
            seekBar.setProgress(audioPlayer.getCurrentPosition());
            audioCurrTime.setText(getCurrentTime());

            if (audioPlayer.isPlaying()) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        positionSeekBar();
                    }
                };
                handler.postDelayed(runnable, 1000);
            }
        }
    }

    /**
     *
     * plays the selected audio file; if a media player does not exist, the method will create one
     */
    public void play(View v) {
        if (audioPlayer == null) {

            audioPlayer = new MediaPlayer();

            try {
                audioPlayer.setDataSource(this, Uri.parse(normalizeURL(audioURL)));
                audioPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            seekBar.setMax(audioPlayer.getDuration());
            audioTotalTime.setText(getTotalTime());
            audioTitle.setText(audioName);
            audioDescription.setText(Html.fromHtml(audioDesc));
            Picasso.get().load(normalizeURL(imageURL)).into(audioImage);

            audioPlayer.setOnCompletionListener(mediaPlayer -> {
                stopPlayer();
                playerStopped();
            });
        }

        audioPlayer.start();
        positionSeekBar();
    }

    /**
     *
     * pauses the currently playing audio file
     */
    public void pause(View v) {
        if (audioPlayer != null) {
            audioPlayer.pause();
        }
    }

    /**
     *
     * stops the currently playing audio file
     */
    public void stop(View v) {
        stopPlayer();
        playerStopped();
    }

    /**
     *
     * releases current media player when player is stopped
     */
    private void stopPlayer() {
        if (audioPlayer != null) {
            audioPlayer.release();
            audioPlayer = null;
        }
    }

    /**
     *
     * stops the player if the activity is unselected
     */
    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

}
