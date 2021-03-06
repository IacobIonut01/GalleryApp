package com.dot.gallery.views;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dot.gallery.R;
import com.dot.gallery.utils.MediaFileFunctions;
import com.dot.gallery.utils.ZoomOutTransformation;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;

public class DetailsSheetView extends LinearLayout {

    AppCompatActivity activity;
    View view;
    CoordinatorLayout root;
    LinearLayout sheet;
    BottomSheetBehavior behavior;
    CardView frg_card;
    CustomViewPager frg_view;
    Context context;
    TouchImageAdapter adapter = new TouchImageAdapter();
    // Options
    ImageButton share, delete;
    TextView name;
    List<String> paths = new ArrayList<>();
    int static_pos;


    public DetailsSheetView(Context context) {
        super(context);
        init(context);
    }

    public DetailsSheetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        view = inflate(context, R.layout.details_sliding_up_layout, this);
        frg_view = view.findViewById(R.id.mViewPager);
        root = view.findViewById(R.id.sheet_root);
        sheet = view.findViewById(R.id.bottom_sheet);
        frg_card = view.findViewById(R.id.fragment_card);
        share = view.findViewById(R.id.share_btn);
        delete = view.findViewById(R.id.delete_file);
        name = view.findViewById(R.id.img_p_name);
        behavior = BottomSheetBehavior.from(sheet);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior.setPeekHeight(dp(80));
        frg_view.setScroll(true);
        frg_view.setPageTransformer(false, new ZoomOutTransformation());
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float offset) {
                frg_card.setTranslationY(-(sheet.getHeight() * offset));
                frg_card.setRadius(dp(12) * offset);
                frg_card.setElevation(2 * offset);
            }
        });
        share.setOnClickListener(v -> shareImage(adapter.getPathAt(frg_view.getCurrentItem())));
        delete.setOnClickListener(v -> {
            DeleteSheet bt = new DeleteSheet();
            List<String> paths = new ArrayList<>();
            paths.add(adapter.getPathAt(frg_view.getCurrentItem()));
            bt.setPaths(paths);
            bt.show(activity.getSupportFragmentManager(), "no");
            bt.setOnDeleteListener(v2 -> {
                File file = new File(adapter.getPathAt(frg_view.getCurrentItem()));
                if (file.exists()) {
                    if (MediaFileFunctions.deleteViaContentProvider(activity, adapter.getPathAt(frg_view.getCurrentItem()))) {
                        MediaScannerConnection.scanFile(activity, new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {
                        });
                        adapter.deletPathAt(frg_view.getCurrentItem());
                        frg_view.setAdapter(adapter);
                        frg_view.invalidate();
                        frg_view.setCurrentItem(static_pos);
                        if (adapter.getCount() < 1) {
                            activity.finish();
                        }
                        bt.dismiss();
                    }
                }
            });
        });
    }

    public void shareImage(String image) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, "com.dot.gallery.fileprovider", new File(image)));
        context.startActivity(Intent.createChooser(share, "Share image"));
    }

    public void setImage(List<String> path) {
        this.paths = path;
        frg_view.setAdapter(adapter);
    }

    public void setPosition(int pos) {
        static_pos = pos;
        frg_view.setCurrentItem(pos);
    }

    private int dp(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void setContainerActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    class TouchImageAdapter extends PagerAdapter {

        String getPathAt(int pos) {
            return paths.get(pos);
        }

        void deletPathAt(int pos) {
            paths.remove(pos);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @NonNull
        @Override
        public View instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView img = new ImageView(container.getContext());
            Glide.with(container.getContext())
                    .load(new File(paths.get(position)))
                    .into(img);
            PagerMatrixTouchHandler matrixTouchHandler = new PagerMatrixTouchHandler(img.getContext());
            matrixTouchHandler.setViewPager(frg_view);
            img.setOnTouchListener(matrixTouchHandler);
            container.addView(img, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            return img;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

}
