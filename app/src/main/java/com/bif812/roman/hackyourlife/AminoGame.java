package com.bif812.roman.hackyourlife;

import android.app.ProgressDialog;
import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AminoGame extends AppCompatActivity {
    Button instructions;
    Button playGame;
    String content;
    ArrayList<com.bif812.roman.hackyourlife.Answer> rowItems;
    ProgressDialog progressDialog;

    private final String OBJECTS_TAG = "objects";
    private final String OBJECT_TAG = "object";
    private final String ID_TAG = "id";
    private final String TEXT_TAG = "text";
    private final String IMAGE_TAG = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amino_game);
        playGame = (Button)findViewById(R.id.playBtn);
        instructions = (Button)findViewById(R.id.instrBtn);
        //call the game
        playGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), com.bif812.roman.hackyourlife.GameActivity.class);
                // Create a Bundle and Put Bundle in to it
                Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("key", rowItems);
                // Put Bundle in to Intent and call start Activity
                intent.putExtras(bundleObject);
                startActivity(intent);
            }
        });
        //call instructions
        instructions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intentInst = new Intent(getApplicationContext(), com.bif812.roman.hackyourlife.InstructionActivity.class);
                startActivity(intentInst);
            }
        });
        openJSON();
    }

    private void openJSON() {
        progressDialog = ProgressDialog.show(AminoGame.this, "", "");
        try
        {
            InputStream fileObj =
                    getResources().openRawResource(R.raw.objects);

            content = readStream(fileObj);
            readJSON(content);
            fileObj.close();
            progressDialog.dismiss();
        }
        catch (Exception ex)
        {
            Log.e("Reading Error", "Error reading the JSON file from raw directory");
        }
    }

    private void readJSON(String content){
        JSONArray obj = null;
        try {
            JSONObject json = new JSONObject(content);
            String object = json.getString(OBJECTS_TAG);
            Log.i("readJSON", object);
            JSONObject pal = json.getJSONObject(OBJECTS_TAG);
            obj = pal.getJSONArray(OBJECT_TAG);
            // looping through All albums
            rowItems = new ArrayList<com.bif812.roman.hackyourlife.Answer>();
            for (int i = 0; i < obj.length(); i++) {
                JSONObject al = obj.getJSONObject(i);
                String id = al.getString(ID_TAG);
                String text = al.getString(TEXT_TAG);
                String imageName = al.getString(IMAGE_TAG);
                Log.d("readJSON", id + " " +text + " " + imageName);
                // get resource id by image name
                //final int resourceId = resources.getIdentifier(imageName, "drawable", context.getPackageName());

                int resId = getResources().getIdentifier(imageName, "drawable" , getPackageName());
                com.bif812.roman.hackyourlife.Answer items = new com.bif812.roman.hackyourlife.Answer(id,resId,text);
                rowItems.add(items);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    // progressDialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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
