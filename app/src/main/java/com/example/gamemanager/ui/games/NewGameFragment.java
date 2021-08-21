package com.example.gamemanager.ui.games;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Model;
import com.example.gamemanager.model.Poll;
import com.example.gamemanager.ui.polls.calendar.CalendarUtils;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.gamemanager.model.Poll.uniqueId;
import static com.example.gamemanager.ui.polls.calendar.CalendarUtils.daysInWeekArray;
import static com.example.gamemanager.ui.polls.calendar.CalendarUtils.monthYearFromDate;

public class NewGameFragment extends Fragment {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    TextView sunNum;
    TextView monNum;
    TextView tueNum;
    TextView wedNum;
    TextView thurNum;
    TextView friNum;
    TextView satNum;
    View view;
    ArrayList<LocalDate> weekDays;
    ArrayList<LocalDate> chosenDays = new ArrayList<>();
    Button prevBtn;
    Button nextBtn;
    Button createBtn;

    NavigationView navigationView;
    View emailView;
    TextView navEmail;

    final static int SUNDAY = 0;
    final static int MONDAY = 1;
    final static int TUESDAY = 2;
    final static int WEDNESDAY = 3;
    final static int THURSDAY = 4;
    final static int FRIDAY = 5;
    final static int SATURDAY = 6;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_poll, container, false);
        CalendarUtils.selectedDate = LocalDate.now();
        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.textView);
        monthYearText = view.findViewById(R.id.newpoll_monthYear_tv);

        sunNum = view.findViewById(R.id.newpoll_sun_num);
        monNum = view.findViewById(R.id.newpoll_mon_num);
        tueNum = view.findViewById(R.id.newpoll_tue_num);
        wedNum = view.findViewById(R.id.newpoll_wed_num);
        thurNum = view.findViewById(R.id.newpoll_thur_num);
        friNum = view.findViewById(R.id.newpoll_fri_num);
        satNum = view.findViewById(R.id.newpoll_sat_num);
        prevBtn = view.findViewById(R.id.newpoll_prev_btn);
        nextBtn = view.findViewById(R.id.newpoll_next_btn);
        createBtn = view.findViewById(R.id.newpoll_create_btn);

        sunNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    v.setBackgroundColor(Color.TRANSPARENT);
                    chosenDays.remove(weekDays.get(SUNDAY));
                }
                else {
                    v.setSelected(true);
                    v.setBackgroundColor(getResources().getColor(R.color.orange));
                    chosenDays.add(weekDays.get(SUNDAY));
                }
            }
        });
        monNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    v.setBackgroundColor(Color.TRANSPARENT);
                    chosenDays.remove(weekDays.get(MONDAY));
                }
                else {
                    v.setSelected(true);
                    v.setBackgroundColor(getResources().getColor(R.color.orange));
                    chosenDays.add(weekDays.get(MONDAY));
                }
            }
        });
        tueNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    v.setBackgroundColor(Color.TRANSPARENT);
                    chosenDays.remove(weekDays.get(TUESDAY));
                }
                else {
                    v.setSelected(true);
                    v.setBackgroundColor(getResources().getColor(R.color.orange));
                    chosenDays.add(weekDays.get(TUESDAY));
                }
            }
        });
        wedNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    v.setBackgroundColor(Color.TRANSPARENT);
                    chosenDays.remove(weekDays.get(WEDNESDAY));
                }
                else {
                    v.setSelected(true);
                    v.setBackgroundColor(getResources().getColor(R.color.orange));
                    chosenDays.add(weekDays.get(WEDNESDAY));
                }
            }
        });
        thurNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    v.setBackgroundColor(Color.TRANSPARENT);
                    chosenDays.remove(weekDays.get(THURSDAY));
                }
                else {
                    v.setSelected(true);
                    v.setBackgroundColor(getResources().getColor(R.color.orange));
                    chosenDays.add(weekDays.get(THURSDAY));
                }
            }
        });
        friNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    v.setBackgroundColor(Color.TRANSPARENT);
                    chosenDays.remove(weekDays.get(FRIDAY));
                }
                else {
                    v.setSelected(true);
                    v.setBackgroundColor(getResources().getColor(R.color.orange));
                    chosenDays.add(weekDays.get(FRIDAY));
                }
            }
        });
        satNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    v.setBackgroundColor(Color.TRANSPARENT);
                    chosenDays.remove(weekDays.get(SATURDAY));
                }
                else {
                    v.setSelected(true);
                    v.setBackgroundColor(getResources().getColor(R.color.orange));
                    chosenDays.add(weekDays.get(SATURDAY));
                }
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousWeekAction();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWeekAction();
            }
        });
        refreshWeek();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBtn.setEnabled(false);
                savePoll();
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refreshWeek()
    {
        weekDays = daysInWeekArray(CalendarUtils.selectedDate);

        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        sunNum.setText(String.valueOf(weekDays.get(SUNDAY).getDayOfMonth()));
        monNum.setText(String.valueOf(weekDays.get(MONDAY).getDayOfMonth()));
        tueNum.setText(String.valueOf(weekDays.get(TUESDAY).getDayOfMonth()));
        wedNum.setText(String.valueOf(weekDays.get(WEDNESDAY).getDayOfMonth()));
        thurNum.setText(String.valueOf(weekDays.get(THURSDAY).getDayOfMonth()));
        friNum.setText(String.valueOf(weekDays.get(FRIDAY).getDayOfMonth()));
        satNum.setText(String.valueOf(weekDays.get(SATURDAY).getDayOfMonth()));

        sunNum.setSelected(false);
        monNum.setSelected(false);
        tueNum.setSelected(false);
        wedNum.setSelected(false);
        thurNum.setSelected(false);
        friNum.setSelected(false);
        satNum.setSelected(false);

        sunNum.setBackgroundColor(Color.TRANSPARENT);
        monNum.setBackgroundColor(Color.TRANSPARENT);
        tueNum.setBackgroundColor(Color.TRANSPARENT);
        wedNum.setBackgroundColor(Color.TRANSPARENT);
        thurNum.setBackgroundColor(Color.TRANSPARENT);
        friNum.setBackgroundColor(Color.TRANSPARENT);
        satNum.setBackgroundColor(Color.TRANSPARENT);

        chosenDays.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousWeekAction()
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        refreshWeek();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextWeekAction()
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        refreshWeek();
    }

    private void savePoll() {
        Poll poll = new Poll();
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<Integer> temp2 = new ArrayList<>();
        poll.setId(uniqueId);
        poll.setManager(navEmail.getText().toString());
        for (LocalDate day: chosenDays) {
            temp.add(day.toString());
            temp2.add(0);
        }
        poll.setOptions(temp);
        poll.setVotes(temp2);

        poll.setRunning(true);
        Model.instance.savePoll(poll, ()-> {
            Navigation.findNavController(view).navigateUp();
        });
    }

}