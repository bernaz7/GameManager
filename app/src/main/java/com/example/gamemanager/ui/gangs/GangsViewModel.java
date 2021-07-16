package com.example.gamemanager.ui.gangs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GangsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GangsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gang fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}