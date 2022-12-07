package com.example.hospitalsystem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationFragment extends Fragment {

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private CircleImageView avt;
    private ConstraintLayout notification;
    private FrameLayout frameLayout;
    private TextView name;
    ConstraintLayout splashScreen;
    FirebaseFirestore db;
    FirebaseUser user;
    StorageReference storageReference;

    private String userEmail; // use userUid instead
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        avt = view.findViewById(R.id.avatar_cimg);
        notification = view.findViewById(R.id.notification);
        frameLayout = view.findViewById(R.id.notification_layout);
        splashScreen = view.findViewById(R.id.splashscreen);

        name = view.findViewById(R.id.name_tv);
        loadUserName();
        avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification.setVisibility(View.GONE);
                Fragment patientProfileFragment = new PatientProfileFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.notification_layout,patientProfileFragment).commit();
            }
        });
        return view;
    }

    private void loadUserName() {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = user.getEmail();
        storageReference = FirebaseStorage.getInstance().getReference("images/"+userEmail);
        try{
            File f = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    avt.setImageBitmap(bitmap);
                    splashScreen.setVisibility(View.GONE);
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }

        db.collection("Users").whereEqualTo("email",userEmail)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            name.setText(documentSnapshot.getString("name"));
                        }
                    }
                });
    }
}