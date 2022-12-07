package com.example.hospitalsystem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.hospitalsystem.databinding.FragmentPatientProfileBinding;
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
import java.util.HashMap;
import java.util.Map;

public class PatientProfileFragment extends Fragment {
    FirebaseFirestore db;
    FirebaseUser user;
    private String userEmail; // use userUid instead
    Uri selectedImage;
    StorageReference storageReference;
    FragmentPatientProfileBinding binding;

    public PatientProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = user.getEmail();
        storageReference = FirebaseStorage.getInstance().getReference("images/"+userEmail);

        loadUserProfile();

        binding = FragmentPatientProfileBinding.inflate(inflater,container,false);

        ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri!=null){
                            binding.circleImageView.setImageURI(uri);
                            selectedImage = uri;
                        }

                    }
                });
        binding.changeAvt.setOnClickListener(view -> launcher.launch("image/*"));
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.profile.setVisibility(View.GONE);
                Fragment notificationFragmentFragment = new NotificationFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.scroll,notificationFragmentFragment).commit();
            }
        });
        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData(binding.nameEt.getText().toString(),binding.bdEt.getText().toString()
                        ,binding.phoneEt.getText().toString()
                    ,binding.addressEt.getText().toString(),binding.cmnd.getText().toString());
            }
        });

        return binding.getRoot();
    }

    private void loadUserProfile() {
        try{
            File f = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    binding.circleImageView.setImageBitmap(bitmap);
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
                            binding.progressBar.setVisibility(View.GONE);
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            binding.emailEt.setText(documentSnapshot.get("email").toString());
                            binding.nameEt.setText(documentSnapshot.getString("name"));
                            binding.bdEt.setText(documentSnapshot.getString("bd"));
                            binding.phoneEt.setText(documentSnapshot.getString("phone"));
                            binding.addressEt.setText(documentSnapshot.getString("address"));
                            binding.cmnd.setText(documentSnapshot.getString("cccd"));
                        }
                    }
                });
    }

    public void updateData(String name, String bd, String phone, String address, String cccd) {
        Map<String, Object> userDetail = new HashMap<>();
        userDetail.put("name", name);
//        userDetail.put("email",email);
        userDetail.put("bd", bd);
        userDetail.put("phone", phone);
        userDetail.put("address", address);
        userDetail.put("cccd", cccd);

        storageReference.putFile(selectedImage);

        db.collection("Users").whereEqualTo("email", userEmail)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            db.collection("Users").document(documentId)
                                    .update(userDetail);
                        }
                    }
                });
    }
}