package com.dot.gallery.adapters;

import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dot.gallery.R;
import com.dot.gallery.activities.PhotosActivity;
import com.dot.gallery.model.AlbumCard;
import com.dot.gallery.views.DeleteSheet;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<AlbumCard> mList;
    private FragmentActivity mActivity;

    public AlbumAdapter(FragmentActivity activity, List<AlbumCard> mList) {
        this.mActivity = activity;
        this.mList = mList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        AlbumCard app = mList.get(position);
        Glide.with(holder.img)
                .load(app.getPath())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .into(holder.img);
        holder.name.setText(app.getName());
        holder.count.setText(app.getCount());
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, PhotosActivity.class);
            intent.putExtra("album", app.getName());
            mActivity.startActivity(intent);
        });
        holder.cardView.setOnLongClickListener(v -> {
            DeleteSheet bt = new DeleteSheet();
            bt.isAlbum(getAlbumPath(app.getPath()), app.getPath(), app.getName());
            bt.setFragment(mActivity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.fragment_pager + ":" + 0));
            bt.show(mActivity.getSupportFragmentManager(), "no");
            return false;
        });
    }

    private String getAlbumPath(String fullPath) {
        return fullPath.replace(fullPath.substring(fullPath.lastIndexOf("/")), "");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView img;
        TextView name, count;

        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.imgcard2);
            img = view.findViewById(R.id.imgag);
            name = view.findViewById(R.id.name);
            count = view.findViewById(R.id.count);
        }
    }


}