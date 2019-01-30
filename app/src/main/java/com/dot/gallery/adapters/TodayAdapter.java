package com.dot.gallery.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dot.gallery.R;
import com.dot.gallery.activities.DetailsActivity;
import com.dot.gallery.activities.VideoActivity;
import com.dot.gallery.views.DeleteSheet;
import com.dot.gallery.model.MediaCard;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

    private List<MediaCard> mList;
    private AppCompatActivity mActivity;
    private FragmentActivity fActivity;

    public TodayAdapter(AppCompatActivity activity, List<MediaCard> mList) {
        this.mList = mList;
        this.mActivity = activity;
    }

    public TodayAdapter(FragmentActivity activity, List<MediaCard> mList) {
        this.mList = mList;
        this.fActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        MediaCard app = mList.get(position);
        if (mActivity != null) {
            if (!app.isVideo) {
                holder.videoMarker.setVisibility(View.GONE);
                Glide.with(holder.img)
                        .load(app.getPath())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.img);
                holder.cardView.setOnClickListener(v -> {
                    if (!app.isSelected()) {
                        Intent intent = new Intent(mActivity, DetailsActivity.class);
                        ArrayList<String> paths = new ArrayList<>();
                        for (int i = 0; i < mList.size(); i++) {
                                paths.add(mList.get(i).getPath());
                        }
                        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                        intent.putStringArrayListExtra("paths", paths);
                        intent.putExtra("pos", position);
                        mActivity.startActivity(intent);
                    } else
                        app.setSelected(!app.isSelected());
                });
                holder.cardView.setOnLongClickListener(v -> {
                    app.setSelected(true);
                    DeleteSheet bt = new DeleteSheet();
                    List<String> paths = new ArrayList<>();
                    paths.add(app.getPath());
                    bt.setPaths(paths);
                    bt.show(mActivity.getSupportFragmentManager(), "no");
                    return false;
                });
            }
            if (app.isVideo) {
                holder.videoMarker.setVisibility(View.VISIBLE);
                Glide.with(holder.img)
                        .load(app.getThumbnail())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.img);
                holder.cardView.setOnClickListener(v -> {
                    if (!app.isSelected()) {
                        Intent intent = new Intent(mActivity, VideoActivity.class);
                        intent.putExtra("video_path", app.getPath());
                        mActivity.startActivity(intent);
                    } else
                        app.setSelected(!app.isSelected());
                });
                holder.cardView.setOnLongClickListener(v -> {
                    app.setSelected(true);
                    DeleteSheet bt = new DeleteSheet();
                    List<String> paths = new ArrayList<>();
                    paths.add(app.getPath());
                    List<String> thumbs = new ArrayList<>();
                    thumbs.add(app.getThumbnail());
                    bt.setThumbs(thumbs);
                    bt.setPaths(paths);
                    bt.show(mActivity.getSupportFragmentManager(), "no");
                    return false;
                });
            }
        } else {
            if (!app.isVideo) {
                holder.videoMarker.setVisibility(View.GONE);
                Glide.with(holder.img)
                        .load(app.getPath())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.img);
                holder.cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(fActivity, DetailsActivity.class);
                    ArrayList<String> paths = new ArrayList<>();
                    for (int i = 0; i < mList.size(); i++) {
                        if (!mList.get(i).isVideo)
                            paths.add(mList.get(i).getPath());
                    }
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putStringArrayListExtra("paths", paths);
                    intent.putExtra("pos", position);
                    fActivity.startActivity(intent);
                });
                holder.cardView.setOnLongClickListener(v -> {
                    DeleteSheet bt = new DeleteSheet();
                    List<String> paths = new ArrayList<>();
                    paths.add(app.getPath());
                    bt.setPaths(paths);
                    bt.show(fActivity.getSupportFragmentManager(), "no");
                    return false;
                });
            }
            if (app.isVideo) {
                holder.videoMarker.setVisibility(View.VISIBLE);
                Glide.with(holder.img)
                        .load(app.getThumbnail())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.img);
                holder.cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(fActivity, VideoActivity.class);
                    intent.putExtra("video_path", app.getPath());
                    fActivity.startActivity(intent);
                });
                holder.cardView.setOnLongClickListener(v -> {
                    DeleteSheet bt = new DeleteSheet();
                    List<String> paths = new ArrayList<>();
                    paths.add(app.getPath());
                    List<String> thumbs = new ArrayList<>();
                    thumbs.add(app.getThumbnail());
                    bt.setThumbs(thumbs);
                    bt.setPaths(paths);
                    bt.show(fActivity.getSupportFragmentManager(), "no");
                    return false;
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView img, videoMarker;

        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.img_card);
            img = view.findViewById(R.id.img);
            videoMarker = view.findViewById(R.id.videoMarker);
        }
    }

}