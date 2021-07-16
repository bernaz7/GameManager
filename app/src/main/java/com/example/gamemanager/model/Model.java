package com.example.gamemanager.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Model {
    final public static Model instance = new Model();
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    public enum LoadingState{
        loaded,
        loading,
        error
    }
    public MutableLiveData<LoadingState> gangsLoadingState= new MutableLiveData<LoadingState>(LoadingState.loaded);
    LiveData<List<Gang>> allGangs = AppLocalDB.db.gangDao().getAll();
    private Model() {    }

    public interface GetDataListener {
        void onComplete(List<Gang> data);
    }
}
