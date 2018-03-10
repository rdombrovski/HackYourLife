package com.bif812.roman.hackyourlife;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bif812.roman.hackyourlife.R;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Random;


public class GameActivity extends AppCompatActivity {
    private ArrayList<com.bif812.roman.hackyourlife.Palabra> palabraList; //Keeps the words (answers)
    private ArrayList<Integer> numeros; //control to show words without repeatables
    ImageButton tarjeta1;
    ImageButton tarjeta2;
    ImageButton tarjeta3;
    ImageView feed;
    TextView palabraMostrar; //word that is shown
    TextView contadorTxt;
    TextView lifeTxt;
    TextView timeTxt;
    private int cantOportunidad;
    private int contadorObjeto;
    private int totalMatches;
    private int usado1;
    private int usado2;
    private int usado3;
    com.bif812.roman.hackyourlife.Palabra palActual;
    ArrayList<String> palabrasArray;
    int ultimaPal;
    int timePalabra;
    int timeJuego;
    int timeCheck;
    private boolean win;
    private boolean life;
    private boolean time;


    //sound
    SoundPool soundPool;
    HashMap<Integer, Integer> soundPoolMap;
    int CORRECT = 0;
    int WRONG = 0;


    //timer to show words
    Handler paltimerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            int randomPos;
            paltimerHandler.removeCallbacks(timerRunnable);
            feed.setImageResource(getResources().getIdentifier("empty", "drawable", getPackageName()));
            if ((cantOportunidad > 0) && (contadorObjeto < totalMatches)) {
                randomPos = getRandomNumber(palabrasArray.size());
                ultimaPal = randomPos;
                palActual = (com.bif812.roman.hackyourlife.Palabra)palabraList.get(ultimaPal);
                palabraMostrar.setText(palabrasArray.get(ultimaPal));
                paltimerHandler.postDelayed(this, timePalabra);
            } else if ((cantOportunidad == 0) && (contadorObjeto < totalMatches)) {
                paltimerHandler.removeCallbacks(timerRunnable);
                life = true;
                callFeedback();
            } else if ((cantOportunidad >= 0) && (contadorObjeto == totalMatches)) {
                paltimerHandler.removeCallbacks(timerRunnable);
                win = true;
                callFeedback();
            }
        }
    };

    //timer to play the game
    Handler gametimerHandler = new Handler();
    Runnable gametimerRunnable = new Runnable() {
        @Override
        public void run() {
            gametimerHandler.removeCallbacks(gametimerRunnable);
            if ((timeJuego > 1000) && (cantOportunidad > 0) && (contadorObjeto < totalMatches)) {
                timeJuego = timeJuego-timeCheck;
                timeTxt.setText(String.valueOf(timeJuego / 1000) + getString(R.string.sec));
                gametimerHandler.postDelayed(this, timeCheck);
            } else if ((timeJuego == 1000) && (cantOportunidad > 0) && (contadorObjeto < totalMatches)) {
                timeJuego = timeJuego-timeCheck;
                timeTxt.setText(String.valueOf(timeJuego / 1000) + getString(R.string.sec));
                gametimerHandler.removeCallbacks(gametimerRunnable);
                time = true;
                callFeedback();
            }  else if ((timeJuego > 0) && (cantOportunidad == 0) && (contadorObjeto < totalMatches)) {
                gametimerHandler.removeCallbacks(gametimerRunnable);
                life = true;
                callFeedback();
            } else if ((timeJuego > 1000) && (cantOportunidad > 0) && (contadorObjeto == totalMatches)) {
                gametimerHandler.removeCallbacks(gametimerRunnable);
                win = true;
                callFeedback();
            }
        }
    };

    //Call the feedback activity
    private void callFeedback() {
        Intent intent;
        intent = new Intent(getApplicationContext(), com.bif812.roman.hackyourlife.FeedbackActivity.class);
        intent.putExtra(com.bif812.roman.hackyourlife.FeedbackActivity.EXTRA_LIFE, life);
        intent.putExtra(com.bif812.roman.hackyourlife.FeedbackActivity.EXTRA_TIME, time);
        intent.putExtra(com.bif812.roman.hackyourlife.FeedbackActivity.EXTRA_WIN, win);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initSounds(this.getApplicationContext());
        tarjeta1 = (ImageButton)findViewById(R.id.imagen1);
        tarjeta2 = (ImageButton)findViewById(R.id.imagen2);
        tarjeta3 = (ImageButton)findViewById(R.id.imagen3);
        feed = (ImageView)findViewById(R.id.feed);
        palabraMostrar = (TextView)findViewById(R.id.palabra);
        contadorTxt = (TextView)findViewById(R.id.contador);
        lifeTxt = (TextView)findViewById(R.id.lifeBox);
        timeTxt = (TextView)findViewById(R.id.timeBox);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Get ArrayList Bundle
            palabraList = (ArrayList<com.bif812.roman.hackyourlife.Palabra>) extras.getSerializable("key");
            totalMatches = palabraList.size();
            palabrasArray = new ArrayList<String>();
            numeros = new ArrayList<Integer>();
            for (int i=0;i<totalMatches;i++) {
                numeros.add(i);
            }
            initGame();
        }
        tarjeta1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (verifyPalabra(tarjeta1.getContentDescription().toString())) {
                    showNextImage(tarjeta1);
                }
            }
        });

        tarjeta2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (verifyPalabra(tarjeta2.getContentDescription().toString())) {
                    showNextImage(tarjeta2);
                }
            }
        });
        tarjeta3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (verifyPalabra(tarjeta3.getContentDescription().toString())) {
                    //mostrar la proxima imagen
                    showNextImage(tarjeta3);
                }
            }
        });

    }

    private void initGame(){
        int randomPos;

        contadorObjeto = 0;
        cantOportunidad = 3;
        timePalabra=1200; //time in miliseconds to show the next word
        timeJuego=40000; //time to play the game in 40 seconds
        timeCheck=1000;
        win = false;
        life = false;
        time = false;
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        contadorTxt.setText(String.valueOf(contadorObjeto)+"/"+String.valueOf(totalMatches));
        lifeTxt.setText(String.valueOf(cantOportunidad));
        timeTxt.setText(String.valueOf(timeJuego/1000) + getString(R.string.sec));
        randomPos = getRandomNumber(numeros.size());
        usado1 = numeros.get(randomPos);
        numeros.remove(randomPos);
        palActual = (com.bif812.roman.hackyourlife.Palabra) palabraList.get(usado1);
        palabrasArray.add(palActual.getName());
        tarjeta1.setImageResource(palActual.getImg());
        tarjeta1.setContentDescription(palActual.getName());
        randomPos = getRandomNumber(numeros.size());
        usado2 = numeros.get(randomPos);
        numeros.remove(randomPos);
        palActual = (com.bif812.roman.hackyourlife.Palabra)palabraList.get(usado2);
        palabrasArray.add(palActual.getName());
        tarjeta2.setImageResource(palActual.getImg());
        tarjeta2.setContentDescription(palActual.getName());
        randomPos = getRandomNumber(numeros.size());
        usado3 = numeros.get(randomPos);
        numeros.remove(randomPos);
        palActual = (com.bif812.roman.hackyourlife.Palabra)palabraList.get(usado3);
        palabrasArray.add(palActual.getName());
        tarjeta3.setImageResource(palActual.getImg());
        tarjeta3.setContentDescription(palActual.getName());
        ultimaPal = getRandomNumber(palabrasArray.size());
        palabraMostrar.setText(palabrasArray.get(ultimaPal));
        //inicia timer para mostrar la palabra
        paltimerHandler.postDelayed(timerRunnable, timePalabra);
        //inicia timer para el juego
        gametimerHandler.postDelayed(gametimerRunnable, timeCheck);
    }


    //getRandom number
    private int getRandomNumber(int num){
        Random r = new Random();
        int random = r.nextInt(num);
        return random;
    }

    private boolean verifyPalabra(String pal){
        String palActual;

        palActual = palabraMostrar.getText().toString();

        if (pal.equalsIgnoreCase(palActual)) {
            contadorObjeto++;
            playSound(CORRECT);
            feed.setImageResource(getResources().getIdentifier("correct", "drawable", getPackageName()));
            contadorTxt.setText(String.valueOf(contadorObjeto)+"/"+String.valueOf(totalMatches));
            return true;

        } else {
            cantOportunidad--;
            feed.setImageResource(getResources().getIdentifier("wrong", "drawable" , getPackageName()));
            playSound(WRONG);
            lifeTxt.setText(String.valueOf(cantOportunidad));
            return false;
        }
    }

    private void showNextImage(ImageButton tarjeta) {
        int palabraCorrecta = ultimaPal;
        int randomPos;
        if ((numeros.size() > 0 ) && (numeros.size() <= 17)){
            randomPos = getRandomNumber(numeros.size());
            usado1 = numeros.get(randomPos);
            numeros.remove(randomPos);
            palActual = (com.bif812.roman.hackyourlife.Palabra)palabraList.get(usado1);
            palabrasArray.set(palabraCorrecta,palActual.getName());
            tarjeta.setImageResource(palActual.getImg());
            tarjeta.setContentDescription(palActual.getName());
        } else {
            palabrasArray.remove(palabraCorrecta);
            tarjeta.setImageResource(getResources().getIdentifier("vacio", "drawable" , getPackageName()));
        }
    }

    //Functions to manage the sound
    /** Populate the SoundPool*/
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.N)
    public void initSounds(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        soundPoolMap = new HashMap(2);

        CORRECT = soundPool.load(context,R.raw.correct, 1);
        WRONG = soundPool.load(context,R.raw.error, 1);

    }

    private void playSound(int soundID) {
        if (soundPool != null) {
            AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = curVolume/maxVolume;
            int priority = 1;
            int no_loop = 0;
            soundPool.play(soundID, volume,volume, priority, no_loop, 1f);
            Log.d("GAME","Volume " + volume);
            Log.d("GAME","Volume max " + maxVolume);
            Log.d("GAME","Volume cur " + curVolume);
            //soundPool.play(soundID, 1, 1, 1, 0, 1);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        paltimerHandler.removeCallbacks(timerRunnable);
        gametimerHandler.removeCallbacks(gametimerRunnable);

    }

    @Override
    public void onResume() {
        super.onResume();

        paltimerHandler.postDelayed(timerRunnable, timePalabra);
        gametimerHandler.postDelayed(gametimerRunnable, timeCheck);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
