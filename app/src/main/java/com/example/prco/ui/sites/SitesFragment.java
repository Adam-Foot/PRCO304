package com.example.prco.ui.sites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.prco.R;

public class SitesFragment extends Fragment {

    private SitesViewModel sitesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sitesViewModel =
                ViewModelProviders.of(this).get(SitesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);

        return root;
    }
}