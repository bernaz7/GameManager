package com.example.gamemanager.ui.games;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gamemanager.R;
import com.example.gamemanager.model.Game;
import com.example.gamemanager.model.Model;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.gamemanager.model.Game.uniqueId;

public class NewGameFragment extends Fragment {

    NavigationView navigationView;
    View emailView;
    TextView navEmail;

    View view;
    Button createBtn;
    EditText nameText;
    EditText timeText;
    EditText locationText;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_game, container, false);

        navigationView =  getActivity().findViewById(R.id.nav_view);
        emailView = navigationView.getHeaderView(0);
        navEmail = (TextView)emailView.findViewById(R.id.textView);

        nameText = view.findViewById(R.id.newgame_name_text);
        timeText = view.findViewById(R.id.newgame_time_text);
        locationText = view.findViewById(R.id.newgame_location_text);

        nameText.setText(navEmail.getText().toString() + "'s Game");

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        datePickerDialog = new DatePickerDialog(view.getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        timeText.setText("Date: "+String.valueOf(year)+'-'+
                                String.valueOf(monthOfYear+1)+'-'+
                                String.valueOf(dayOfMonth));
                        timePickerDialog.show();
                    }
                }, mYear, mMonth, mDay);

        timePickerDialog = new TimePickerDialog(view.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        timeText.setText(timeText.getText().toString()+", Time: " +
                                String.valueOf(hourOfDay) + ':' +
                                String.valueOf(minute));
                    }
                }, mHour, mMinute, false);
//
//        timeText.setOnClickListener(new View.OnClickListener() {
//            @Override
//
//        });

        timeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    datePickerDialog.show();
            }
        });

        createBtn = view.findViewById(R.id.newgame_save_btn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                createBtn.setEnabled(false);
                saveGame();
            }
        });

        return view;
    }


    private void saveGame() {
        Game game = new Game();
        game.setId(uniqueId);
        game.setName(nameText.getText().toString());
        game.setManager(navEmail.getText().toString());
        game.addUser(navEmail.getText().toString());
        game.setTime(timeText.getText().toString());
        game.setLocation(locationText.getText().toString());
        //game.setUsers();
        Model.instance.saveGame(game, ()-> {
            Navigation.findNavController(view).navigateUp();
        });
    }

}