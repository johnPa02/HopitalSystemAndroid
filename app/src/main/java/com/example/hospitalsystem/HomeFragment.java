package com.example.hospitalsystem;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



public class HomeFragment extends Fragment{

    private AppointmentAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirestoreRecyclerOptions<Appointment> options = new FirestoreRecyclerOptions.Builder<Appointment>()
                .setQuery(FirebaseFirestore.getInstance().collection("Appointments"),Appointment.class).build();
        adapter = new AppointmentAdapter(options, new AppointmentAdapter.ItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Appointment appointment = documentSnapshot.toObject(Appointment.class);
                String date = documentSnapshot.getString("date");
                String drName = documentSnapshot.getString("drName");
                String time = documentSnapshot.getString("time");
                Intent intent = new Intent(getActivity(), DetailBookingActivity.class);
                intent.putExtra("date",date);
                intent.putExtra("drName",drName);
                intent.putExtra("time",time);
                startActivity(intent);
            }
        });

        View view = inflater.inflate(R.layout.fragment_home,container,false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}