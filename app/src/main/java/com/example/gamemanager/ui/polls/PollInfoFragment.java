package com.example.gamemanager.ui.polls;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import static android.content.Context.INPUT_METHOD_SERVICE;

public class PollInfoFragment extends Fragment {
    Poll poll;
    View root;
    TextView nameTv;
    ProgressBar progressBar;
    Button voteBtn;
    TextView[] pollOptionViews;
    LinearLayout optionsLayout;
//    ListView list;
//    MyAdapter adapter;

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
        pollOptionViews = new TextView[poll.getOptions().size()];
        for(int i=0; i<poll.getOptions().size(); i++)
        {
            TextView optionTextView = new TextView(this.getContext());
            LocalDate date = CalendarUtils.stringToLocalDate(poll.getOptions().get(i));
            optionTextView.setText(date.getDayOfWeek().toString() + "\n" + date.toString());
            optionTextView.setTextSize(18);
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

            optionsLayout.addView(optionTextView);
            pollOptionViews[i] = optionTextView;
        }
//        list = root.findViewById(R.id.pollinfo_listv);
//        adapter = new MyAdapter();
//        list.setAdapter(adapter);
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

//    class MyAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            if(poll.getOptions() != null)
//                return poll.getOptions().size();
//            else
//                return 0;
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if(convertView == null) {
//                convertView = getLayoutInflater().inflate(R.layout.pollday_option,null);
//            }
//            TextView dayText = convertView.findViewById(R.id.pollday_text);
//            TextView numText = convertView.findViewById(R.id.pollday_num);
//            //Student student = studentsListViewModel.getData().getValue().get(position);
//
//            Poll poll = list.poll.getOptions().get(position);
//
//            dayText.setText(CalendarUtils.stringToLocalDate(option).getDayOfWeek().toString());
//            numText.setText(CalendarUtils.stringToLocalDate(option).getDayOfMonth());
////            nameTv.setText(student.getName());
////            idTv.setText(student.getId());
////            imageV.setImageResource(R.drawable.download);
////            if(student.avatar != null && student.avatar != "") {
////                Picasso.get().load(student.avatar).placeholder(R.drawable.download)
////                        .error(R.drawable.download).into(imageV);
////            }
//
//
//            return convertView;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//    }

    private void savePoll() {
        // TODO: edit votes
        Model.instance.savePoll(poll, ()-> {
            Navigation.findNavController(root).navigateUp();
        });
    }
}