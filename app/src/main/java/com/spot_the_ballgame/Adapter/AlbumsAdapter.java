package com.spot_the_ballgame.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spot_the_ballgame.Model.Album;
import com.spot_the_ballgame.R;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Album> albumList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }

    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Album album = albumList.get(position);
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
