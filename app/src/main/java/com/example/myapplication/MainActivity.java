package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.ErrorListener;

public class MainActivity extends AppCompatActivity {
private RelativeLayout homeRL;
private ProgressBar loadingPB;
private TextView cityNameTv,conditionTV,tempetureTv;
private TextInputEditText cityEdt;
    private ImageView backIv,IconIv,searchIV;
    private RecyclerView RvWeather;
private ArrayList<WetherRVModel>weatherRVAdaptersArrayList;
private  WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE=1;
    private String cityName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        cityNameTv = findViewById(R.id.idTVCityName);
        conditionTV = findViewById(R.id.idTVCondition);
        tempetureTv = findViewById(R.id.idTVTempeture);
        cityEdt = findViewById(R.id.idEdtcity);
        backIv = findViewById(R.id.IVBack);
        IconIv = findViewById(R.id.idIVIcon);
        searchIV = findViewById(R.id.idIVSearch);
        RvWeather = findViewById(R.id.RvWeather);
        weatherRVAdaptersArrayList = new ArrayList<>();
        weatherRVAdapter=new WeatherRVAdapter(this,weatherRVAdaptersArrayList);
       RvWeather.setAdapter(weatherRVAdapter);
       locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
       if(ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this ,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
       }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
       cityName=getCityName(location.getLongitude(),location.getLatitude());
       getWeatherInfo(cityName);

       searchIV.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String City = cityEdt.getText().toString();
               if (City.isEmpty())
               {
                   Toast.makeText(MainActivity.this, "please Enter City Name", Toast.LENGTH_SHORT).show();
               }else
               {
                   cityNameTv.setText(cityName);
                   getWeatherInfo(City);
               }
           }
       });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "permission granted....", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"please provide the permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String  getCityName(double longtuibe, double latitube) {
        String CityName = "Not Found";

        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try{
            List<Address> addresses =gcd.getFromLocation(latitube,longtuibe,10);
            for (Address adr:addresses)
            {
                if (adr!=null)
                {
                    String city=adr.getLocality();
                    if(city!=null && !city.equals(""))
                    {
                        CityName=city;
                    }else
                    {
                        Log.d("TAG","CITY NOT FOUND");
                        Toast.makeText(this, "City Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  CityName;
    }




    private  void getWeatherInfo(String cityName)
    {
    String uri = "http://api.weatherapi.com/v1/forecast.json?key=64e52b3a66c14db0bda44705211611&q="+cityName+"&days=1&aqi=yes&alerts=yes";
//    String uri1="http://api.weatherapi.com/v1/current.json?key=64e52b3a66c14db0bda44705211611&q=London&aqi=yes";
cityNameTv.setText(cityName);
     RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest   = new JsonObjectRequest(Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                weatherRVAdaptersArrayList.clear();
                String tempeture = response.getJSONObject("current").getString("temp_c");

            }

        }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(MainActivity.this, "please enter valid city Name", Toast.LENGTH_SHORT).show();
        }
    });
        requestQueue.add(jsonObjectRequest);

    }


}