package com.bif812.roman.hackyourlife;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Meditation extends AppCompatActivity {

    //Variables
    Button playButton, pauseButton, stopButton;

    private int paused;
    MediaPlayerFunction myMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.meditation);

        myMediaPlayer = new MediaPlayerFunction(MediaPlayer.create(Meditation.this, R.raw.chill));


        //Initializing and using playButton to play music
        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myMediaPlayer.playFunction(MediaPlayer.create(Meditation.this, R.raw.chill), paused);

            }
        });

        //Initializing and using pauseButton to pause music
        pauseButton = (Button) findViewById(R.id.pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paused=myMediaPlayer.pauseFunction();

            }
        });

        //Initializing and using stopButton to stop music
        stopButton = (Button) findViewById(R.id.stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myMediaPlayer.stopFunction();

            }
        });

    }
}
