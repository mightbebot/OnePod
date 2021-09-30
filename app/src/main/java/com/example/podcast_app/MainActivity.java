package com.example.podcast_app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button ai;
    Button myPlayer;
    public static String TAG = "MainActivity";
    Button go;
    EditText editText;

    public static final String URL_KEY = "com.example.podcast_test_URL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // URL validity checker
    public static boolean URLchecker (String URL){
        if (URLUtil.isValidUrl(URL)){
            return true;
        }
        else return false;
    }

        // This method gets executed when the user pressed the go button,
        // it takes the URL and passes it to the episodes activity
    public void enterOwnUrl (View view) {
        Intent intent = new Intent(this, EpisodesActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText2);
        String URL = editText.getText().toString();
        // this below checks the URL for validity, and starts the activity if it is valid
        if (URLchecker(URL)) {
            intent.putExtra("URL", URL);
            startActivity(intent);
        } else {
            TextView textview = (TextView)findViewById(R.id.textView);
            textview.setText("Invalid entry! Note: the URL must be an RSS");
            textview.setTextColor(Color.RED);
        }
    }

    // method for when button 1 is pressed
    public void buttonPress1 (View view){
        Intent intent = new Intent (this, EpisodesActivity.class);

        String string = getString(com.example.podcast_app.R.string.URL1);
        intent.putExtra("URL", string);
        startActivity(intent);

        Log.v(TAG, string);
    }

    // method for when button 2 is pressed
    public void buttonPress2 (View view){
        Intent intent = new Intent (this, EpisodesActivity.class);

        String string = getString(com.example.podcast_app.R.string.URL2);
        intent.putExtra("URL", string);
        startActivity(intent);
    }

    // method for when button 3 is pressed
    public void buttonPress3 (View view){
        Intent intent = new Intent (this, EpisodesActivity.class);

        String string = getString(com.example.podcast_app.R.string.URL3);
        intent.putExtra("URL", string);
        startActivity(intent);
    }

    // method for when button 4 is pressed
    public void buttonPress4 (View view){
        Intent intent = new Intent (this, EpisodesActivity.class);

        String string = getString(com.example.podcast_app.R.string.URL4);
        intent.putExtra("URL", string);
        startActivity(intent);
    }

    // method for when button 5 is pressed
    public void buttonPress5 (View view){
        Intent intent = new Intent (this, EpisodesActivity.class);

        String string = getString(com.example.podcast_app.R.string.URL5);
        intent.putExtra("URL", string);
        startActivity(intent);
    }

    // method for when button 6 is pressed
    public void buttonPress6 (View view){
        Intent intent = new Intent (this, EpisodesActivity.class);

        String string = getString(com.example.podcast_app.R.string.URL6);
        intent.putExtra("URL", string);
        startActivity(intent);
    }

    // method for when button 7 is pressed
    public void buttonPress7 (View view){
        Intent intent = new Intent (this, EpisodesActivity.class);

        String string = getString(com.example.podcast_app.R.string.URL7);
        intent.putExtra("URL", string);
        startActivity(intent);
    }

    // method for when button 8 is pressed
    public void buttonPress8 (View view){
        Intent intent = new Intent (this, EpisodesActivity.class);

        String string = getString(com.example.podcast_app.R.string.URL8);
        intent.putExtra("URL", string);
        startActivity(intent);
    }

    // method for when button 9 is pressed
    public void buttonPress9 (View view){
        Intent intent = new Intent (this, EpisodesActivity.class);

        String string = getString(com.example.podcast_app.R.string.URL9);
        intent.putExtra("URL", string);
        startActivity(intent);
    }
}
