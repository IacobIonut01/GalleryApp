package com.dot.gallery.utils;

import android.app.Dialog;
import android.os.Bundle;

import com.dot.gallery.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;

public class RoundedSheetFragment extends BottomSheetDialogFragment {

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }


}
