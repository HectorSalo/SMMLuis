package com.skysam.firebase.seamosmejoresmaestros.luis.Adaptadores;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.skysam.firebase.seamosmejoresmaestros.luis.Fragments.Sala1Fragment;

public class SalasAdapter extends FragmentStatePagerAdapter {
    public SalasAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                Sala1Fragment sala1Fragment = new Sala1Fragment();
                return sala1Fragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
