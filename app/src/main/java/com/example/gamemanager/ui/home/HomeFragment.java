package com.example.gamemanager.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamemanager.R;

public class HomeFragment extends Fragment {
    TextView homeTv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeTv = root.findViewById(R.id.home_tv);
        homeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to login
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_authLoginFragment);
            }
        });
        return root;
    }
}