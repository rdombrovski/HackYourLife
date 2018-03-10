package com.bif812.roman.hackyourlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bif812.roman.hackyourlife.R;

/**
 * Created by Roman on 3/6/2018.
 */

public class NewGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_start);
        ImageView aminoGame = (ImageView) findViewById(R.id.aminoIcon);

        aminoGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewGame.this, AminoGame.class);
                startActivity(myIntent);
            }
        });
    }

}
