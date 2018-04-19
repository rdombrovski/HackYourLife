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

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Random;


public class GameActivity extends AppCompatActivity {
    private ArrayList<com.bif812.roman.hackyourlife.Answer> answerList; //Keeps the words (answers)
    private ArrayList<Integer> numbers; //control to show words without repeatables
    ImageButton target1;
    ImageButton target2;
    ImageButton target3;
    ImageView feed;
    TextView countedWord; //word that is shown
    TextView textCounter;
    TextView lifeTxt;
    TextView timeTxt;
    private int oppCounter;
    private int objectCounter;
    private int totalMatches;

    private int usage1;
    private int usage2;
    private int usage3;
    com.bif812.roman.hackyourlife.Answer actualWord;

    //current Amino Acids (3)
    ArrayList<String> wordsArray;
    int lastWord;
    int wordTime;
    int gameTime;
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
    Handler wordTimerHandler = new Handler();

    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            int randomPos;
            wordTimerHandler.removeCallbacks(timerRunnable);
            feed.setImageResource(getResources().getIdentifier("empty", "drawable", getPackageName()));
            if ((oppCounter > 0) && (objectCounter < totalMatches)) {
                randomPos = getRandomNumber(wordsArray.size());
                lastWord = randomPos;
                actualWord = (com.bif812.roman.hackyourlife.Answer)answerList.get(lastWord);
                countedWord.setText(wordsArray.get(lastWord));
                wordTimerHandler.postDelayed(this, wordTime);
            } else if ((oppCounter == 0) && (objectCounter < totalMatches)) {
                    wordTimerHandler.removeCallbacks(timerRunnable);
                life = true;
                callFeedback();
            } else if ((oppCounter >= 0) && (objectCounter == totalMatches)) {
                wordTimerHandler.removeCallbacks(timerRunnable);
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
            if ((gameTime > 1000) && (oppCounter > 0) && (objectCounter < totalMatches)) {
                gameTime = gameTime -timeCheck;
                timeTxt.setText(String.valueOf(gameTime / 1000) + getString(R.string.sec));
                gametimerHandler.postDelayed(this, timeCheck);
            } else if ((gameTime == 1000) && (oppCounter > 0) && (objectCounter < totalMatches)) {
                gameTime = gameTime -timeCheck;
                timeTxt.setText(String.valueOf(gameTime / 1000) + getString(R.string.sec));
                gametimerHandler.removeCallbacks(gametimerRunnable);
                time = true;
                callFeedback();
            }  else if ((gameTime > 0) && (oppCounter == 0) && (objectCounter < totalMatches)) {
                gametimerHandler.removeCallbacks(gametimerRunnable);
                life = true;
                callFeedback();
            } else if ((gameTime > 1000) && (oppCounter > 0) && (objectCounter == totalMatches)) {
                gametimerHandler.removeCallbacks(gametimerRunnable);
                win = true;
                callFeedback();
            }
        }
    };

    //Call the feedback activity after game is done
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

        target1 = (ImageButton)findViewById(R.id.image1);
        target2 = (ImageButton)findViewById(R.id.image2);
        target3 = (ImageButton)findViewById(R.id.image3);
        feed = (ImageView)findViewById(R.id.feed);
        countedWord = (TextView)findViewById(R.id.word);
        textCounter = (TextView)findViewById(R.id.counter);
        lifeTxt = (TextView)findViewById(R.id.lifeBox);
        timeTxt = (TextView)findViewById(R.id.timeBox);

        //retrieving bundle from AminoGame.java
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            // Get ArrayList Bundle
            //create arrays for words and numbers
            try {
                answerList = (ArrayList<com.bif812.roman.hackyourlife.Answer>) extras.getSerializable("key");
                totalMatches = answerList.size()/2;
                wordsArray = new ArrayList<String>();
                numbers = new ArrayList<Integer>();
                for (int i = 0; i < totalMatches; i++) {
                    numbers.add(i);
                }
                initGame();
            }
            catch (Exception ex)
            {
                Log.e("Loading Error", "No Array was passed");
            }
        }


        target1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (verifyWord(target1.getContentDescription().toString())) {
                    showNextImage(target1);
                }
            }
        });

        target2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (verifyWord(target2.getContentDescription().toString())) {
                    showNextImage(target2);
                }
            }
        });
        target3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (verifyWord(target3.getContentDescription().toString())) {
                    //show the next image
                    showNextImage(target3);
                }
            }
        });
    }

    private void initGame(){
        int randomPos;

        objectCounter = 0;
        oppCounter = 3;
        wordTime =1200; //time in milliseconds to show the next word
        gameTime =60000; //time to play the game in 60 seconds
        timeCheck=1000;
        win = false;
        life = false;
        time = false;

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        textCounter.setText(String.valueOf(objectCounter)+"/"+String.valueOf(totalMatches));
        lifeTxt.setText(String.valueOf(oppCounter));
        timeTxt.setText(String.valueOf(gameTime /1000) + getString(R.string.sec));

        randomPos = getRandomNumber(numbers.size());
        usage1 = numbers.get(randomPos);
        numbers.remove(randomPos);
        actualWord = (com.bif812.roman.hackyourlife.Answer) answerList.get(usage1);
        wordsArray.add(actualWord.getName());
        target1.setImageResource(actualWord.getImg());
        target1.setContentDescription(actualWord.getName());

        randomPos = getRandomNumber(numbers.size());
        usage2 = numbers.get(randomPos);
        numbers.remove(randomPos);
        actualWord = (com.bif812.roman.hackyourlife.Answer)answerList.get(usage2);
        wordsArray.add(actualWord.getName());
        target2.setImageResource(actualWord.getImg());
        target2.setContentDescription(actualWord.getName());

        randomPos = getRandomNumber(numbers.size());
        usage3 = numbers.get(randomPos);
        numbers.remove(randomPos);
        actualWord = (com.bif812.roman.hackyourlife.Answer)answerList.get(usage3);
        wordsArray.add(actualWord.getName());
        target3.setImageResource(actualWord.getImg());
        target3.setContentDescription(actualWord.getName());


        lastWord = getRandomNumber(wordsArray.size());
        countedWord.setText(wordsArray.get(lastWord));
        //start counter after showing word
        wordTimerHandler.postDelayed(timerRunnable, wordTime);
        //start timer for the game
        gametimerHandler.postDelayed(gametimerRunnable, timeCheck);
    }


    //getRandom number
    private int getRandomNumber(int num){
        Random r = new Random();
        int random = r.nextInt(num);
        return random;
    }

    private boolean verifyWord(String aa){
        String actualWord;

        actualWord = countedWord.getText().toString();

        if (aa.equalsIgnoreCase(actualWord)) {
            objectCounter++;
            playSound(CORRECT);
            feed.setImageResource(getResources().getIdentifier("correct", "drawable", getPackageName()));
            textCounter.setText(String.valueOf(objectCounter)+"/"+String.valueOf(totalMatches));
            return true;

        } else {
            oppCounter--;
            feed.setImageResource(getResources().getIdentifier("wrong", "drawable" , getPackageName()));
            playSound(WRONG);
            lifeTxt.setText(String.valueOf(oppCounter));
            return false;
        }
    }

    private void showNextImage(ImageButton target) {
        int correctWord = lastWord;
        int randomPos;
        if ((numbers.size() > 0 ) && (numbers.size() <= 20)){
            randomPos = getRandomNumber(numbers.size());
            usage1 = numbers.get(randomPos);
            numbers.remove(randomPos);
            actualWord = (com.bif812.roman.hackyourlife.Answer)answerList.get(usage1);
            wordsArray.set(correctWord, actualWord.getName());
            target.setImageResource(actualWord.getImg());
            target.setContentDescription(actualWord.getName());
        } else {
            wordsArray.remove(correctWord);
            target.setImageResource(getResources().getIdentifier("empty", "drawable" , getPackageName()));
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
        wordTimerHandler.removeCallbacks(timerRunnable);
        gametimerHandler.removeCallbacks(gametimerRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();

        wordTimerHandler.postDelayed(timerRunnable, wordTime);
        gametimerHandler.postDelayed(gametimerRunnable, timeCheck);
    }

  //  @Override
   // public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_game, menu);
   //     return true;
   // }

    //@Override
   // public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
   //     int id = item.getItemId();

        //noinspection SimplifiableIfStatement
   //     if (id == R.id.action_settings) {
   //         return true;
     //   }

    //    return super.onOptionsItemSelected(item);
   // }
}
