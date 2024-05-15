package com.rush.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private Context context;
    private List<PostModel> postModelList;

    public PostAdapter(Context context) {
        this.context = context;
        postModelList = new ArrayList<>();
    }

    public void addPost(PostModel postModel) {
        postModelList.add(postModel);
        notifyDataSetChanged();
    }

    public  void clearPosts() {
        postModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.MyViewHolder holder, int position) {
        PostModel postModel = postModelList.get(position);
        if(postModel.getPostImage() != null) {
            holder.postImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(postModel.getPostImage()).into(holder.postImage);
        }
        else {
            holder.postImage.setVisibility(View.GONE);
        }
        holder.postText.setText(postModel.getPostText());

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("id", postModel.getPostId());
                context.startActivity(intent);

            }
        });



        FirebaseFirestore.getInstance().collection("Likes")
                        .document(postModel.getPostId()+FirebaseAuth.getInstance().getUid())
                        .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot != null) {
                                            String data = documentSnapshot.getString("number");
                                            if(data != null) {
                                                postModel.setLiked(true);
                                                holder.like.setImageResource(R.drawable.like_image_pur);
                                            }
                                            else {
                                                postModel.setLiked(false);
                                                holder.like.setImageResource(R.drawable.like_image);
                                            }
                                        }
                                        else {
                                            postModel.setLiked(false);
                                            holder.like.setImageResource(R.drawable.like_image);
                                        }
                                    }
                                });


        //postModel.setLiked(false);
        holder.clickProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("id", postModel.getUserId());
                context.startActivity(intent);
            }
        });


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postModel.isLiked()){
                    postModel.setLiked(false);
                    holder.like.setImageResource(R.drawable.like_image);
                    FirebaseFirestore.getInstance()
                            .collection("Likes")
                            .document(postModel.getPostId()+FirebaseAuth.getInstance().getUid())
                            .delete();
                }
                else {
                    postModel.setLiked(true);
                    holder.like.setImageResource(R.drawable.like_image_pur);
                    FirebaseFirestore.getInstance()
                            .collection("Likes")
                            .document(postModel.getPostId()+FirebaseAuth.getInstance().getUid())
                            .set(new PostModel("1"));
                }
            }
        });

        String uid = postModel.getUserId();
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                        if (userModel.getUserProfile()!=null) {
                            Glide.with(context).load(userModel.getUserProfile()).into(holder.UseProfileImage);
                        }
                        holder.UserName.setText(userModel.getUserName());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView UserName, postText;
        private ImageView UseProfileImage, postImage, like, comment;
        private RelativeLayout clickProfile;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.UserName);
            postText = itemView.findViewById(R.id.postText);
            UseProfileImage = itemView.findViewById(R.id.UseProfileImage);
            postImage = itemView.findViewById(R.id.postImage);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            clickProfile = itemView.findViewById(R.id.clickProfile);

        }
    }
}

