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
                        binding.textView32.setText("??i???u g?? khi???n b???n ch??a h??i l??ng?");
                        binding.appCompatButton6.setText("B??c s?? kh??m qua loa");
                        binding.appCompatButton.setText("Ch??? l??u");
                        binding.appCompatButton2.setText("Kh??ng nh??? nh??ng");
                        binding.appCompatButton4.setText("Th??i ????? ti??u c???c");
                        binding.appCompatButton3.setText("Ph??ng kh??m d??");
                        break;
                    case 3:
                    case 4:
                        binding.textView32.setText("B???nh vi???n c?? th??? c???i thi???n ??i???u g???");
                        binding.appCompatButton6.setText("B??c s?? gi???i h??n");
                        binding.appCompatButton.setText("Chu ????o h??n");
                        binding.appCompatButton2.setText("Kh??ng ????? ch??? l??u");
                        binding.appCompatButton4.setText("Th??i ????? t???t");
                        binding.appCompatButton3.setText("Ph??ng kh??m s???ch");
                        break;
                    case 5:
                        binding.textView32.setText("??i???u g?? khi???n b???n th??ch?");
                        binding.appCompatButton6.setText("B??c s?? m??t tay");
                        binding.appCompatButton.setText("Th??n thi???n, nhi???t t??nh");
                        binding.appCompatButton2.setText("Chu ????o, c???n th???n");
                        binding.appCompatButton4.setText("Ph??ng kh??m s???ch s???");
                        binding.appCompatButton3.setText("Thi??t b??? hi???n ?????i");
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

                        Toast.makeText(getApplicationContext(),"C???m ??n b???n ???? ????nh gi??",Toast.LENGTH_SHORT).show();
                    }
                });
        db.collection("Doctors").document(doctor).update(doctorDetail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onBackPressed();
                        Toast.makeText(getApplicationContext(),"C???m ??n b???n ???? ????nh gi??",Toast.LENGTH_SHORT).show();
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