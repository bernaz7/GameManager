package com.example.gamemanager.ui.authentication;

import android.app.ProgressDialog;
import android.os.Bundle;

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

import com.example.gamemanager.R;
import com.example.gamemanager.model.ModelFirebase;

import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AuthRegisterFragment extends Fragment {

    EditText emailEt;
    EditText passwordEt;
    Button registerBtn;
    TextView badRegisterTv;
    private String email ="";
    private String password ="";
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_auth_register, container, false);
        emailEt = root.findViewById(R.id.register_email_et);
        passwordEt = root.findViewById(R.id.register_password_et);

        badRegisterTv = root.findViewById(R.id.register_badregister_tv);
        badRegisterTv.setVisibility(View.INVISIBLE);

        progressDialog = new ProgressDialog(root.getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating your account");
        progressDialog.setCanceledOnTouchOutside(false);


        registerBtn = root.findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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
            Register();
        }
    }

    private void Register() {
        progressDialog.show();
        ModelFirebase.firebaseRegister(email, password, new ModelFirebase.FirebaseRegisterListener() {
            @Override
            public void OnFirebaseRegisterSuccess(FirebaseUser user) {
                badRegisterTv.setVisibility(View.INVISIBLE);
                progressDialog.dismiss();
                new SweetAlertDialog(getActivity()) // https://ourcodeworld.com/articles/read/928/how-to-use-sweet-alert-dialogs-in-android
                        .setTitleText("Successfully signed up!")
                        .setContentText("Please login")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Navigation.findNavController(getView()).navigateUp();
                            }
                        })
                        .show();
            }

            @Override
            public void OnFirebaseRegisterFailure() {
                progressDialog.dismiss();
                badRegisterTv.setVisibility(View.VISIBLE);
            }
        });
    }
}