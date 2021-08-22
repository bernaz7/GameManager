package com.example.gamemanager.ui.games;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gamemanager.GameManagerApp;
import com.example.gamemanager.R;
import com.example.gamemanager.model.Game;
import com.example.gamemanager.model.Model;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
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
    String date;
    String time;




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

                        date = String.format("%02d-%02d-%02d",year,monthOfYear+1,dayOfMonth);
                        timePickerDialog.show();
                    }
                }, mYear, mMonth, mDay);

        timePickerDialog = new TimePickerDialog(view.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        time = String.format("%02d:%02d", hourOfDay, minute);
                        timeText.setText("Date: "+date+", Time: "+time);
                    }
                }, mHour, mMinute, false);

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
        game.setTime(time);
        game.setDate(date);
        game.setLocation(locationText.getText().toString());
        //game.setUsers();
        Model.instance.saveGame(game, ()-> {
            Navigation.findNavController(view).navigateUp();
        });
    }

}