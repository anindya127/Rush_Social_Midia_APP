package com.rush.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rush.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.name.getText().toString();
                String number = binding.number.getText().toString();
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();

                progressDialog = new ProgressDialog(MainActivity.this);

                progressDialog.setTitle("Registering User");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                binding.gotologin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivities(new Intent[]{new Intent(MainActivity.this, loginActivity.class)});
                    }
                });

                FirebaseAuth
                        .getInstance()
                        .createUserWithEmailAndPassword(email.trim(), password.trim())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();
                                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
                                new MySharedPerformances(MainActivity.this).setMyData(number);
                                UserModel userModel = new UserModel();
                                userModel.setUserName(name);
                                userModel.setUserNumber(number);
                                userModel.setUserEmail(email);

                                FirebaseFirestore
                                        .getInstance()
                                        .collection("users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .set(userModel);
                                reset();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void reset() {
        progressDialog.dismiss();
        Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
    }
}