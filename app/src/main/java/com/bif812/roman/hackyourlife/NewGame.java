package com.bif812.roman.hackyourlife;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bif812.roman.hackyourlife.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Roman on 3/6/2018.
 */

public class NewGame extends AppCompatActivity {
    // Constants:
    final int REQUEST_CODE = 123;
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    // App ID to use OpenWeather data
    final String APP_ID = "025fb2ff5d24e00a9073e4da87d75e1c";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;

    // Member Variables:
    //TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;


    LocationManager mLocationManager;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_start);

        //variables for weather components
        //mCityLabel = (TextView) findViewById(R.id.cityName);
        mWeatherImage = (ImageView) findViewById(R.id.weatherPic);
        mTemperatureLabel = (TextView) findViewById(R.id.currentTemp);

        //All the options menu
        ImageView aminoGame = (ImageView) findViewById(R.id.aminoBut);
        ImageView pedometer = (ImageView) findViewById(R.id.pedomBut);
        ImageView meditate = (ImageView) findViewById(R.id.meditateBut);
        ImageView toDoList = (ImageView) findViewById(R.id.listBut);

        //Intents for all options
        aminoGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewGame.this, AminoGame.class);
                startActivity(myIntent);
            }
        });

        pedometer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(NewGame.this, Steps.class);
                startActivity(myIntent);
            }
        });

        meditate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewGame.this, Meditation.class);
                startActivity(myIntent);
            }
        });
        toDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("todolist", "Clicked on the to do list");
                Intent myIntent = new Intent (NewGame.this, ToDoList.class);
                startActivity(myIntent);
            }
        });

    }

    protected void onResume() {
        super.onResume();
        Log.d("Clima", "OnResume() called");


        Intent myIntent = getIntent();
        String city = myIntent.getStringExtra("City");

        if (city != null){
            getWeatherForNewCity(city);

        } else{
            Log.d("Clima", "Getting weather for location");
            getWeatherForCurrentLocation();
        }

    }

    //gets weather for the selected city here
    private void getWeatherForNewCity(String city){
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", APP_ID);
        letsDoSomeNetworking(params);
    }


    //uses phone system settings to get longitude and latitude data
    private void getWeatherForCurrentLocation() {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Clima", "onLocationChanged() callback received");

                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());

                Log.d("Clima", "longitude : " + longitude);
                Log.d("Clima", "latitude : " + latitude);

                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", APP_ID);
                letsDoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Clima", "onProviderDisabled() callback received");
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);

            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Clima", "onRequestPermissionsResult(): Granted");
                getWeatherForCurrentLocation();
            } else{
                Log.d("Clima","Permission denied =( ");
            }
        }

    }

    // connects to the API and gets the JSON data
    private void letsDoSomeNetworking(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(WEATHER_URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Clima", "Success! JSON: " + response.toString());

                WeatherDataModel weatherData = WeatherDataModel.fromJSON(response);

                    updateUI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response){
                Log.e("Clima", "Fail " + e.toString());
                Log.d("Clima", "Status code "+ statusCode);
                Toast.makeText(NewGame.this, "Weather Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // updates the UI for the Weather
    private void updateUI(WeatherDataModel weather){

        //String currentCity = weather.getCity();
        //mCityLabel.setText(currentCity);
        mTemperatureLabel.setText(weather.getTemperature());

        int resourceID = getResources().getIdentifier(weather.getIconName(), "drawable", getPackageName());
        mWeatherImage.setImageResource(resourceID);
    }



    @Override
    protected void onPause() {
        super.onPause();

        if (mLocationManager !=null) mLocationManager.removeUpdates(mLocationListener);
    }

}
