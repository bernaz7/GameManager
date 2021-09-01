package com.example.gamemanager.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
        navEmail = (TextView)emailView.findViewById(R.id.drawer_user_text);
        homeTv = root.findViewById(R.id.home_tv);
        Menu navMenu = navigationView.getMenu();

        if(navEmail.getText().toString() == getString(R.string.anon_user)) {
            navMenu.findItem(R.id.nav_games).setVisible(false);
            navMenu.findItem(R.id.nav_polls).setVisible(false);
            navMenu.findItem(R.id.nav_gangs).setVisible(false);

            homeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // navigate to login
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_authLoginFragment);
                }
            });
        }

        else {
            navMenu.findItem(R.id.nav_games).setVisible(true);
            navMenu.findItem(R.id.nav_polls).setVisible(true);
            navMenu.findItem(R.id.nav_gangs).setVisible(true);
            homeTv.setText(R.string.home_welcome);
        }
        return root;
    }
}