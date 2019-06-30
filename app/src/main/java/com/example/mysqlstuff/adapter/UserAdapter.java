package com.example.mysqlstuff.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mysqlstuff.OtherUserActivity;
import com.example.mysqlstuff.R;
import com.example.mysqlstuff.model.otherUser;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private Context mContext;
    private List<otherUser> mData;
    RequestOptions options;

    public UserAdapter(Context mContext, List<otherUser> mData) {
        this.mContext = mContext;
        this.mData = mData;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.user_list_layout,viewGroup, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        myViewHolder.nameTextView.setText(mData.get(i).getOtherUsername());
        myViewHolder.followerTextView.setText(mData.get(i).getOtherFollowers()+" Followers");
        Glide.with(mContext).load(mData.get(i).getOtherProfilePic_url()).apply(options).into(myViewHolder.profilePicture);

        myViewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, OtherUserActivity.class);
                i.putExtra("otherUserId",mData.get(myViewHolder.getAdapterPosition()).getOtherUserId());
                i.putExtra("otherUsername",mData.get(myViewHolder.getAdapterPosition()).getOtherUsername());
                i.putExtra("otherPronoun",mData.get(myViewHolder.getAdapterPosition()).getOtherPronoun());
                i.putExtra("otherProfilePic",mData.get(myViewHolder.getAdapterPosition()).getOtherProfilePic_url());
                i.putExtra("otherName",mData.get(myViewHolder.getAdapterPosition()).getOtherName());
                i.putExtra("otherLocation",mData.get(myViewHolder.getAdapterPosition()).getOtherLocation());
                i.putExtra("otherFollowers",mData.get(myViewHolder.getAdapterPosition()).getOtherFollowers());
                i.putExtra("otherEmail",mData.get(myViewHolder.getAdapterPosition()).getOtherEmail());
                i.putExtra("otherBio",mData.get(myViewHolder.getAdapterPosition()).getOtherBio());

                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView followerTextView;
        ImageView profilePicture;
        LinearLayout view_container;

        public MyViewHolder (View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            followerTextView = itemView.findViewById(R.id.commentTextView);
            profilePicture = itemView.findViewById(R.id.commentProfilePicture);

        }
    }


}
