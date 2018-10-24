package com.example.mysto.rickmortybuddyapp.MainActivity.adapter;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.Fragment_Personnages;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.Fragment_Episodes;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.Fragment_Lieux;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> FragmentListTitles = new ArrayList<>();


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentListTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return FragmentListTitles.get(position);
    }

    public void AddFragment(Fragment fragment, String Title) {
        fragmentList.add(fragment);
        FragmentListTitles.add(Title);
    }

    public void refreshAllFragments() {

            for(Fragment fg: fragmentList) {
                if(fg instanceof Fragment_Episodes) {
                    ((Fragment_Episodes) fg).loadRecyclerViewData();
                } else if(fg instanceof Fragment_Lieux) {
                    ((Fragment_Lieux) fg).loadRecyclerViewData();
                } else {
                    ((Fragment_Personnages) fg).loadRecyclerViewData();
                }
            }
    }

}


