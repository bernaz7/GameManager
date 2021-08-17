package com.example.gamemanager.ui.polls;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamemanager.R;
import com.example.gamemanager.ui.polls.calendar.CalendarUtils;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.gamemanager.ui.polls.calendar.CalendarUtils.daysInWeekArray;
import static com.example.gamemanager.ui.polls.calendar.CalendarUtils.monthYearFromDate;

public class NewPollFragment extends Fragment {
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
    View root;
    ArrayList<LocalDate> weekDays;
    ArrayList<LocalDate> chosenDays = new ArrayList<>();
    Button prevBtn;
    Button nextBtn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_new_poll, container, false);
        CalendarUtils.selectedDate = LocalDate.now();
        monthYearText = root.findViewById(R.id.newpoll_monthYear_tv);
        sunNum = root.findViewById(R.id.newpoll_sun_num);
        monNum = root.findViewById(R.id.newpoll_mon_num);
        tueNum = root.findViewById(R.id.newpoll_tue_num);
        wedNum = root.findViewById(R.id.newpoll_wed_num);
        thurNum = root.findViewById(R.id.newpoll_thur_num);
        friNum = root.findViewById(R.id.newpoll_fri_num);
        satNum = root.findViewById(R.id.newpoll_sat_num);
        prevBtn = root.findViewById(R.id.newpoll_prev_btn);
        nextBtn = root.findViewById(R.id.newpoll_next_btn);

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

        sunNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refreshWeek()
    {
        weekDays = daysInWeekArray(CalendarUtils.selectedDate);

        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        sunNum.setText(String.valueOf(weekDays.get(0).getDayOfMonth()));
        monNum.setText(String.valueOf(weekDays.get(1).getDayOfMonth()));
        tueNum.setText(String.valueOf(weekDays.get(2).getDayOfMonth()));
        wedNum.setText(String.valueOf(weekDays.get(3).getDayOfMonth()));
        thurNum.setText(String.valueOf(weekDays.get(4).getDayOfMonth()));
        friNum.setText(String.valueOf(weekDays.get(5).getDayOfMonth()));
        satNum.setText(String.valueOf(weekDays.get(6).getDayOfMonth()));
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

}