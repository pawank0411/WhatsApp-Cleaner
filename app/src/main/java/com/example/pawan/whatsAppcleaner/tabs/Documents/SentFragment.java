package com.example.pawan.whatsAppcleaner.tabs.Documents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawan.whatsAppcleaner.MainActivity;
import com.example.pawan.whatsAppcleaner.R;

public class SentFragment extends Fragment {

    public SentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.doc_activity, container, false);
    }

}