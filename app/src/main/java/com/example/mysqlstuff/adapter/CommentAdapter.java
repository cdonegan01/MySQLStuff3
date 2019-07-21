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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mysqlstuff.OtherUserActivity;
import com.example.mysqlstuff.R;
import com.example.mysqlstuff.objects.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

        private Context mContext;
        private List<Comment> mData;
        RequestOptions options;
        private JsonArrayRequest ArrayRequest;
        private String URL_JSON = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/helpfulList.php";

    public CommentAdapter(Context mContext, List<Comment> mData) {
            this.mContext = mContext;
            this.mData = mData;
            options = new RequestOptions().centerCrop().placeholder(R.drawable.ic_account_box_black_24dp).error(R.drawable.ic_account_box_black_24dp);

        }

        @NonNull
        @Override
        public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.comment_list_layout,viewGroup, false);
            final CommentAdapter.MyViewHolder viewHolder = new CommentAdapter.MyViewHolder(view);
            return new CommentAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

            myViewHolder.usernameTextView.setText(mData.get(i).getUserName());
            myViewHolder.commentTextView.setText(mData.get(i).getComment());
            Glide.with(mContext).load(mData.get(i).getUserAvatarURL()).apply(options).into(myViewHolder.gameThumbnail);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

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
