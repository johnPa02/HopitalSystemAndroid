package com.example.hospitalsystem;

import android.graphics.Color;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class AppointmentAdapter extends FirestoreRecyclerAdapter<Appointment,AppointmentAdapter.AppointmentViewHolder> {

    private ItemClickListener itemClickListener;
    private FirebaseUser user;
    public AppointmentAdapter(@NonNull FirestoreRecyclerOptions<Appointment> options,ItemClickListener itemClickListener) {
        super(options);
        this.itemClickListener = itemClickListener;
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
        holder.date.setText(model.getDate());
        holder.sick.setText(model.getSick());
        if (model.getStatus().equals("done")){
            holder.status.setBackground(holder.status.getResources().getDrawable(R.drawable.tv_active));
            holder.status.setTextColor(Color.parseColor("#25be4f"));
        }
        holder.status.setText(model.getStatus());
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getUid().contains("doctor"))
        {
            holder.cancel.setVisibility(View.GONE);
        }
        holder.cancel.setOnClickListener(view -> getSnapshots().getSnapshot(holder.getBindingAdapterPosition()).getReference().delete());
    }

    public interface ItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }
    public class AppointmentViewHolder extends RecyclerView.ViewHolder{
        TextView name,email,date,sick,status;
        Button cancel;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.patient_name);
            email = itemView.findViewById(R.id.patient_email);
            date = itemView.findViewById(R.id.date);
            sick = itemView.findViewById(R.id.sick);
            status = itemView.findViewById(R.id.status);
            cancel = itemView.findViewById(R.id.cancel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    itemClickListener.onItemClick(getSnapshots().getSnapshot(position),position);
                }
            });
        }
    }

}
