package com.bif812.roman.hackyourlife;

import android.support.v7.app.AppCompatActivity;

public class MediaPlayerFunction {
    //Variables
    private android.media.MediaPlayer musicPlayer;

    //Constructor
    public MediaPlayerFunction(android.media.MediaPlayer x){
        this.musicPlayer=x;
    }

    // General playFunction for the meditation
    public void playFunction (android.media.MediaPlayer x, int y){
        if(musicPlayer==null){
            musicPlayer = x;
            musicPlayer.start();
        } else if (!musicPlayer.isPlaying()) {
            musicPlayer.seekTo(y);
            musicPlayer.start();

        }
    }
    //General pauseFunction for the meditation
    public int pauseFunction (){
        if(musicPlayer!=null) {
            if (musicPlayer.isPlaying()) {
                musicPlayer.pause();
                int x;
                x = musicPlayer.getCurrentPosition();
                return x;
            }
        }
        return 0;
    }

    // General stopfunction for the meditation
    public void stopFunction() {
        if(musicPlayer!=null) {
            musicPlayer.stop();
            musicPlayer = null;
        }
    }


}
