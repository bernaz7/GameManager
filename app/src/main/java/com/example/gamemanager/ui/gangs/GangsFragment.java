package com.example.gamemanager.ui.gangs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.gamemanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GangsFragment extends Fragment {

    private GangsViewModel gangsViewModel;
    FloatingActionButton addBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gangsViewModel =
                new ViewModelProvider(this).get(GangsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gangs, container, false);
        final TextView textView = root.findViewById(R.id.ganglist_textview);
        gangsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        addBtn = root.findViewById(R.id.ganglist_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_newgangfragment_open);
            }
        });

        return root;
    }
}