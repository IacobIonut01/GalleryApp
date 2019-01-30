package com.dot.gallery.views;

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
import com.dot.gallery.fragments.RoundedSheetFragment;
import com.dot.gallery.utils.MediaFileFunctions;
import com.dot.gallery.utils.NotificationBuilder;
import com.google.android.material.button.MaterialButton;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class DeleteSheet extends RoundedSheetFragment implements View.OnClickListener {

    private List<String> paths = new ArrayList<>();
    private List<String> thumbs = new ArrayList<>();
    private MaterialButton delete;
    private TextView counter;
    private ImageView img;
    private View.OnClickListener cls = null;
    private boolean isAlbum = false;
    private String album_path, album_img, album_name;
    private Fragment frg;

    @Override
    public void dismiss() {
        super.dismiss();
        if (frg != null)
            frg.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view =  inflater.inflate(R.layout.sheet_delete, container, false);
        delete = view.findViewById(R.id.delete);
        counter = view.findViewById(R.id.del_counter);
        img = view.findViewById(R.id.first_img);

        if (paths.size() > 1 && paths.size() <= 9)
            counter.setText(paths.size());
        else if (paths.size() > 9)
            counter.setText("9+");
        else
            counter.setText("");
        if (!isAlbum) {
            if (thumbs.size() == 0) {
                Glide.with(view)
                        .load(paths.get(0))
                        .into(img);
            } else {
                Glide.with(view)
                        .load(thumbs.get(0))
                        .into(img);
            }
        } else {
            Glide.with(view)
                    .load(album_img)
                    .into(img);
        }
        if (cls != null)
            delete.setOnClickListener(cls);
        else
            delete.setOnClickListener(this);
        return view;
    }

    public void setFragment(Fragment frg) {
        this.frg = frg;
    }


    public void setThumbs(List<String> thumbs) {
        this.thumbs = thumbs;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    private void callBroadCast() {
        MediaScannerConnection.scanFile(getActivity(), new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {
        });
    }

    public void isAlbum(String album_path, String album_img, String album_name) {
        this.isAlbum = true;
        this.album_path = album_path;
        this.album_img = album_img;
        this.album_name = album_name;
    }

    @Override
    public void onClick(View v) {
        if (!isAlbum) {
            if (paths.size() == 1) {
                File image = new File(paths.get(0));
                if (image.exists()) {
                    if (MediaFileFunctions.deleteViaContentProvider(getContext(), paths.get(0))) {
                        callBroadCast();
                    }
                }
                dismiss();
            } else {
                for (int i = 0; i < paths.size(); i++) {
                    File image = new File(paths.get(i));
                    if (image.exists()) {
                        if (MediaFileFunctions.deleteViaContentProvider(getContext(), paths.get(i))) {
                            callBroadCast();
                        }
                    }
                }
                dismiss();
            }
        } else {
            NotificationBuilder notificationBuilder = new NotificationBuilder();
            notificationBuilder.initDeleteAlbumNotification(getActivity(), album_name, album_path);
            dismiss();
        }
    }

    void setOnDeleteListener(View.OnClickListener cl) {
        this.cls = cl;
    }

}
