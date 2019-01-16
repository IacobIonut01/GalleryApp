package com.dot.gallery.fragments;

import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dot.gallery.R;
import com.dot.gallery.utils.MediaFileFunctions;
import com.dot.gallery.utils.RoundedSheetFragment;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeleteSheet extends RoundedSheetFragment {

    List<String> paths = new ArrayList<>();
    MaterialButton delete;
    TextView counter;
    ImageView img;
    RecyclerView recyclerView;

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view =  inflater.inflate(R.layout.sheet_delete, container, false);
        delete = view.findViewById(R.id.delete);
        counter = view.findViewById(R.id.del_counter);
        img = view.findViewById(R.id.first_img);
        if (paths.size() > 0)
            initSheet(view);
        else
            Log.e("@Delete Sheet", "Paths List not found and/or not valid. Please set paths list before attaching the fragment");
        return view;
    }

    private void initSheet(View view) {
        if (paths.size() > 1 && paths.size() <= 9)
            counter.setText(paths.size());
        else if (paths.size() > 9)
            counter.setText("9+");
        else
            counter.setText("");
        Glide.with(view)
                .load(new File(paths.get(0)))
                .into(img);
        delete.setOnClickListener(v -> {
            if (paths.size() == 1) {
                File image = new File(paths.get(0));
                if (image.exists()) {
                    if (MediaFileFunctions.deleteViaContentProvider(getContext(), paths.get(0))) {
                        callBroadCast();
                    }
                }
                dismiss();
            } else {
                for (int i = 0; i<paths.size(); i++) {
                    File image = new File(paths.get(i));
                    if (image.exists()) {
                        if (MediaFileFunctions.deleteViaContentProvider(getContext(), paths.get(i))) {
                            callBroadCast();
                        }
                    }
                }
                dismiss();
            }
        });
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public void setRootRecycler(RecyclerView recycler) {
        recyclerView = recycler;
    }

    public void callBroadCast() {
        MediaScannerConnection.scanFile(getActivity(), new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {
        });
    }
}
