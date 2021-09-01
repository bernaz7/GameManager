package com.example.gamemanager.ui.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Model;
import com.example.gamemanager.model.ModelFirebase;
import com.example.gamemanager.model.UserData;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class AuthRegisterFragment extends Fragment {

    EditText emailEt;
    EditText passwordEt;
    EditText fullnameEt;
    Button registerBtn;
    TextView badRegisterTv;
    private String email ="";
    private String password ="";
    private String fullname="";
    Bitmap imageBitmap;
    ImageView imageV;

    String _userId;
    String _fullname;
    String _emailId;
    View root;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_auth_register, container, false);
        emailEt = root.findViewById(R.id.register_email_et);
        passwordEt = root.findViewById(R.id.register_password_et);
        fullnameEt = root.findViewById(R.id.register_fullname_et);
        imageV = root.findViewById(R.id.register_img_view);

        imageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

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

    static final int REQUEST_IMAGE_CAPTURE = 1;
    void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if( resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imageV.setImageBitmap(imageBitmap);
            }
        }
    }
    private void validateData() {
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        fullname = fullnameEt.getText().toString().trim();

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
        else if (TextUtils.isEmpty(fullname)){
            fullnameEt.setError("Please enter fullname");
        }
        else {
            // everything is valid
            Register();
        }
    }

    private void Register() {
        progressDialog.show();
        UserData userData = new UserData(fullname,email);

        ModelFirebase.firebaseRegister(email, password, userData, new ModelFirebase.FirebaseRegisterListener() {
            @Override
            public void OnFirebaseRegisterSuccess(FirebaseUser user) {
                badRegisterTv.setVisibility(View.INVISIBLE);
                progressDialog.dismiss();
                _userId = userData.getId();
                _fullname = userData.getFullName();
                _emailId = userData.getEmailId();



                if(imageBitmap != null) {
                    Model.instance.uploadImage(imageBitmap, userData.getId(), new Model.UploadImageListener() {

                        @Override
                        public void onComplete(String url) {
                            userData.setImageUrl(url);
                            saveUserData(url);
                        }
                    });
                }
                else {
                    userData.setImageUrl(null);
                    saveUserData(null);
                }

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

    void saveUserData(String url) {
        UserData userData = new UserData();
        userData.setId(_userId);
        userData.setFullName(_fullname);
        userData.setEmailId(_emailId);
        if(url == null) {
            userData.setImageUrl("");
        }
        else {
            userData.setImageUrl(url);
        }
        Model.instance.saveUserData(userData);

    }
}