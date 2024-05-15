package com.rush.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.rush.app.databinding.ActivityNewsFeedBinding;

import java.util.List;

public class NewsFeedActivity extends AppCompatActivity {

    ActivityNewsFeedBinding binding;
    private  PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewsFeedBinding.inflate(getLayoutInflater());
        postAdapter = new PostAdapter(this);
        binding.postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadPosts();

        binding.imageviewX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NewsFeedActivity.this, ProfileActivity.class);
                intent.putExtra("id", FirebaseAuth.getInstance().getUid());
                startActivity(intent);
            }
        });


        setContentView(binding.getRoot());

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                                if(userModel.getUserProfile() != null) {
                                    Glide.with(NewsFeedActivity.this).load(userModel.getUserProfile()).into(binding.imageviewX   );

                                }
                            }
                        });




        binding.goCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivities(new Intent[]{new Intent(NewsFeedActivity.this, CreatePostActivity.class)});
            }
        });




        setContentView(R.layout.activity_news_feed);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private boolean OnCreateoptionManu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_out_manu, menu);
        return true;
    }

    public  boolean OnOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivities(new Intent[]{new Intent(NewsFeedActivity.this, loginActivity.class)});
            finish();
            return true;
        }
        return false;
    }

    private void loadPosts() {
        FirebaseFirestore.getInstance()
                .collection("posts")
                .orderBy("postingTime")
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
                        Toast.makeText(NewsFeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    };

}