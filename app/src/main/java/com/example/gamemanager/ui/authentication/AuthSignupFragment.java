package com.example.gamemanager.ui.authentication;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gamemanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthSignupFragment extends Fragment {

    EditText emailEt;
    EditText passwordEt;
    Button signupBtn;
    private String email ="";
    private String password ="";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_auth_signup, container, false);
        emailEt = root.findViewById(R.id.signup_email_et);
        passwordEt = root.findViewById(R.id.signup_password_et);

        //init firebase auth
        firebaseAuth = firebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getView().getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating your account");
        progressDialog.setCanceledOnTouchOutside(false);

        signupBtn = getView().findViewById(R.id.signup_signup_btn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });


        return root;
    }

    private void validateData() {
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();

        // validate
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //invalid email address
            emailEt.setError("Invalid email format");
        }
        else if(TextUtils.isEmpty(password)){
            // no password
            passwordEt.setError("Enter password");
        }
        else if (password.length()<6) {
            // password too short
            passwordEt.setError("Password must be at least 6 characters long");
        }
        else {
            // everything is valid
            firebaseSignUp();
        }
    }

    private void firebaseSignUp() {
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String email = firebaseUser.getEmail();
                        Toast.makeText(getActivity(), "Account created\n"+email, Toast.LENGTH_SHORT).show();

                        // TODO: navigate to profile fragment
                        //Navigation.findNavController(v).navigate(R.id.action_profile_open);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}