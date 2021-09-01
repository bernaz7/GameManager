package com.example.gamemanager.ui.polls;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Model;
import com.example.gamemanager.model.Poll;
import com.example.gamemanager.ui.polls.calendar.CalendarUtils;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PollInfoFragment extends Fragment {
    Poll poll;
    View root;
    TextView nameTv;
    ProgressBar progressBar;
    Button voteBtn;
    Button deleteBtn;
    Button createGameBtn;
    Button endPollBtn;
    TextView[] pollOptionViews;
    LinearLayout optionsLayout;
    LinearLayout optionsLayout2;


    NavigationView navigationView;
    View emailView;
    TextView navEmail;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_poll_info, container, false);
        poll = (Poll) getArguments().getSerializable("poll");
        optionsLayout = root.findViewById(R.id.pollinfo_options_layout);
        optionsLayout2 = root.findViewById(R.id.pollinfo_options_layout2);
        pollOptionViews = new TextView[poll.getOptions().size()];
        for(int i=0; i<poll.getOptions().size(); i++)
        {
            TextView optionTextView = new TextView(this.getContext());
            LocalDate date = CalendarUtils.stringToLocalDate(poll.getOptions().get(i));
            optionTextView.setText(date.getDayOfWeek().toString() + "\n" + date.toString());
            optionTextView.setTextSize(16);
            optionTextView.setPadding(10, 10, 10, 10);
            optionTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            optionTextView.setTextColor(getResources().getColor(R.color.black));
            optionTextView.setBackgroundColor(getResources().getColor(R.color.gray));
            optionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        v.setSelected(false);
                        v.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    else {
                        v.setSelected(true);
                        v.setBackgroundColor(getResources().getColor(R.color.orange));
                    }
                }
            });

            if(i<4)
                optionsLayout.addView(optionTextView);
            else
                optionsLayout2.addView(optionTextView);
            pollOptionViews[i] = optionTextView;
        }

        progressBar = root.findViewById(R.id.pollinfo_progbar);
        progressBar.setVisibility(View.INVISIBLE);

        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.drawer_user_text);

        nameTv = root.findViewById(R.id.pollinfo_name_text);
        voteBtn = root.findViewById(R.id.pollinfo_vote_btn);
        deleteBtn = root.findViewById(R.id.pollinfo_delete_btn);
        endPollBtn = root.findViewById(R.id.pollinfo_endpoll_btn);
        createGameBtn = root.findViewById(R.id.pollinfo_creategame_btn);

        //if manager no matter what (for delete, endpoll and create game btns)
        if(navEmail.getText().toString().compareTo(poll.getManager()) == 0)
        {
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.instance.deletePoll(poll, ()-> {
                        new SweetAlertDialog(getActivity()) // https://ourcodeworld.com/articles/read/928/how-to-use-sweet-alert-dialogs-in-android
                                .setTitleText("")
                                .setContentText("Poll deleted")
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
        if(poll.getRunning())
        {
            // if manager
            if(navEmail.getText().equals(poll.getManager())) {
                nameTv.setText(poll.getId().toString());

                voteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        voteBtn.setEnabled(false);

                        savePoll();
                    }                });

                endPollBtn.setVisibility(View.VISIBLE);
                endPollBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        poll.setRunning(false);

                        savePoll();
                    }
                });
            }
            // everyone
            if (!poll.voters.contains(navEmail.getText().toString())) { // if didn't vote
                nameTv.setText(poll.getId().toString());
                nameTv.setVisibility(View.INVISIBLE);
                voteBtn.setText("Vote");
                voteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        poll.voters.add(navEmail.getText().toString());
                        for(int i=0; i<poll.getOptions().size(); i++)
                        {
                            if(pollOptionViews[i].isSelected())
                            {
                                poll.votes.set(i,poll.votes.get(i)+1);
                            }
                        }
                        savePoll();
                    }
                });
            }
            else { // already voted
                nameTv.setText("You've already voted!");
                nameTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                nameTv.setVisibility(View.VISIBLE);

                for(int i=0; i<poll.getOptions().size(); i++) {
                    String currentText = pollOptionViews[i].getText().toString();
                    pollOptionViews[i].setText(currentText + "\nVotes: " + poll.votes.get(i));
                    pollOptionViews[i].setOnClickListener(null);
                    pollOptionViews[i].setBackgroundColor(getResources().getColor(R.color.orange));
                }
                voteBtn.setText("Back");
                voteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigation.findNavController(root).navigateUp();
                    }
                });
            }
        }
        else {   // poll has ended
            nameTv.setText("Poll has ended!");
            nameTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            nameTv.setVisibility(View.VISIBLE);
            int winnerDate=0;

            for(int i=0; i<poll.getOptions().size(); i++) {
                String currentText = pollOptionViews[i].getText().toString();
                pollOptionViews[i].setText(currentText + "\nVotes: " + poll.votes.get(i));
                pollOptionViews[i].setOnClickListener(null);
                pollOptionViews[i].setBackgroundColor(getResources().getColor(R.color.gray));
                if(poll.votes.get(i) > poll.votes.get(winnerDate))
                    winnerDate = i;
            }

            if (navEmail.getText().equals(poll.getManager())) { // if manager
                createGameBtn.setVisibility(View.VISIBLE);
                int finalWinnerDate = winnerDate;
                createGameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        String[] s = pollOptionViews[finalWinnerDate].getText().toString().split("\n");
                        bundle.putString("date",s[1]);
                        Navigation.findNavController(v).navigate(R.id.action_pollInfoFragment_to_newGameFragment,bundle);
                    }
                });
            }

            pollOptionViews[winnerDate].setBackgroundColor(getResources().getColor(R.color.orange));
            voteBtn.setText("Back");
            voteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(root).navigateUp();
                }
            });
        }

        return root;
    }

    private void savePoll() {
        Model.instance.savePoll(poll, ()-> {
            Navigation.findNavController(root).navigateUp();
        });
    }
}