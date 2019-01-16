package com.dot.gallery.adapters;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dot.gallery.R;
import com.dot.gallery.activities.DetailsActivity;
import com.dot.gallery.fragments.DeleteSheet;
import com.dot.gallery.model.TodayCard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

    private List<TodayCard> mList;
    private AppCompatActivity mActivity;
    private FragmentActivity mFActivity;

    public TodayAdapter(AppCompatActivity activity, List<TodayCard> mList) {
        this.mList = mList;
        this.mActivity = activity;
    }

    public TodayAdapter(FragmentActivity activity, List<TodayCard> mList) {
        this.mList = mList;
        this.mFActivity = activity;
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
        TodayCard app = mList.get(position);
        Glide.with(holder.img)
                .load(new File(app.getPath()))
                .into(holder.img);
        holder.name.setText(app.getName());
        if (mActivity != null) {
            holder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(mActivity, DetailsActivity.class);
                List<String> paths = new ArrayList<>();
                for (int i = 0; i < mList.size(); i++) {
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
                paths.add(app.getPath());
                bt.setPaths(paths);
                bt.show(mActivity.getSupportFragmentManager(), "no");
                return false;
            });
        } else if (mFActivity != null) {
            holder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(mFActivity, DetailsActivity.class);
                List<String> paths = new ArrayList<>();
                for (int i = 0; i < mList.size(); i++) {
                    paths.add(mList.get(i).getPath());
                }
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putStringArrayListExtra("paths", (ArrayList<String>) paths);
                intent.putExtra("pos", position);
                mFActivity.startActivity(intent);
            });
            holder.cardView.setOnLongClickListener(v -> {
                DeleteSheet bt = new DeleteSheet();
                List<String> paths = new ArrayList<>();
                paths.add(app.getPath());
                bt.setPaths(paths);
                bt.show(mFActivity.getSupportFragmentManager(), "no");
                return false;
            });
        }
    }

    public void callBroadCast() {
        MediaScannerConnection.scanFile(mActivity, new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView img;
        TextView name;

        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.img_card);
            img = view.findViewById(R.id.img);
            name = view.findViewById(R.id.img_title);
        }
    }


}