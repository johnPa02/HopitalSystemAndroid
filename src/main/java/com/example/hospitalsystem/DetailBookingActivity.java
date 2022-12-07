package com.example.hospitalsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hospitalsystem.databinding.ActivityDetailBookingBinding;

public class DetailBookingActivity extends AppCompatActivity {
    private ActivityDetailBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String drName = getIntent().getStringExtra("drName");
        binding = ActivityDetailBookingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.tvDate.setText(date);
        binding.tvDrName.setText(drName);
        binding.tvTime.setText(time);
        binding.drDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PopDrDetail.class);
                startActivity(i);
            }
        });
    }
}