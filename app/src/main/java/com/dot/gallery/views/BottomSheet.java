package com.dot.gallery.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dot.gallery.R;
import com.dot.gallery.activities.TrashActivity;
import com.dot.gallery.fragments.FavouriteFragment;
import com.dot.gallery.fragments.RoundedSheetFragment;

import androidx.annotation.NonNull;

public class BottomSheet extends RoundedSheetFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view =  inflater.inflate(R.layout.sheet_main, container, false);
        RelativeLayout fv = view.findViewById(R.id.open_fv);
        fv.setOnClickListener(v -> startActivity(new Intent(getActivity(), FavouriteFragment.class)));
        RelativeLayout ts = view.findViewById(R.id.open_ts);
        ts.setOnClickListener(v -> startActivity(new Intent(getActivity(), TrashActivity.class)));
        return view;
    }
}
