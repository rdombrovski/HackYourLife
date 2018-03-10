package com.bif812.roman.hackyourlife;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bif812.roman.hackyourlife.NewGame;
import com.bif812.roman.hackyourlife.R;
import com.bif812.roman.hackyourlife.Typewriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Typewriter type1 = (Typewriter)findViewById(R.id.intro1);
        final Typewriter type2 = (Typewriter)findViewById(R.id.intro2);
        final Typewriter type3 = (Typewriter)findViewById(R.id.intro3);
        final Typewriter type4 = (Typewriter)findViewById(R.id.intro4);
        final String intro1 = getString(R.string.introText);

        final String intro2 = getString(R.string.introText2);
        final String intro3 = getString(R.string.introText3);
        final String intro4 = getString(R.string.introText4);
        Button introButton = (Button)findViewById(R.id.introButton);


        type1.setText("");
        type2.setText("");
        type3.setText("");
        type4.setText("");
        type1.setCharacterDelay(150);
        type1.animateText(intro1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 2s = 5000ms
                type2.setCharacterDelay(150);
                type2.animateText(intro2);
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 2s = 5000ms
                type3.setCharacterDelay(150);
                type3.animateText(intro3);
            }
        }, 8000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 2s = 5000ms
                type4.setCharacterDelay(150);
                type4.animateText(intro4);
            }
        }, 12000);

//        typing(type1, intro1);
//        typing(type2, intro2);
//        typing(type3, intro3);
//        typing(type4, intro4);
//
//        public void typing (Typewriter type, String s){
//            type.setText("");
//            type.setCharacterDelay(150);
//            type.animateText(s);
//
//        }

        introButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, NewGame.class);
                startActivity(myIntent);
            }
        });

    }
}
