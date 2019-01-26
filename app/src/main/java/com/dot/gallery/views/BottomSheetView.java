package com.dot.gallery.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dot.gallery.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class BottomSheetView extends LinearLayout {

    View view;
    CoordinatorLayout root;
    LinearLayout sheet;
    BottomSheetBehavior behavior;
    CardView frg_card;
    FrameLayout frg_view;


    public BottomSheetView(Context context) {
        super(context);
        init(context);
    }

    public BottomSheetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        view = inflate(context, R.layout.sliding_up_layout, this);
        root = view.findViewById(R.id.sheet_root);
        sheet = view.findViewById(R.id.bottom_sheet);
        frg_card = view.findViewById(R.id.fragment_card);
        frg_view = view.findViewById(R.id.fragment_container);
        behavior = BottomSheetBehavior.from(sheet);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior.setPeekHeight(dp(30));
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float offset) {
                CoordinatorLayout.LayoutParams card_params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                frg_card.setTranslationY(-(sheet.getHeight() * offset));
                frg_card.setLayoutParams(card_params);
                frg_card.setRadius(dp(12) * offset);
                frg_card.setElevation(2 * offset);
            }
        });
    }

    public void setFragment(FragmentManager fm, Fragment fragment) {
        fm.beginTransaction().replace(R.id.fragment_container, fragment).commitNow();
    }

    private int dp(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
