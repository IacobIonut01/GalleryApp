package com.dot.gallery.adapters;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dot.gallery.R;
import com.dot.gallery.activities.DetailsActivity;
import com.dot.gallery.activities.VideoActivity;
import com.dot.gallery.fragments.DeleteSheet;
import com.dot.gallery.model.MediaCard;
import com.dot.gallery.model.TodayCard;
import com.dot.gallery.model.VideoCard;
import com.masterwok.simplevlcplayer.activities.MediaPlayerActivity;

import java.io.File;
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
                .inflate(R.layout.today_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        MediaCard app = mList.get(position);
        if (mActivity != null) {
            if (app instanceof TodayCard) {
                TodayCard appt = (TodayCard) app;
                holder.videoMarker.setVisibility(View.GONE);
                Glide.with(holder.img)
                        .load(appt.getPath())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.img);
                holder.cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(mActivity, DetailsActivity.class);
                    List<String> paths = new ArrayList<>();
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i) instanceof  TodayCard)
                            paths.add(mList.get(i).getPath());
                    }
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putStringArrayListExtra("paths", (ArrayList<String>) paths);
                    intent.putExtra("pos", position);
                    mActivity.startActivity(intent);
                });
                holder.cardView.setOnLongClickListener(v -> {
                    DeleteSheet bt = new DeleteSheet();
                    List<String> paths = new ArrayList<>();
                    paths.add(appt.getPath());
                    bt.setPaths(paths);
                    bt.show(mActivity.getSupportFragmentManager(), "no");
                    return false;
                });
            }
            if (app instanceof VideoCard) {
                VideoCard appv = (VideoCard) app;
                holder.videoMarker.setVisibility(View.VISIBLE);
                Glide.with(holder.img)
                        .load(appv.getThumbnail())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.img);
                holder.cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(mActivity, VideoActivity.class);
                    intent.putExtra("video_path", appv.getPath());
                    mActivity.startActivity(intent);
                });
                holder.cardView.setOnLongClickListener(v -> {
                    DeleteSheet bt = new DeleteSheet();
                    List<String> paths = new ArrayList<>();
                    paths.add(appv.getPath());
                    List<String> thumbs = new ArrayList<>();
                    thumbs.add(appv.getThumbnail());
                    bt.setThumbs(thumbs);
                    bt.setPaths(paths);
                    bt.show(mActivity.getSupportFragmentManager(), "no");
                    return false;
                });
            }
        } else {
            if (app instanceof TodayCard) {
                TodayCard appt = (TodayCard) app;
                holder.videoMarker.setVisibility(View.GONE);
                Glide.with(holder.img)
                        .load(appt.getPath())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.img);
                holder.cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(fActivity, DetailsActivity.class);
                    List<String> paths = new ArrayList<>();
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i) instanceof  TodayCard)
                            paths.add(mList.get(i).getPath());
                    }
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putStringArrayListExtra("paths", (ArrayList<String>) paths);
                    intent.putExtra("pos", position);
                    fActivity.startActivity(intent);
                });
                holder.cardView.setOnLongClickListener(v -> {
                    DeleteSheet bt = new DeleteSheet();
                    List<String> paths = new ArrayList<>();
                    paths.add(appt.getPath());
                    bt.setPaths(paths);
                    bt.show(fActivity.getSupportFragmentManager(), "no");
                    return false;
                });
            }
            if (app instanceof VideoCard) {
                VideoCard appv = (VideoCard) app;
                holder.videoMarker.setVisibility(View.VISIBLE);
                Glide.with(holder.img)
                        .load(appv.getThumbnail())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.img);
                holder.cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(fActivity, VideoActivity.class);
                    intent.putExtra("video_path", appv.getPath());
                    fActivity.startActivity(intent);
                });
                holder.cardView.setOnLongClickListener(v -> {
                    DeleteSheet bt = new DeleteSheet();
                    List<String> paths = new ArrayList<>();
                    paths.add(appv.getPath());
                    List<String> thumbs = new ArrayList<>();
                    thumbs.add(appv.getThumbnail());
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