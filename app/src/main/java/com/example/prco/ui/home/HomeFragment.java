package com.example.prco.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.prco.R;
import com.example.prco.ui.googlemaps.NearbyLocationsFragment;
import com.example.prco.ui.landmarkselfie.LandmarkSelfie;
import com.example.prco.ui.sitewalks.SiteWalks;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button nearbylocations = root.findViewById(R.id.btnNearby);
        Button sitewalks = root.findViewById(R.id.btnSiteWalk);
        Button selfie = root.findViewById(R.id.btnSelfieLandmark);

        nearbylocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), NearbyLocationsFragment.class);
                startActivityForResult(myIntent, 0);
            }
        });

        sitewalks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent siteIntent = new Intent(v.getContext(), SiteWalks.class);
                startActivityForResult(siteIntent, 0);
            }
        });

        selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selfieIntent = new Intent(v.getContext(), LandmarkSelfie.class);
                startActivityForResult(selfieIntent, 0);
            }
        });


        return root;



    }
}