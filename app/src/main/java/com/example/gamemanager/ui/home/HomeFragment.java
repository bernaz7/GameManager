package com.example.gamemanager.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamemanager.R;
import com.google.android.material.navigation.NavigationView;

public class HomeFragment extends Fragment {
    TextView homeTv;
    NavigationView navigationView;
    View emailView;
    TextView navEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.textView);
        homeTv = root.findViewById(R.id.home_tv);

        if(navEmail.getText().toString() == getString(R.string.anon_user)) {

            homeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // navigate to login
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_authLoginFragment);
                }
            });
        }
        else {
            homeTv.setText(R.string.home_welcome);
        }
        return root;
    }
}