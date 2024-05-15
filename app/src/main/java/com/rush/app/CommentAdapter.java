package com.rush.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private List<CommentModel> postModelList;

    public CommentAdapter(Context context) {
        this.context = context;
        postModelList = new ArrayList<>();
    }

    public void addPost(CommentModel postModel) {
        postModelList.add(postModel);
        notifyDataSetChanged();
    }

    public  void clearPosts() {
        postModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coment_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        CommentModel commentModel = postModelList.get(position);
        holder.CommentText.setText(commentModel.getComment());



        String uid = commentModel.getUserId();
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                        if (userModel.getUserProfile()!=null) {
                            Glide.with(context).load(userModel.getUserProfile()).into(holder.UseProfileImage1);
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
        public ImageView UseProfileImage1;
        private TextView UserName, CommentText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.UserName1);
            CommentText = itemView.findViewById(R.id.comment);
            UseProfileImage1 = itemView.findViewById(R.id.UseProfileImage1);
        }
    }
}

