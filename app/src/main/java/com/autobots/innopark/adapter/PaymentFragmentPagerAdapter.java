package com.autobots.innopark.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.autobots.innopark.fragment.FinesListFragment;
import com.autobots.innopark.fragment.TariffListFragment;

public class PaymentFragmentPagerAdapter extends FragmentStatePagerAdapter
{

    private String tab_names[] = new String[] {"Tariff", "Fines"};
    private Context context;

    public PaymentFragmentPagerAdapter(@NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new TariffListFragment();
            case 1:
                return new FinesListFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return tab_names.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return tab_names[0];
            case 1:
                return tab_names[1];
            default:
                return null;
        }
    }
}
