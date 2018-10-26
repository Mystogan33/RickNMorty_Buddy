package com.example.mysto.rickmortybuddyapp.MainActivity;

import androidx.annotation.NonNull;

import com.example.mysto.rickmortybuddyapp.notifications.NotificationHelperWelcomeBack;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.Fragment_Episodes;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.Fragment_Lieux;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.Fragment_Personnages;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.MainActivity.adapter.ViewPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tablayout_id)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager_id)
    ViewPager mViewPager;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.menu_button)
    ImageButton home_button;
    @BindView(R.id.more_button)
    ImageButton more_button;
    @BindView(R.id.refresh_button)
    FloatingActionButton refresh_button;

    NotificationHelperWelcomeBack mNotifier;
    ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // set item as selected to persist highlight
                        item.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.AddFragment(new Fragment_Personnages(), getResources().getString(R.string.tab_title_characters));
        mViewPagerAdapter.AddFragment(new Fragment_Episodes(), getResources().getString(R.string.tab_title_episodes));
        mViewPagerAdapter.AddFragment(new Fragment_Lieux(), getResources().getString(R.string.tab_title_locations));

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

          /*if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            mViewPager.setPageTransformer(true, new DepthPageTransformer());
            //mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        }*/

        mTabLayout.setupWithViewPager(mViewPager);

        /*home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPagerAdapter.refreshAllFragments();
            }
        });*/

        this.sendNotifications();

    }

    @OnClick(R.id.menu_button)
    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.refresh_button)
    public void refreshAllFragments() {
        mViewPagerAdapter.refreshAllFragments();
    }

    public void sendNotifications() {
        Calendar cal = Calendar.getInstance();
        String date = new SimpleDateFormat("yyyy-MM-dd",Locale.FRANCE).format(cal.getTime());
        SharedPreferences sharedPreferences = this.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("last_launch_time", null);

        if(json == null) {
            sharedPreferences.edit()
                    .putString("last_launch_time", date)
                    .apply();
        } else {
            if(json != date) {
                mNotifier = new NotificationHelperWelcomeBack(this);
                mNotifier.createNotification("Welcome Back", "Thanks to visit Rick & Morty ! Hope you get swifty. Stay connected ! ");

                sharedPreferences.edit()
                        .putString("last_launch_time", date)
                        .apply();
            }
        }
    }

}
