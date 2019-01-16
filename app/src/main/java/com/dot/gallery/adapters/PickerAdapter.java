package com.dot.gallery.adapters;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dot.gallery.R;
import com.dot.gallery.activities.DetailsActivity;
import com.dot.gallery.model.FavouriteCard;
import com.dot.gallery.model.PickerCard;
import com.dot.gallery.model.TodayCard;
import com.dot.gallery.utils.MediaFileFunctions;
import com.dot.gallery.utils.TinyDB;
import com.dot.gallery.views.RoundedDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PickerAdapter extends RecyclerView.Adapter<PickerAdapter.ViewHolder> {

    private List<PickerCard> mList;
    private Activity mActivity;

    public PickerAdapter(Activity activity, List<PickerCard> mList) {
        this.mList = mList;
        this.mActivity = activity;
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
        PickerCard app = mList.get(position);
        Glide.with(holder.img)
                .load(new File(app.getPath()))
                .into(holder.img);
        holder.name.setText(app.getName());
        holder.cardView.setOnClickListener(app.getLst());
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