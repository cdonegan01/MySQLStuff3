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
import com.example.mysqlstuff.activities.GameActivity;
import com.example.mysqlstuff.objects.Game;
import com.example.mysqlstuff.R;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {

    private Context mContext;
    private List<Game> mData;
    RequestOptions options;

    public RvAdapter(Context mContext, List<Game> mData) {
        this.mContext = mContext;
        this.mData = mData;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.gamelist_layout,viewGroup, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        myViewHolder.game_name.setText(mData.get(i).getTitle());
        myViewHolder.game_rating.setText(mData.get(i).getAverageRating());
        myViewHolder.game_developer.setText(mData.get(i).getDeveloper());
        myViewHolder.game_year.setText(mData.get(i).getReleaseYear());

        Glide.with(mContext).load(mData.get(i).getImage_url()).apply(options).into(myViewHolder.img_thumbnail);

        myViewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, GameActivity.class);
                i.putExtra("id",mData.get(myViewHolder.getAdapterPosition()).getGameId());
                i.putExtra("title",mData.get(myViewHolder.getAdapterPosition()).getTitle());
                i.putExtra("description",mData.get(myViewHolder.getAdapterPosition()).getDescription());
                i.putExtra("average_rating",mData.get(myViewHolder.getAdapterPosition()).getAverageRating());
                i.putExtra("release_year",mData.get(myViewHolder.getAdapterPosition()).getReleaseYear());
                i.putExtra("developer",mData.get(myViewHolder.getAdapterPosition()).getDeveloper());
                i.putExtra("cover_art",mData.get(myViewHolder.getAdapterPosition()).getImage_url());

                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView game_name;
        TextView game_developer;
        TextView game_rating;
        TextView game_year;
        ImageView img_thumbnail;
        LinearLayout view_container;

        public MyViewHolder (View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            game_name = itemView.findViewById(R.id.rowName);
            game_developer = itemView.findViewById(R.id.developer);
            game_rating = itemView.findViewById(R.id.rating);
            game_year = itemView.findViewById(R.id.releaseYearID);
            img_thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }
}
