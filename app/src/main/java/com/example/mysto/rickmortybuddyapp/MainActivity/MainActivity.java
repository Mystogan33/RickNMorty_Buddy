package com.example.mysto.rickmortybuddyapp.MainActivity;

import androidx.annotation.NonNull;

import com.example.mysto.rickmortybuddyapp.notifications.NotificationHelperWelcomeBack;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
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
import timber.log.Timber;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.Fragment_Episodes;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.Fragment_Lieux;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.Fragment_Personnages;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.MainActivity.adapter.ViewPagerAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

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
    /*@BindView(R.id.dots_indicator)
    DotsIndicator dotsIndicator;*/

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
        //dotsIndicator.setViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

        this.sendNotifications();

         /*if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            mViewPager.setPageTransformer(true, new DepthPageTransformer());
            //mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        }*/

    }

    @OnClick(R.id.menu_button)
    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.more_button)
    public void openOptions() { initSequence(); }

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
            initSequence();
        } else {
            if(!json.equals(date)) {
                mNotifier = new NotificationHelperWelcomeBack(this);
                mNotifier.createNotification("Welcome Back", "Thanks to visit Rick & Morty ! Hope you get swifty. Stay connected ! ");

                sharedPreferences.edit()
                        .putString("last_launch_time", date)
                        .apply();
            }
        }
    }

    public void initSequence() {
        TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(home_button, "Menu", "Vous pouvez ouvrir le menu ici")
                                .outerCircleColor(R.color.tabindicatorcolor)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.colorAccent)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.colorAccent)      // Specify the color of the title text
                                .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.colorAccent)  // Specify the color of the description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.followersBg)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(60)
                                .id(1),
                        TapTarget.forView(more_button, "Plus d'options", "Plus d'options ici ! :)")
                                .outerCircleColor(R.color.tabindicatorcolor)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.colorAccent)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.colorAccent)      // Specify the color of the title text
                                .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.colorAccent)  // Specify the color of the description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.followersBg)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(60)
                                .id(2),
                        TapTarget.forView(refresh_button, "Rafraichir toutes les catégories", "(A noter: Toutes les catégories sont automatiquement rafraichies chaque jour et peuvent être rafraichis avec ce bouton mais vous pouvez les rafraichir manuellement et séparément en swipant vers le bas en haut de chacune des catégories)")
                                .outerCircleColor(R.color.tabindicatorcolor)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.colorAccent)      // Specify the color of the title text
                                .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.colorAccent)  // Specify the color of the description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(60)
                                .id(3))
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        Toast.makeText(getApplicationContext(), "You know everything now Morty !", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean bool) {
                        switch (lastTarget.id()) {
                            case 1:
                                openDrawer();
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() { mDrawerLayout.closeDrawers(); }
                                }, 500);
                                break;
                            case 3:
                                refreshAllFragments();
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        Toast.makeText(getApplicationContext(), "Why you left me Morty ! You can't left me !", Toast.LENGTH_SHORT).show();
                    }
                });
        sequence.start();
    }

}
