package com.example.gamemanager.ui.games;

import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gamemanager.model.Model;
import com.example.gamemanager.model.Game;

import java.util.List;

public class GamesViewModel extends ViewModel {

    public LiveData<List<Game>> gamesList;

    public GamesViewModel() { gamesList = Model.instance.getAllGames(); }

    public LiveData<List<Game>> getData() {
        return gamesList;
    }

    public void refresh() {
        Model.instance.getAllGames();
    }
}