package com.example.gamemanager.ui.gangs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.EntityDeletionOrUpdateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Gang;
import com.example.gamemanager.model.Model;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class NewGangFragment extends Fragment {
    View view;
    Button saveBtn;
    ProgressBar progressBar;
    EditText nameEt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_gang, container, false);
        nameEt = view.findViewById(R.id.newgang_name_text);
        saveBtn = view.findViewById(R.id.newgang_save_btn);
        progressBar = view.findViewById(R.id.newgang_progbar);
        progressBar.setVisibility(View.INVISIBLE);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                progressBar.setVisibility(View.VISIBLE);
                saveBtn.setEnabled(false);

                saveGang(null);
            }
        });

        return view;
    }

    private void saveGang(Object o) {
        Gang gang = new Gang();
        gang.setName(nameEt.getText().toString());
        Model.instance.saveGang(gang, ()-> {
            Navigation.findNavController(view).navigateUp();
        });
    }
}