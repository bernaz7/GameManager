package com.example.gamemanager.model;

import android.util.Log;

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

    public LiveData<List<Gang>> getAllGangs() {
        gangsLoadingState.setValue(LoadingState.loading);
        // read local last update time
        Long localLastUpdate = Gang.getLocalLastUpdateTime();

        // get all updates from firebase
        ModelFirebase.getAllGangs(localLastUpdate,(gangs)-> {
            executorService.execute(()-> {
                Long lastUpdate = new Long(0);
                Log.d("TAG","firebase returned " + gangs.size());
                for ( Gang gang : gangs) {
                    Log.d("TAG","gang: " + gang.id +"\tlud: " + gang.lastUpdated);
                    // update the local DB with the new records
                    if(gang.isDeleted != null && gang.isDeleted) {
                        AppLocalDB.db.gangDao().delete(gang);
                    }
                    else {
                        AppLocalDB.db.gangDao().insertAll(gang);
                    }
                    // update the local last update time
                    if( lastUpdate < gang.lastUpdated) {
                        lastUpdate = gang.lastUpdated;
                    }
                }
                Gang.setLocalLastUpdateTime(lastUpdate);
                gangsLoadingState.postValue(LoadingState.loaded);
                // read all the data from the local DB -> return the data to the caller
                // automatically performed by room -> live data gets updated
            });
        });
        return allGangs;
    }

    public interface onCompleteListener {
        void onComplete();
    }

    public void saveGang(Gang gang, onCompleteListener listener) {
        gangsLoadingState.setValue(LoadingState.loading);
        ModelFirebase.saveGang(gang,()-> {
            getAllGangs();
            listener.onComplete();
        });
    }


}
