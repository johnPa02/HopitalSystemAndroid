package com.example.hospitalsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hospitalsystem.databinding.ActivityDetailBookingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class DetailBookingActivity extends AppCompatActivity {
    private ActivityDetailBookingBinding binding;
    FirebaseFirestore db;
    FirebaseUser user;
    StorageReference storageReference;
    int totalStar;
    int sumRating;
    int rate;
    String drName;
    Bitmap bitmap;
    private String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String doctor = getIntent().getStringExtra("doctor");
        String room = getIntent().getStringExtra("room");
        String id = getIntent().getStringExtra("id");



        binding = ActivityDetailBookingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = user.getEmail();
        storageReference = FirebaseStorage.getInstance().getReference("images/"+userEmail);
        try{
            File f = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    binding.circleImageView2.setImageBitmap(bitmap);
                }
            });
        }catch (IOException e){
        }
        binding.tvTime.setText(time);
        binding.tvDate.setText(date);
        binding.tvRoom.setText(room);

        db.collection("Doctors").document(doctor).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                drName = task.getResult().getString("name");
                String majors = task.getResult().getString("majors");

                totalStar = task.getResult().getLong("totalStar").intValue();
                sumRating = task.getResult().getLong("sumRating").intValue();
                rate = task.getResult().getLong("rating").intValue();

                binding.tvDrName.setText(drName);
                binding.tvMajors.setText(majors);

            }
        });

        binding.drDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PopDrDetail.class);
                i.putExtra("rate",rate);
                startActivity(i);
            }
        });
        binding.rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RatingActivity.class);
                i.putExtra("id",id);
                i.putExtra("doctor",doctor);
                i.putExtra("totalStar",totalStar);
                i.putExtra("sumRating",sumRating);
                startActivity(i);
            }
        });
        binding.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                i.putExtra("name",drName);
                i.putExtra("uid",doctor);
                i.putExtra("image",bitmap);
                startActivity(i);
            }
        });
    }
}