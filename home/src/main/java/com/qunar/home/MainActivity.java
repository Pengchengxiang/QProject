package com.qunar.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qunar.flight.FlightActivity;
import com.qunar.hotel.controller.HotelActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toHotel(View view) {
        Intent intent = new Intent(this, HotelActivity.class);
        startActivity(intent);
    }

    public void toFlight(View view) {
        Intent intent = new Intent(this, FlightActivity.class);
        startActivity(intent);
    }

    public void toSmartUpdate(View view) {
        Intent intent = new Intent(this, SmartUpdateActivity.class);
        startActivity(intent);
    }

    public void toClassLoader(View view) {
        Intent intent = new Intent(this, ClassLoaderActivity.class);
        startActivity(intent);
    }
}
