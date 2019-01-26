package com.dot.gallery.activities;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.dot.gallery.R;
import com.dot.gallery.fragments.FavouriteFragment;
import com.dot.gallery.fragments.HomeFragment;
import com.dot.gallery.fragments.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!hasPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
        SharedPreferences prefs = getSharedPreferences("app_theme_style", MODE_PRIVATE);
        switch (prefs.getInt("style", 0)) {
            case 0:
                setTheme(R.style.AppTheme);
                break;
            case 1:
                setTheme(R.style.DarkTheme);
                break;
        }
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.main_title);
        AHBottomNavigationViewPager viewPager = findViewById(R.id.fragment_pager);
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new HomeFragment();
                    case 1:
                        return new FavouriteFragment();
                    case 2:
                        return new SearchFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        AHBottomNavigation bottomNavigation = findViewById(R.id.bottomv);
        List<AHBottomNavigationItem> items = new ArrayList<>();

        AHBottomNavigationItem iAlbums = new AHBottomNavigationItem("Albums", R.drawable.ic_album);
        AHBottomNavigationItem iFavourites = new AHBottomNavigationItem("Favourites", R.drawable.ic_star);
        AHBottomNavigationItem iSearch = new AHBottomNavigationItem("Search", R.drawable.ic_search);
        AHBottomNavigationItem iCamera = new AHBottomNavigationItem("Camera", R.drawable.ic_photo_camera);
        AHBottomNavigationItem iSettings = new AHBottomNavigationItem("Settings", R.drawable.ic_settings);
        items.add(iAlbums);
        items.add(iFavourites);
        items.add(iSearch);
        items.add(iCamera);
        items.add(iSettings);
        bottomNavigation.addItems(items);
        bottomNavigation.setDefaultBackgroundColor(getColorAttr(this, R.attr.colorPrimary));
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setAccentColor(getColor(R.color.colorAccent));
        bottomNavigation.setInactiveColor(getColorAttr(this, android.R.attr.textColorSecondary));
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:
                    viewPager.setCurrentItem(0);
                    setTitle("Gallery");
                    break;
                case 1:
                    viewPager.setCurrentItem(1);
                    setTitle("Favourites");
                    break;
                case 2:
                    viewPager.setCurrentItem(2);
                    setTitle("Search");
                    break;
                case 3:
                    bottomNavigation.setCurrentItem(0);
                    viewPager.setCurrentItem(0);
                    startActivity(new Intent(this, CameraActivity.class));
                    break;
                case 4:
                    bottomNavigation.setCurrentItem(0);
                    viewPager.setCurrentItem(0);
                    startActivity(new Intent(this, SettingsActivity.class));
                    break;
            }
            return true;
        });
    }

    public void setTitle(CharSequence text) {
        title.setText(text);
    }

    public void setTitle(int id) {
        title.setText(id);
    }

    @ColorInt
    public static int getColorAttr(Context context, int attr) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{attr});
        @ColorInt int colorAccent = ta.getColor(0, 0);
        ta.recycle();
        return colorAccent;
    }

    private boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
