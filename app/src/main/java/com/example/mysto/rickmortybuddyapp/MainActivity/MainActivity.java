package com.example.mysto.rickmortybuddyapp.MainActivity;

import android.os.Build;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.Fragment_Episodes;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.Fragment_Lieux;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.Fragment_Personnages;
import com.example.mysto.rickmortybuddyapp.MainActivity.adapter.DepthPageTransformer;
import com.example.mysto.rickmortybuddyapp.MainActivity.adapter.ZoomOutPageTransformer;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.MainActivity.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ViewPagerAdapter mViewPagerAdapter;
    //private FloatingActionButton home_button;
    private ImageButton home_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mTabLayout = findViewById(R.id.tablayout_id);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mViewPager = findViewById(R.id.viewpager_id);
        home_button = findViewById(R.id.menu_button);

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

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
        mViewPagerAdapter.AddFragment(new Fragment_Personnages(), "Characters");
        mViewPagerAdapter.AddFragment(new Fragment_Episodes(), "Episodes");
        mViewPagerAdapter.AddFragment(new Fragment_Lieux(), "Locations");

        mViewPager.setAdapter(mViewPagerAdapter);

        /*if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            mViewPager.setPageTransformer(true, new DepthPageTransformer());
            //mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        }*/

        mTabLayout.setupWithViewPager(mViewPager);

    }
}
