package com.dot.gallery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dot.gallery.R;
import com.dot.gallery.model.PickerCard;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PickerAdapter extends RecyclerView.Adapter<PickerAdapter.ViewHolder> {

    private List<PickerCard> mList;

    public PickerAdapter(List<PickerCard> mList) {
        this.mList = mList;
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
                .load(app.getPath())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .into(holder.img);
        holder.cardView.setOnClickListener(app.getLst());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView img;

        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.img_card);
            img = view.findViewById(R.id.img);
        }
    }


}