 package com.rush.app;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rush.app.databinding.ActivityCommentsBinding;

import java.util.List;
import java.util.UUID;

 public class CommentsActivity extends AppCompatActivity {
    ActivityCommentsBinding binding;
    private  String postId;
    private  CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentsBinding.inflate(getLayoutInflater());

        postId = getIntent().getStringExtra("id");

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        commentAdapter = new CommentAdapter(this);
        binding.commentsList.setAdapter(commentAdapter);
        binding.commentsList.setLayoutManager(new LinearLayoutManager(this));
        loadComments();


        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = binding.commentText.getText().toString();
                if (comment.trim().length()>0) {
                    comment(comment);
                }
            }
        });







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadComments() {
        FirebaseFirestore.getInstance()
                .collection("Comments")
                .whereEqualTo("postId",postId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        commentAdapter.clearPosts();
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds : dsList) {
                            CommentModel commentModel = ds.toObject(CommentModel.class);
                            commentAdapter.addPost(commentModel);
                        }
                    }
                });

    }


    private void comment(String comment) {
        String id = UUID.randomUUID().toString();
        CommentModel commentModel = new CommentModel(id,postId,FirebaseAuth.getInstance().getUid(),comment);
        FirebaseFirestore.getInstance().collection("Comments")
                .document(id)
                .set(commentModel);
        commentAdapter.addPost(commentModel);
    }
}