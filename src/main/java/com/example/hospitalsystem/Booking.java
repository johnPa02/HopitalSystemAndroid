package com.example.hospitalsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Booking extends AppCompatActivity {
    private EditText pickDate;
    private ImageView calenderBtn;
    int year,month,day;
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView sickAct;
    private String[] times = {"7:00 - 9:00","9:00 - 11:00","13:00 - 15h:00","15:00 - 17:00"};
    private String[] sicks = {"Thần kinh","Ung bướu","Nhi khoa","Tai mũi họng","Mắt"
            ,"Ngoại tổng hợp","Tim mạch","Tiêu hóa","Khoa sản"};
    private ArrayAdapter<String> adapterTimes;
    private ArrayAdapter<String> adapterSicks;

    private Toolbar toolbar;

    private EditText etName,etEmail,etPhone,etDate,etDsc;
    private AutoCompleteTextView actTime,actSick;
    private Button bookBtn;
    private FirebaseFirestore db;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        pickDate = findViewById(R.id.pickDate);
        calenderBtn = findViewById(R.id.calendarBtn);
        final Calendar calendar = Calendar.getInstance();
        calenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Booking.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        pickDate.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        autoCompleteTextView = findViewById(R.id.time_act);
        adapterTimes = new ArrayAdapter<String>(this,R.layout.list_times,times);
        autoCompleteTextView.setAdapter(adapterTimes);

        sickAct = findViewById(R.id.sick_act);
        adapterSicks = new ArrayAdapter<String>(this,R.layout.list_times,sicks);
        sickAct.setAdapter(adapterSicks);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đặt lịch khám");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etDate = findViewById(R.id.pickDate);
        actTime = findViewById(R.id.time_act);
        actSick = findViewById(R.id.sick_act);
        etDsc = findViewById(R.id.etDsc);
        bookBtn = findViewById(R.id.bookBtn);
        user = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

    }
    public void insertData(){
        Map<String,String> items = new HashMap<>();
        items.put("patientName",etName.getText().toString().trim());
        items.put("email",etEmail.getText().toString().trim());
        items.put("phone",etPhone.getText().toString().trim());
        items.put("date",etDate.getText().toString().trim());
        items.put("time",actTime.getText().toString());
        items.put("sick",actSick.getText().toString());
        items.put("desc",etDsc.getText().toString());
        items.put("status","waiting");
        items.put("uid", user.getEmail());

        db.collection("Appointments").add(items)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        onBackPressed();
                    }
                });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}