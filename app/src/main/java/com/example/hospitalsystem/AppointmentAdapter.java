package com.example.hospitalsystem;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class AppointmentAdapter extends FirestoreRecyclerAdapter<Appointment,AppointmentAdapter.AppointmentViewHolder> {



    public AppointmentAdapter(@NonNull FirestoreRecyclerOptions<Appointment> options) {
        super(options);
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment,parent,false);

        return new AppointmentViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position, @NonNull Appointment model) {
        holder.name.setText(model.getPatientName());
        holder.email.setText(model.getEmail());
        holder.phone.setText(model.getPhone());
        holder.desc.setText(model.getDesc());
        if (model.getStatus().equals("done")){
            holder.status.setBackground(holder.status.getResources().getDrawable(R.drawable.tv_active));
            holder.status.setTextColor(Color.parseColor("#25be4f"));
        }
        holder.status.setText(model.getStatus());
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder{
        TextView name,email,phone,desc,status;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.patient_name);
            email = itemView.findViewById(R.id.patient_email);
            phone = itemView.findViewById(R.id.phone);
            desc = itemView.findViewById(R.id.description);
            status = itemView.findViewById(R.id.status);
        }
    }

}
