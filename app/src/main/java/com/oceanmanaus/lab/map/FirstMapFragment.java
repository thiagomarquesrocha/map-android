package com.oceanmanaus.lab.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by oceanmanaus on 11/22/2016.
 */

public class FirstMapFragment extends SupportMapFragment {

    public FirstMapFragment() {
    }

    public static FirstMapFragment newInstance(){
        return new FirstMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        return root;
    }
}
