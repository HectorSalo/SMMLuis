package com.skysam.firebase.seamosmejoresmaestros.luis.Adaptadores;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.skysam.firebase.seamosmejoresmaestros.luis.Fragments.EventosFragment;

public class InicioAdapter extends FragmentStatePagerAdapter {
    public InicioAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                EventosFragment eventosFragment = new EventosFragment();
                return eventosFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
