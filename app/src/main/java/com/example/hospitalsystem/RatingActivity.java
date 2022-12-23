package com.example.hospitalsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.hospitalsystem.databinding.ActivityDetailBookingBinding;
import com.example.hospitalsystem.databinding.ActivityRatingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingActivity extends AppCompatActivity{
    private ActivityRatingBinding binding;
    private Boolean[] buttonState = {false,false,false,false,false};
    FirebaseFirestore db;
    String id,doctor;
    ArrayList<AppCompatButton> buttons;
    int totalStar, sumRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        binding = ActivityRatingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        id = getIntent().getStringExtra("id");
        doctor = getIntent().getStringExtra("doctor");
        db = FirebaseFirestore.getInstance();
        buttons = new ArrayList<>();
        buttons.add(binding.appCompatButton);
        buttons.add(binding.appCompatButton2);
        buttons.add(binding.appCompatButton3);
        buttons.add(binding.appCompatButton4);
        buttons.add(binding.appCompatButton6);

        totalStar = getIntent().getIntExtra("totalStar",0);
        sumRating = getIntent().getIntExtra("sumRating",0);


        for (int i=0;i<buttonState.length;i++) {
            int finalI = i;
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(buttonState[finalI]) {
                        buttons.get(finalI).setBackgroundResource(R.color.default_);
                        buttonState[finalI] = false;
                    }
                    else{
                        buttons.get(finalI).setBackgroundResource(R.color.blue);
                        buttonState[finalI] = true;
                    }
                }
            });
        }
        binding.submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRating();
            }
        });

        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rating = (int)v;
                switch (rating){
                    case 1:
                    case 2:
                        binding.textView32.setText("Điều gì khiến bạn chưa hài lòng?");
                        binding.appCompatButton6.setText("Bác sĩ khám qua loa");
                        binding.appCompatButton.setText("Chờ lâu");
                        binding.appCompatButton2.setText("Không nhẹ nhàng");
                        binding.appCompatButton4.setText("Thái độ tiêu cực");
                        binding.appCompatButton3.setText("Phòng khám dơ");
                        break;
                    case 3:
                    case 4:
                        binding.textView32.setText("Bệnh viện có thể cải thiện điều gì?");
                        binding.appCompatButton6.setText("Bác sĩ giỏi hơn");
                        binding.appCompatButton.setText("Chu đáo hơn");
                        binding.appCompatButton2.setText("Không để chờ lâu");
                        binding.appCompatButton4.setText("Thái độ tốt");
                        binding.appCompatButton3.setText("Phòng khám sạch");
                        break;
                    case 5:
                        binding.textView32.setText("Điều gì khiến bạn thích?");
                        binding.appCompatButton6.setText("Bác sĩ mát tay");
                        binding.appCompatButton.setText("Thân thiện, nhiệt tình");
                        binding.appCompatButton2.setText("Chu đáo, cẩn thận");
                        binding.appCompatButton4.setText("Phòng khám sạch sẽ");
                        binding.appCompatButton3.setText("Thiét bị hiện đại");
                        break;
                }
            }
        });
    }



    private void saveRating() {
        Map<String, Object> apmDetail = new HashMap<>();
        Map<String, Object> doctorDetail = new HashMap<>();
        String comment = "";
        int rating = (int)binding.ratingBar.getRating();

        sumRating += 1;
        totalStar += binding.ratingBar.getRating();
        double rate = Math.ceil(totalStar/sumRating);
        doctorDetail.put("totalStar",totalStar);
        doctorDetail.put("sumRating",sumRating);
        doctorDetail.put("rating",(int)rate);

        for(int i=0;i<buttonState.length;i++){
            if(buttonState[i]){
                comment += buttons.get(i).getText().toString()+", ";
            }
        }
        comment+= binding.bonusComment.getText().toString();

        apmDetail.put("comment", comment);
        apmDetail.put("rating", rating);



        db.collection("Appointments").document(id).update(apmDetail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(getApplicationContext(),"Cảm ơn bạn đã đánh giá",Toast.LENGTH_SHORT).show();
                    }
                });
        db.collection("Doctors").document(doctor).update(doctorDetail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onBackPressed();
                        Toast.makeText(getApplicationContext(),"Cảm ơn bạn đã đánh giá",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}