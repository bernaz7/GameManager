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

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.gamemanager.model.Gang.uniqueId;

public class GangInfoFragment extends Fragment {
    Gang gang;
    View root;
    TextView nameTv;
    ProgressBar progressBar;
    Button saveBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_gang_info, container, false);
        gang = (Gang) getArguments().getSerializable("gang");
        progressBar = root.findViewById(R.id.ganginfo_progbar);
        progressBar.setVisibility(View.INVISIBLE);

        nameTv = root.findViewById(R.id.ganginfo_name_text);
        nameTv.setText(gang.getName().toString());

        saveBtn = root.findViewById(R.id.ganginfo_save_btn);
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
        return root;
    }

    private void saveGang() {
        gang.setName(nameTv.getText().toString());
        Model.instance.saveGang(gang, ()-> {
            Navigation.findNavController(root).navigateUp();
        });
    }
}