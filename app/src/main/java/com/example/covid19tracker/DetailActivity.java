package com.example.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.covid19tracker.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
ActivityDetailBinding binding;
private int positioncountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        positioncountry=intent.getIntExtra("position",0);

        getSupportActionBar().setTitle("Details "+AffectedCountries.countryModelList.get(positioncountry).getCountry());




        binding.tvcountry.setText(AffectedCountries.countryModelList.get(positioncountry).getCountry());
        binding.tvcases.setText(AffectedCountries.countryModelList.get(positioncountry).getCases());
        binding.tvtodaycases.setText(AffectedCountries.countryModelList.get(positioncountry).getTodayCases());
        binding.tvtodaydeaths.setText(AffectedCountries.countryModelList.get(positioncountry).getTodaydeaths());
        binding.tvrecovered.setText(AffectedCountries.countryModelList.get(positioncountry).getRecovered());
        binding.tvcritical.setText(AffectedCountries.countryModelList.get(positioncountry).getCritical());
        binding.tvactive.setText(AffectedCountries.countryModelList.get(positioncountry).getActive());
        binding.tvdeaths.setText(AffectedCountries.countryModelList.get(positioncountry).getDeaths());

    }

    public void gobackbtn(View view) {
        super.onBackPressed();
    }
}