package com.dot.gallery.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.dot.gallery.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;

public class RoundedSheetFragment extends BottomSheetDialogFragment {

    @Override
    public int getTheme() {
        SharedPreferences prefs = getActivity().getSharedPreferences("app_theme_style", MODE_PRIVATE);
        switch (prefs.getInt("style", 0)) {
            case 0:
                return R.style.BottomSheetDialogTheme;
            case 1:
                return R.style.BottomSheetDialogThemeD;
            default:
                return R.style.BottomSheetDialogTheme;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }

}
