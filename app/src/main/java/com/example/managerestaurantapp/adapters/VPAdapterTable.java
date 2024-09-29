package com.example.managerestaurantapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.managerestaurantapp.fragments.FragmentFloor1;
import com.example.managerestaurantapp.fragments.FragmentFloor2;

public class VPAdapterTable extends FragmentStateAdapter {
    public VPAdapterTable(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentFloor1();
            case 1:
                return new FragmentFloor2();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
