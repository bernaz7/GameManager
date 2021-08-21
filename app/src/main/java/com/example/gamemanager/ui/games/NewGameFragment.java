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
import com.example.gamemanager.model.Game;
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

    NavigationView navigationView;
    View emailView;
    TextView navEmail;
    View view;
    Button createBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_game, container, false);

        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.textView);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBtn.setEnabled(false);
                saveGame();
            }
        });

        return view;
    }


    private void saveGame() {
        Game game = new Game();
        game.setId(uniqueId);
        game.setManager(navEmail.getText().toString());
        //game.setLocation();
        //game.setUsers();
        Model.instance.saveGame(game, ()-> {
            Navigation.findNavController(view).navigateUp();
        });
    }

}