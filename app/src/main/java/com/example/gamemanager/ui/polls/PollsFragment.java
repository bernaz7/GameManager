package com.example.gamemanager.ui.polls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gamemanager.R;

public class PollsFragment extends Fragment {

    private PollsViewModel pollsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pollsViewModel =
                new ViewModelProvider(this).get(PollsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_polls, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        pollsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}