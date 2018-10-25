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

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ViewPagerAdapter mViewPagerAdapter;
    private ImageButton home_button;
    private ImageButton more_button;
    private FloatingActionButton refresh_button;

    NotificationHelperWelcomeBack mNotifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.findViews();

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

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPagerAdapter.refreshAllFragments();
            }
        });

        this.sendNotifications();

    }

    public void sendNotifications() {
        Calendar cal = Calendar.getInstance();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        SharedPreferences sharedPreferences = this.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("last_launch_time", null);

        if(json != null) {
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

    public void findViews() {

        mTabLayout = findViewById(R.id.tablayout_id);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mViewPager = findViewById(R.id.viewpager_id);
        home_button = findViewById(R.id.menu_button);
        more_button = findViewById(R.id.more_button);
        refresh_button = findViewById(R.id.refresh_button);

    }
}
