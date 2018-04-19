package com.bif812.roman.hackyourlife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Zena on 2018-04-16.
 */

public class SNP_select extends AppCompatActivity {

    String content;
    ArrayList<com.bif812.roman.hackyourlife.SNP> rowItems;
    ProgressDialog progressDialog;
    Button cont;

    private final String snps_tag = "snps";
    private final String snp_tag = "snp";
    private final String id_tag = "id";
    private final String code_tag = "code";
    private final String nuc_tag = "nuc";
    private final String exp_tag = "exp";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snp_select_layout);
        openJSON();
        cont = (Button) findViewById(R.id.contButton);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SNP_select.this, NewGame.class);
                startActivity(myIntent);
            }
        });
    }

    private void openJSON() {
        progressDialog = ProgressDialog.show(SNP_select.this, "", "");
        try
        {
            InputStream fileObj =
                    getResources().openRawResource(R.raw.snp);

            content = readStream(fileObj);
            readJSON(content);
            fileObj.close();
            progressDialog.dismiss();
        }
        catch (Exception ex)
        {
            Log.e("Warning", "Unable to open JSON file");
        }
    }
    //change the tags to capital letters because they are constants!!
    private void readJSON(String content){
        JSONArray obj = null;
        try {
            JSONObject json = new JSONObject(content);
            String snps_object = json.getString(snps_tag);
            Log.i("readJSON",snps_object);
            JSONObject pal = json.getJSONObject(snps_tag);
            obj = pal.getJSONArray(snps_tag);
            // looping through All albums
            rowItems = new ArrayList<SNP>();
            for (int i = 0; i < obj.length(); i++) {
                JSONObject al = obj.getJSONObject(i);
                String id = al.getString(id_tag);
                String code = al.getString(code_tag);
                String nuc = al.getString(nuc_tag);
                String exp = al.getString(exp_tag);
                Log.d("readJSON", id + " " +code + " " + nuc + " " + exp);
                // get resource id by image name
                //final int resourceId = resources.getIdentifier(imageName, "drawable", context.getPackageName());

                //int resId = getResources().getIdentifier(imageName, "drawable" , getPackageName());
                com.bif812.roman.hackyourlife.SNP items = new com.bif812.roman.hackyourlife.SNP(id,code,nuc,exp);
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







}
