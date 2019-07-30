package com.example.mysqlstuff.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mysqlstuff.R;
import com.example.mysqlstuff.objects.Comment;

import java.util.List;

/**
 * This class applies the Comment format to RecyclerViews that display comments.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    /**
     * Declaring Vars
     */

    private Context context;
    private List<Comment> comments;
    RequestOptions options;
    private JsonArrayRequest ArrayRequest;

    /**
     * Constructor for CommentAdapter
     * @param context
     * @param comments
     */

    public CommentAdapter(Context context, List<Comment> comments) {
            this.context = context;
            this.comments = comments;
            options = new RequestOptions().centerCrop().placeholder(R.drawable.ic_account_box_black_24dp).error(R.drawable.ic_account_box_black_24dp);

        }

    /**
     * Creates a view that holds the layout for Comments
     * @param viewGroup
     * @param i
     * @return
     */

    @NonNull
        @Override
        public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.comment_list_layout,viewGroup, false);
            final CommentAdapter.MyViewHolder viewHolder = new CommentAdapter.MyViewHolder(view);
            return new CommentAdapter.MyViewHolder(view);
        }

    /**
     * Applies the parameters of the Comment object to the created viewholder.
     * @param myViewHolder
     * @param i
     */

    @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
            myViewHolder.usernameTextView.setText(comments.get(i).getUserName());
            myViewHolder.commentTextView.setText(comments.get(i).getComment());
            Glide.with(context).load(comments.get(i).getUserAvatarURL()).apply(options).into(myViewHolder.gameThumbnail);
        }

    /**
     * Returns the size of the Comments arraylist
     * @return
     */

    @Override
        public int getItemCount() {
            return comments.size();
        }

    /**
     * Creates the view elements for each variable and assigns them to an itemView objects in the layout file.
     */

    public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView usernameTextView;
            TextView commentTextView;
            ImageView gameThumbnail;
            LinearLayout view_container;

            public MyViewHolder (View itemView) {
                super(itemView);

                view_container = itemView.findViewById(R.id.container);
                usernameTextView = itemView.findViewById(R.id.commentUsernameID);
                commentTextView = itemView.findViewById(R.id.commentTextView);
                gameThumbnail = itemView.findViewById(R.id.commentProfilePicture);

            }
        }


}
