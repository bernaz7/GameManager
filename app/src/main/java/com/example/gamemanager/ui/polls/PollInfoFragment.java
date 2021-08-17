package com.example.gamemanager.ui.polls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Model;
import com.example.gamemanager.model.Poll;
import com.google.android.material.navigation.NavigationView;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class PollInfoFragment extends Fragment {
    Poll poll;
    View root;
    TextView nameTv;
    ProgressBar progressBar;
    Button voteBtn;

    NavigationView navigationView;
    View emailView;
    TextView navEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_poll_info, container, false);
        poll = (Poll) getArguments().getSerializable("poll");
        progressBar = root.findViewById(R.id.pollinfo_progbar);
        progressBar.setVisibility(View.INVISIBLE);

        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.textView);
        nameTv = root.findViewById(R.id.pollinfo_name_text);
        voteBtn = root.findViewById(R.id.pollinfo_vote_btn);

        // if manager
        if(navEmail.getText().toString() == poll.getManager().toString()) {
            nameTv.setText(poll.getId().toString());

            voteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                    progressBar.setVisibility(View.VISIBLE);
                    voteBtn.setEnabled(false);

                    savePoll();
                }
            });
        }
        else { // if not manager
            if (!poll.voters.contains(navEmail.toString())) {
                nameTv.setText(poll.getId().toString());
                nameTv.setVisibility(View.INVISIBLE);
                voteBtn.setText("Vote");
                voteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        poll.voters.add(navEmail.toString());
                        savePoll();
                    }
                });
            }
            else {
                nameTv.setText("You've already voted!");
                nameTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                nameTv.setVisibility(View.VISIBLE);
                voteBtn.setText("Back");
                voteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigation.findNavController(root).navigateUp();
                    }
                });
            }
        }

        return root;
    }

    private void savePoll() {
        // edit votes
        Model.instance.savePoll(poll, ()-> {
            Navigation.findNavController(root).navigateUp();
        });
    }
}