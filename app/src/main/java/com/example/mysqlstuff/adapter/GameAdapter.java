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
import com.example.mysqlstuff.GameActivity;
import com.example.mysqlstuff.objects.Game;
import com.example.mysqlstuff.R;

import java.util.List;

/**
 * This class applies the Game format to RecyclerViews that display Games.
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {

    /**
     * Declaring Vars
     */

    private Context context;
    private List<Game> games;
    RequestOptions options;

    /**
     * Constructor for GameAdapter
     * @param context
     * @param games
     */

    public GameAdapter(Context context, List<Game> games) {
        this.context = context;
        this.games = games;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.ic_account_box_black_24dp).error(R.drawable.ic_account_box_black_24dp);

    }

    /**
     * Creates a view that holds the layout for Games in a list format
     * @param viewGroup
     * @param i
     * @return
     */

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.gamelist_layout,viewGroup, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    /**
     * Applies the parameters of the Game object to the created viewholder and prepares the Intent for user interaction.
     * @param myViewHolder
     * @param i
     */

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        myViewHolder.game_name.setText(games.get(i).getTitle());
        myViewHolder.game_developer.setText(games.get(i).getDeveloper());
        myViewHolder.game_year.setText(games.get(i).getReleaseYear());

        Glide.with(context).load(games.get(i).getImage_url()).apply(options).into(myViewHolder.img_thumbnail);

        myViewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, GameActivity.class);
                i.putExtra("id", games.get(myViewHolder.getAdapterPosition()).getGameId());
                i.putExtra("title", games.get(myViewHolder.getAdapterPosition()).getTitle());
                i.putExtra("description", games.get(myViewHolder.getAdapterPosition()).getDescription());
                i.putExtra("release_year", games.get(myViewHolder.getAdapterPosition()).getReleaseYear());
                i.putExtra("developer", games.get(myViewHolder.getAdapterPosition()).getDeveloper());
                i.putExtra("cover_art", games.get(myViewHolder.getAdapterPosition()).getImage_url());

                context.startActivity(i);
            }
        });

    }

    /**
     * Returns the size of the Games arraylist
     * @return
     */

    @Override
    public int getItemCount() {
        return games.size();
    }

    /**
     * Creates the view elements for each variable and assigns them to an itemView objects in the layout file.
     */

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView game_name;
        TextView game_developer;
        TextView game_year;
        ImageView img_thumbnail;
        LinearLayout view_container;

        public MyViewHolder (View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            game_name = itemView.findViewById(R.id.rowName);
            game_developer = itemView.findViewById(R.id.developer);
            game_year = itemView.findViewById(R.id.releaseYearID);
            img_thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }
}
