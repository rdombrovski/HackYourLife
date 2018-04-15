package com.bif812.roman.hackyourlife;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bif812.roman.hackyourlife.MainActivity;
import com.bif812.roman.hackyourlife.R;


public class FeedbackActivity extends AppCompatActivity {
    public static final String EXTRA_LIFE = "life";
    public static final String EXTRA_TIME = "time";
    public static final String EXTRA_WIN = "win";

    boolean lifeFeed;
    boolean timeFeed;
    boolean winFeed;
    TextView feedbackText;
    Button playAgain;
    FloatingActionButton shareBtn;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedbackText = (TextView)findViewById(R.id.feedbackView);
        playAgain = (Button)findViewById(R.id.playagainBtn);
        shareBtn = (FloatingActionButton)findViewById(R.id.share);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lifeFeed = extras.getBoolean(EXTRA_LIFE);
            timeFeed = extras.getBoolean(EXTRA_TIME);
            winFeed = extras.getBoolean(EXTRA_WIN);
           showFeedback();
        }
        //call instructions
        playAgain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.i("GAME","click en share");
                createShareIntent();
            }
        });
    }

    private void showFeedback(){
        if(lifeFeed) {
            feedbackText.setText(getResources().getString(R.string.feedback_neg_lifes));
        }
        if(winFeed) {
            feedbackText.setText(getResources().getString(R.string.feedback_pos));
        }
        if(timeFeed) {
            feedbackText.setText(getResources().getString(R.string.feedback_neg_time));
        }
    }

    // Create and return the Share Intent
    private void createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String header= getResources().getString(R.string.share_title);

        String shareText = header + getResources().getString(R.string.share_app);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent,""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
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
