package com.example.gamemanager.ui.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.gamemanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    EditText emailTv;
    Button logoutBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_auth_profile, container, false);

        emailTv = root.findViewById(R.id.profile_email_tv);
        logoutBtn = root.findViewById(R.id.profile_logout_btn);

        firebaseAuth = firebaseAuth.getInstance();
        checkUser();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        return root;
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null) {
            //navigate to login fragment?
        }
        else {
            String email = firebaseUser.getEmail();
            emailTv.setText(email);
        }
    }
}