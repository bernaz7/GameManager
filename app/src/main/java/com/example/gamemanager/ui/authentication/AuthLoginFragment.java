package com.example.gamemanager.ui.authentication;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamemanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AuthLoginFragment extends Fragment {

    private Button loginBtn;
    private String email ="";
    private String password="";
    EditText emailEt;
    EditText passwordEt;
    TextView gotAccountTv;
    TextView badLoginTv;
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
        gotAccountTv = root.findViewById(R.id.login_gotacccount_tv);

        badLoginTv = root.findViewById(R.id.login_badlogin_tv);
        badLoginTv.setVisibility(View.INVISIBLE);


        progressDialog = new ProgressDialog(root.getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Logging in");
        progressDialog.setCanceledOnTouchOutside(false);

        //init firebase auth
        firebaseAuth = firebaseAuth.getInstance();
        checkUser();

        loginBtn.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            validateData();
        });

        gotAccountTv.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_authLoginFragment_to_authSignupFragment);
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
                        badLoginTv.setVisibility(View.INVISIBLE);

                        progressDialog.dismiss();
                        new SweetAlertDialog(getActivity()) // https://ourcodeworld.com/articles/read/928/how-to-use-sweet-alert-dialogs-in-android
                                .setTitleText("Successfully logged in!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Navigation.findNavController(getView()).navigateUp();
                                    }
                                })
                                .show();
                        // do logged in stuff here
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        badLoginTv.setVisibility(View.VISIBLE);
                    }
                });
    }
}