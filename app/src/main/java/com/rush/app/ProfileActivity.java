package com.rush.app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rush.app.databinding.ActivityProfileBinding;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    private PostAdapter postAdapter;
    private  String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        postAdapter = new PostAdapter(this);
        binding.postsList.setAdapter(postAdapter);
        binding.postsList.setLayoutManager(new LinearLayoutManager(this));

        userId = getIntent().getStringExtra("id");

        loadUserData();
        loadPosts();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadUserData() {
        binding.UserName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                        if (userModel.getUserBio() != null) {
                            binding.bio.setText(userModel.getUserBio());
                        }
                        else {
                            binding.bio.setText(R.string.Bio);
                        }

                        if (userModel.getUserProfile() != null) {
                            Glide.with(ProfileActivity.this)
                                    .load(userModel.getUserProfile())
                                    .into(binding.profilePhoto);
                        }

                        if (userModel.getUserCover() != null) {
                            Glide.with(ProfileActivity.this)
                                    .load(userModel.getUserCover())
                                    .into(binding.coverPhoto);
                        }


                    }
                });

    }

    private void loadPosts() {
        FirebaseFirestore.getInstance()
                .collection("posts")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        postAdapter.clearPosts();
                        List<DocumentSnapshot> dList = queryDocumentSnapshots.getDocuments();

                        for(DocumentSnapshot d : dList) {
                            PostModel postModel = d.toObject(PostModel.class);
                            postAdapter.addPost(postModel);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    };
}