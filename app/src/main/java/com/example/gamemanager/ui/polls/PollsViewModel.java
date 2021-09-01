package com.example.gamemanager.ui.polls;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.gamemanager.model.Model;
import com.example.gamemanager.model.Poll;

import java.util.List;

public class PollsViewModel extends ViewModel {

    public LiveData<List<Poll>> pollsList;

    public PollsViewModel() { pollsList = Model.instance.getAllPolls(); }

    public LiveData<List<Poll>> getData() {
        return pollsList;
    }

    public void refresh() {
        Model.instance.getAllPolls();
    }
}