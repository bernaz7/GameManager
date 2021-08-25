package com.example.gamemanager.ui.gangs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Gang;
import com.example.gamemanager.model.Model;
import com.example.gamemanager.model.ModelFirebase;
import com.google.android.material.navigation.NavigationView;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.gamemanager.model.Gang.uniqueId;

public class GangInfoFragment extends Fragment {
    Gang gang;
    View root;
    TextView nameTv;
    ProgressBar progressBar;
    Button saveBtn;
    Button deleteBtn;

    NavigationView navigationView;
    View emailView;
    TextView navEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_gang_info, container, false);
        gang = (Gang) getArguments().getSerializable("gang");
        progressBar = root.findViewById(R.id.ganginfo_progbar);
        progressBar.setVisibility(View.INVISIBLE);

        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.textView);
        nameTv = root.findViewById(R.id.ganginfo_name_text);
        saveBtn = root.findViewById(R.id.ganginfo_save_btn);
        deleteBtn = root.findViewById(R.id.ganginfo_delete_btn);
        // if manager
        if(navEmail.getText().toString().compareTo(gang.getManager()) == 0) {
            nameTv.setText(gang.getName().toString());

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                    progressBar.setVisibility(View.VISIBLE);
                    saveBtn.setEnabled(false);

                    saveGang();
                }
            });

            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.instance.deleteGang(gang, ()-> {
                        new SweetAlertDialog(getActivity()) // https://ourcodeworld.com/articles/read/928/how-to-use-sweet-alert-dialogs-in-android
                                .setTitleText("")
                                .setContentText("Gang deleted")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Navigation.findNavController(root).navigateUp();
                                    }
                                })
                                .show();
                    });
                }
            });
        }
        else { // if not manager
            nameTv.setText(gang.getName().toString());
            nameTv.setVisibility(View.INVISIBLE);
            saveBtn.setText("Join Gang");
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gang.addMember(navEmail.getText().toString());
                    saveGang();
                }
            });
        }

        return root;
    }

    private void saveGang() {
        gang.setName(nameTv.getText().toString());
        Model.instance.saveGang(gang, ()-> {
            Navigation.findNavController(root).navigateUp();
        });
    }
}