package com.example.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19tracker.databinding.ActivityMainBinding;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SimpleArcLoader simpleArcLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null || !networkInfo.isConnected() || !networkInfo.isAvailable())
        {
            final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Your Internet Connection is 'OFF',Turn ON your Internet Connection and Reopen the app");
            builder.setCancelable(false);
            builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    dialog.cancel();
                    finish();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
        else {
            Toast.makeText(MainActivity.this,"Good,Your Internet Connection is ON",Toast.LENGTH_LONG).show();
        }
        final ProgressDialog dialog= ProgressDialog.show(this,"Important", "Welcome to Covid19 Tracker App! Wear Mask and Stay Safe !",true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    dialog.dismiss();
                }
                catch(InterruptedException ex){
                    ex.printStackTrace();
                }
            }
        }).start();


        fetchData();

    }

    private void fetchData() {
        String url = "https://corona.lmao.ninja/v2/all/";
        binding.loader.start();
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.toString());
                    binding.tvcases.setText(jsonObject.getString("cases"));
                    binding.tvrecovered.setText(jsonObject.getString("recovered"));
                    binding.tvcritical.setText(jsonObject.getString("critical"));
                    binding.tvactive.setText(jsonObject.getString("active"));
                    binding.tvtodaycases.setText(jsonObject.getString("todayCases"));
                    binding.tvtotaldeaths.setText(jsonObject.getString("deaths"));
                    binding.tvtodaydeaths.setText(jsonObject.getString("todayDeaths"));
                    binding.tvaffectedcountries.setText(jsonObject.getString("affectedCountries"));

                    binding.piechart.addPieSlice(new PieModel("Cases",Integer.parseInt(binding.tvcases.getText().toString()), Color.parseColor("#FFA726")));
                    binding.piechart.addPieSlice(new PieModel("Recovered",Integer.parseInt(binding.tvrecovered.getText().toString()), Color.parseColor("#66BB6A")));
                    binding.piechart.addPieSlice(new PieModel("Deaths",Integer.parseInt(binding.tvtotaldeaths.getText().toString()), Color.parseColor("#EF5350")));
                    binding.piechart.addPieSlice(new PieModel("Active",Integer.parseInt(binding.tvactive.getText().toString()), Color.parseColor("#29B6F6")));
               binding.piechart.startAnimation();
               binding.loader.stop();
               binding.loader.setVisibility(View.GONE);
               binding.scrollstats.setVisibility(View.VISIBLE);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    binding.loader.stop();
                    binding.loader.setVisibility(View.GONE);
                    binding.scrollstats.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.loader.stop();
                binding.loader.setVisibility(View.GONE);
                binding.scrollstats.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void goTrackCountries(View view) {
        startActivity(new Intent(getApplicationContext(),AffectedCountries.class));
        Toast.makeText(MainActivity.this,"Select a country and see current situation",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to exit");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finishAffinity();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}