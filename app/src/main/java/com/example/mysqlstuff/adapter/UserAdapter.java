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
import com.example.mysqlstuff.objects.otherUser;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    /**
     * Declaring Vars
     */

    private Context context;
    private List<otherUser> otherUsers;
    RequestOptions options;

    public UserAdapter(Context context, List<otherUser> otherUsers) {
        this.context = context;
        this.otherUsers = otherUsers;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.ic_account_box_black_24dp).error(R.drawable.ic_account_box_black_24dp);

    }

    /**
     * Creates a view that holds the layout for other Users in a list format
     * @param viewGroup
     * @param i
     * @return
     */

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.user_list_layout,viewGroup, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    /**
     * Applies the parameters of the otherUser object to the created viewholder and prepares the Intent for user interaction.
     * @param myViewHolder
     * @param i
     */

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        myViewHolder.nameTextView.setText(otherUsers.get(i).getOtherUsername());
        Glide.with(context).load(otherUsers.get(i).getOtherProfilePic_url()).apply(options).into(myViewHolder.profilePicture);

        myViewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, OtherUserActivity.class);
                i.putExtra("otherUserId", otherUsers.get(myViewHolder.getAdapterPosition()).getOtherUserId());
                i.putExtra("otherUsername", otherUsers.get(myViewHolder.getAdapterPosition()).getOtherUsername());
                i.putExtra("otherProfilePic", otherUsers.get(myViewHolder.getAdapterPosition()).getOtherProfilePic_url());
                i.putExtra("otherFollowers", otherUsers.get(myViewHolder.getAdapterPosition()).getOtherFollowers());
                i.putExtra("otherBio", otherUsers.get(myViewHolder.getAdapterPosition()).getOtherBio());

                context.startActivity(i);
            }
        });

    }

    /**
     * Returns the size of the otherUsers arraylist
     * @return
     */

    @Override
    public int getItemCount() {
        return otherUsers.size();
    }

    /**
     * Creates the view elements for each variable and assigns them to itemView objects in the layout file.
     */

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView profilePicture;
        LinearLayout view_container;

        public MyViewHolder (View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            profilePicture = itemView.findViewById(R.id.commentProfilePicture);

        }
    }


}
