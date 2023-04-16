package com.example.forum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    String userName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity mainActivity = (MainActivity) getActivity();

        if (mainActivity != null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                userName = bundle.getString("userName");
            }
            mainActivity.findViewById(R.id.image_slider).setVisibility(View.VISIBLE);
            mainActivity.findViewById(R.id.textView).setVisibility(View.VISIBLE);

        }

        return inflater.inflate(R.layout.fragment_home, container, false);


    }
}