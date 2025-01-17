package com.example.prco.ui.planning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.prco.R;

public class PlanningFragment extends Fragment {

    private PlanningViewModel planningViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        planningViewModel =
                ViewModelProviders.of(this).get(PlanningViewModel.class);
        View root = inflater.inflate(R.layout.fragment_planning, container, false);


        return root;
    }
}