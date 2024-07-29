package com.example.healthapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.healthapp.Fragment.fragment_home;
import com.example.healthapp.Fragment.fragment_hoso;
import com.example.healthapp.Fragment.fragment_lichsu;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new fragment_home();
            case 1:
                return new fragment_lichsu();
            case 2:
                return new fragment_hoso();
            default:
                return new fragment_home();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
