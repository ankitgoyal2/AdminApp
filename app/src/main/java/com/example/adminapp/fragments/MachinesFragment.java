package com.example.adminapp.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MachinesFragment extends Fragment {

    public MachinesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setSharedElementEnterTransition(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.setSharedElementEnterTransition(requireContext());
        return inflater.inflate(R.layout.fragment_machines, container, false);
    }
}
