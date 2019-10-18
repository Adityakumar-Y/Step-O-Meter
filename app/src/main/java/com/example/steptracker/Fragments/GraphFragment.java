package com.example.steptracker.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steptracker.Activities.MainActivity;
import com.example.steptracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GraphFragment extends Fragment {


    private static final String TAG = "GraphFragment";
    private View view;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_graph, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView();
        return view;
    }


    private void setupRecyclerView() {
        ((MainActivity) getActivity()).getAllData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(((MainActivity) getActivity()).adapter);

    }



}
