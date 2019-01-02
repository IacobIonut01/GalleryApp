package com.dot.gallery.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dot.gallery.R;
import com.dot.gallery.activities.DetailsActivity;
import com.dot.gallery.activities.FavouriteActivity;
import com.dot.gallery.fragments.HomeFragment;
import com.dot.gallery.fragments.PickerSheet;
import com.dot.gallery.model.FavouriteCard;
import com.dot.gallery.model.TodayCard;
import com.dot.gallery.utils.MediaFileFunctions;
import com.dot.gallery.views.RoundedDialog;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private List<FavouriteCard> mList;
    private AppCompatActivity mActivity;
    private FragmentActivity fActivity;

    public FavouriteAdapter(AppCompatActivity activity, List<FavouriteCard> mList) {
        this.mList = mList;
        this.mActivity = activity;
    }

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
                Intent intent;
                if (mActivity != null)
                    intent = new Intent(mActivity, DetailsActivity.class);
                else
                    intent = new Intent(fActivity, DetailsActivity.class);
                List<String> paths = new ArrayList<>();
                for (int i = 0; i < mList.size(); i++) {
                    paths.add(mList.get(i).getPath());
                }
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putStringArrayListExtra("paths", (ArrayList<String>) paths);
                intent.putExtra("pos", position);
                if (mActivity != null)
                    mActivity.startActivity(intent);
                else
                    fActivity.startActivity(intent);
            });
            holder.cardView.setOnLongClickListener(v -> {
                RoundedDialog dialog;
                View view;
                if (mActivity != null) {
                    dialog = new RoundedDialog(mActivity, R.style.Theme_RoundedDialog);
                    view = View.inflate(mActivity, R.layout.delete_dialog, null);
                } else {
                    dialog = new RoundedDialog(fActivity, R.style.Theme_RoundedDialog);
                    view = View.inflate(fActivity, R.layout.delete_dialog, null);
                }
                dialog.setContentView(view);
                TextView title = view.findViewById(R.id.title);
                TextView text = view.findViewById(R.id.text);
                Button positive = view.findViewById(R.id.dialog_postive);
                Button negative = view.findViewById(R.id.dialog_negative);
                text.setText("Do you want to remove image from favourites?");
                title.setText("Confirm Remove");
                positive.setVisibility(View.VISIBLE);
                if (mActivity != null) {
                    positive.setText(mActivity.getString(R.string.sub_yes));
                } else
                    positive.setText(fActivity.getString(R.string.sub_yes));
                positive.setOnClickListener(v2 -> {
                    mList.remove(0);
                    mList.remove(app);
                    updateList();
                    dialog.cancel();
                    if (mActivity != null) {
                        FavouriteActivity activity = (FavouriteActivity) mActivity;
                        activity.loadImages();
                    } else {
                        HomeFragment frg = (HomeFragment) fActivity.getSupportFragmentManager().findFragmentByTag("main");
                        frg.loadImages();
                    }
                });
                negative.setText(android.R.string.cancel);
                negative.setOnClickListener(v3 -> dialog.cancel());
                dialog.show();
                return false;
            });
        } else {
            holder.adds.setVisibility(View.VISIBLE);
            holder.cardView.setVisibility(View.GONE);
            holder.add_items.setOnClickListener(v -> {
                PickerSheet sheet = new PickerSheet();
                if (fActivity != null)
                    sheet.show(fActivity.getSupportFragmentManager(),"picker");
                else
                    sheet.show(mActivity.getSupportFragmentManager(), "picker");
            });
        }
    }

    private void updateList() {
        SharedPreferences shared;
        if (mActivity != null)
            shared = mActivity.getSharedPreferences("favourite_images", MODE_PRIVATE);
        else
            shared = fActivity.getSharedPreferences("favourite_images", MODE_PRIVATE);
        String selectedOutput = new Gson().toJson(mList);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("favourite_images", selectedOutput);
        editor.apply();
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