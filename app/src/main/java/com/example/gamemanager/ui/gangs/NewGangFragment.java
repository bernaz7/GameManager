package com.example.gamemanager.ui.gangs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Gang;
import com.example.gamemanager.model.Model;
import com.google.android.material.navigation.NavigationView;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.gamemanager.model.Gang.uniqueId;


public class NewGangFragment extends Fragment {
    View view;
    Button saveBtn;
    ProgressBar progressBar;
    EditText nameEt;

    NavigationView navigationView;
    View emailView;
    TextView navEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_gang, container, false);
        nameEt = view.findViewById(R.id.newgang_name_text);
        saveBtn = view.findViewById(R.id.newgang_save_btn);
        progressBar = view.findViewById(R.id.newgang_progbar);
        progressBar.setVisibility(View.INVISIBLE);

        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.textView);

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

        return view;
    }

    private void saveGang() {
        Gang gang = new Gang();
        gang.setId(uniqueId);
        gang.setName(nameEt.getText().toString());
        gang.setManager(navEmail.getText().toString());
        Model.instance.saveGang(gang, ()-> {
            Navigation.findNavController(view).navigateUp();
        });
    }
}