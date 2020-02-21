package com.example.prco.ui.usingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.prco.R;

public class UsingAppFragment extends Fragment {

    private UsingAppViewModel usingAppViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        usingAppViewModel =
                ViewModelProviders.of(this).get(UsingAppViewModel.class);
        View root = inflater.inflate(R.layout.fragment_usingapp, container, false);

        return root;
    }
}