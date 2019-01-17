package com.dot.gallery.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.dot.gallery.R;
import com.dot.gallery.activities.DetailsActivity;
import com.dot.gallery.fragments.FavDeleteSheet;
import com.dot.gallery.fragments.PickerSheet;
import com.dot.gallery.model.FavouriteCard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private List<FavouriteCard> mList;
    private FragmentActivity fActivity;


    public FavouriteAdapter(FragmentActivity activity, List<FavouriteCard> mList) {
        this.mList = mList;
        this.fActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        FavouriteCard app = mList.get(position);
        if (!app.isFirst()) {
            holder.adds.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.VISIBLE);
            Glide.with(holder.img)
                    .load(new File(app.getPath()))
                    .into(holder.img);
            holder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(fActivity, DetailsActivity.class);
                List<String> paths = new ArrayList<>();
                for (int i = 1; i < mList.size(); i++) {
                    paths.add(mList.get(i).getPath());
                }
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putStringArrayListExtra("paths", (ArrayList<String>) paths);
                intent.putExtra("pos", position-1);
                fActivity.startActivity(intent);
            });
            holder.cardView.setOnLongClickListener(v -> {
                FavDeleteSheet sheet = new FavDeleteSheet();
                Log.d("Debug", String.valueOf(position));
                sheet.setPosition(position);
                sheet.setmActivity(fActivity);
                sheet.show(fActivity.getSupportFragmentManager(), "favdel");
                return false;
            });
        } else {
            holder.adds.setVisibility(View.VISIBLE);
            holder.cardView.setVisibility(View.GONE);
            holder.add_items.setOnClickListener(v -> {
                PickerSheet sheet = new PickerSheet();
                sheet.show(fActivity.getSupportFragmentManager(),"picker");
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView img;
        RelativeLayout adds;
        ImageButton add_items;

        ViewHolder(View view) {
            super(view);
            add_items = view.findViewById(R.id.add_favs);
            adds = view.findViewById(R.id.add_favourite);
            cardView = view.findViewById(R.id.img_card);
            img = view.findViewById(R.id.img);
        }
    }

}