package com.example.mysto.rickmortybuddyapp.MainActivity;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.Fragment_Episodes;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.Fragment_Lieux;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.Fragment_Personnages;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.MainActivity.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ViewPagerAdapter adapter;
    private FloatingActionButton home_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tablayout_id);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.viewpager_id);
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

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new Fragment_Personnages(), "Characters");
        adapter.AddFragment(new Fragment_Episodes(), "Episodes");
        adapter.AddFragment(new Fragment_Lieux(), "Locations");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
