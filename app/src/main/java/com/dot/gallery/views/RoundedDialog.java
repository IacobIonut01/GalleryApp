package com.dot.gallery.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.dot.gallery.R;

import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.res.ResourcesCompat;

public class RoundedDialog extends AppCompatDialog {

    public RoundedDialog(Context context) {
        super(context);
        init();
    }

    public RoundedDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(wrap(view));
    }

    @SuppressLint("ClickableViewAccessibility")
    private View wrap(View view) {
        Context context = getContext();
        Resources resources = context.getResources();
        int verticalMargin = resources.getDimensionPixelSize(R.dimen.dialog_vertical_margin);
        int horizontalMargin = resources.getDimensionPixelSize(R.dimen.dialog_horizontal_margin);

        FrameLayout frameLayout = new FrameLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin);
        params.gravity = Gravity.CENTER;
        frameLayout.addView(view, params);
        frameLayout.setBackground(new ColorDrawable(ResourcesCompat.getColor(resources, R.color.scrim, context.getTheme())));
        return frameLayout;
    }
}
