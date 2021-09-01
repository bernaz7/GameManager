package com.example.gamemanager.ui.gangs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.gamemanager.model.Gang;
import com.example.gamemanager.model.Model;

import java.util.List;

public class GangsViewModel extends ViewModel {

    public LiveData<List<Gang>> gangsList;

    public GangsViewModel() { gangsList = Model.instance.getAllGangs(); }

    public LiveData<List<Gang>> getData() {
        return gangsList;
    }

    public void refresh() {
        Model.instance.getAllGangs();
    }
}