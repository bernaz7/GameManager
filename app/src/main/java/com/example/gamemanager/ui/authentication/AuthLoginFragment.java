package com.example.gamemanager.ui.authentication;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

public class AuthLoginFragment extends Fragment {

    private Button loginBtn;
    private String email ="";
    private String password="";
    EditText emailEt;
    EditText passwordEt;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_auth_login, container, false);

        emailEt = root.findViewById(R.id.login_email_et);
        passwordEt = root.findViewById(R.id.login_password_et);
        loginBtn = root.findViewById(R.id.login_login_btn);



        progressDialog = new ProgressDialog(getView().getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Logging in");
        progressDialog.setCanceledOnTouchOutside(false);

        //init firebase auth
        firebaseAuth = firebaseAuth.getInstance();
        checkUser();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        return root;
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            //navigate to profile fragment?
        }
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
        else {
            // everything is valid
            firebaseLogin();
        }
    }

    private void firebaseLogin() {
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String email = firebaseUser.getEmail();
                        Toast.makeText(getActivity(), "Logged in\n"+email, Toast.LENGTH_SHORT).show();

                        // navigate?
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