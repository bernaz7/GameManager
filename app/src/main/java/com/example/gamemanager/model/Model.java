package com.example.gamemanager.model;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Model {
    final public static Model instance = new Model();

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public enum LoadingState{
        loaded,
        loading,
        error
    }
    public MutableLiveData<LoadingState> gangsLoadingState= new MutableLiveData<LoadingState>(LoadingState.loaded);
    public MutableLiveData<LoadingState> usersLoadingState= new MutableLiveData<LoadingState>(LoadingState.loaded);
    public MutableLiveData<LoadingState> pollsLoadingState= new MutableLiveData<LoadingState>(LoadingState.loaded);
    public MutableLiveData<LoadingState> gamesLoadingState= new MutableLiveData<LoadingState>(LoadingState.loaded);

    LiveData<List<Gang>> allGangs = AppLocalDB.db.gangDao().getAll();
    LiveData<List<UserData>> allUsers = AppLocalDB.db.userDataDao().getAll();
    LiveData<List<Poll>> allPolls = AppLocalDB.db.pollDao().getAll();
    LiveData<List<Game>> allGames = AppLocalDB.db.gameDao().getAll();

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

    public LiveData<List<UserData>> getAllUsers() {
        usersLoadingState.setValue(LoadingState.loading);
        // read local last update time
        Long localLastUpdate = UserData.getLocalLastUpdateTime();

        // get all updates from firebase
        ModelFirebase.getAllUsers(localLastUpdate,(users)-> {
            executorService.execute(()-> {
                Long lastUpdate = new Long(0);
                Log.d("TAG","firebase returned " + users.size());
                for ( UserData userData : users) {
                    Log.d("TAG","gang: " + userData.id +"\tlud: " + userData.lastUpdated);
                    // update the local DB with the new records
                    if(userData.isDeleted != null && userData.isDeleted) {
                        AppLocalDB.db.userDataDao().delete(userData);
                    }
                    else {
                        AppLocalDB.db.userDataDao().insertAll(userData);
                    }
                    // update the local last update time
                    if( lastUpdate < userData.lastUpdated) {
                        lastUpdate = userData.lastUpdated;
                    }
                }
                UserData.setLocalLastUpdateTime(lastUpdate);
                usersLoadingState.postValue(LoadingState.loaded);
                // read all the data from the local DB -> return the data to the caller
                // automatically performed by room -> live data gets updated
            });
        });
        return allUsers;
    }

    public LiveData<List<Poll>> getAllPolls() {
        pollsLoadingState.setValue(LoadingState.loading);
        // read local last update time
        Long localLastUpdate = Poll.getLocalLastUpdateTime();

        // get all updates from firebase
        ModelFirebase.getAllPolls(localLastUpdate,(polls)-> {
            executorService.execute(()-> {
                Long lastUpdate = new Long(0);
                Log.d("TAG","firebase returned " + polls.size());
                for ( Poll poll : polls) {
                    Log.d("TAG","gang: " + poll.id +"\tlud: " + poll.lastUpdated);
                    // update the local DB with the new records
                    if(poll.isDeleted != null && poll.isDeleted) {
                        AppLocalDB.db.pollDao().delete(poll);
                    }
                    else {
                        AppLocalDB.db.pollDao().insertAll(poll);
                    }
                    // update the local last update time
                    if( lastUpdate < poll.lastUpdated) {
                        lastUpdate = poll.lastUpdated;
                    }
                }
                Poll.setLocalLastUpdateTime(lastUpdate);
                pollsLoadingState.postValue(LoadingState.loaded);
                // read all the data from the local DB -> return the data to the caller
                // automatically performed by room -> live data gets updated
            });
        });
        return allPolls;
    }

    public LiveData<List<Game>> getAllGames() {
        gamesLoadingState.setValue(LoadingState.loading);
        // read local last update time
        Long localLastUpdate = Game.getLocalLastUpdateTime();

        // get all updates from firebase
        ModelFirebase.getAllGames(localLastUpdate,(games)-> {
            executorService.execute(()-> {
                Long lastUpdate = new Long(0);
                Log.d("TAG","firebase returned " + games.size());
                for ( Game game : games) {
                    Log.d("TAG","gang: " + game.id +"\tlud: " + game.lastUpdated);
                    // update the local DB with the new records
                    if(game.isDeleted != null && game.isDeleted) {
                        AppLocalDB.db.gameDao().delete(game);
                    }
                    else {
                        AppLocalDB.db.gameDao().insertAll(game);
                    }
                    // update the local last update time
                    if( lastUpdate < game.lastUpdated) {
                        lastUpdate = game.lastUpdated;
                    }
                }
                Game.setLocalLastUpdateTime(lastUpdate);
                gamesLoadingState.postValue(LoadingState.loaded);
                // read all the data from the local DB -> return the data to the caller
                // automatically performed by room -> live data gets updated
            });
        });
        return allGames;
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

    public void savePoll(Poll poll, onCompleteListener listener) {
        pollsLoadingState.setValue(LoadingState.loading);
        ModelFirebase.savePoll(poll,()-> {
            getAllPolls();
            listener.onComplete();
        });
    }

    public void saveGame(Game game, onCompleteListener listener) {
        gamesLoadingState.setValue(LoadingState.loading);
        ModelFirebase.saveGame(game,()-> {
            getAllGames();
            listener.onComplete();
        });
    }

    public void saveUserData(UserData userData) {
        usersLoadingState.setValue(LoadingState.loading);
        ModelFirebase.saveUserData(userData,()-> {
            getAllUsers();
        });
    }
    public void deleteGang(Gang gang, onCompleteListener listener) {
        gang.setDeleted(true);
        AppLocalDB.db.gangDao().delete(gang);
        ModelFirebase.deleteGang(gang, () -> {
            listener.onComplete();
        });
    }

    public void deletePoll(Poll poll, onCompleteListener listener) {
        poll.setDeleted(true);
        AppLocalDB.db.pollDao().delete(poll);
        ModelFirebase.deletePoll(poll, () -> {
            listener.onComplete();
        });
    }

    public void deleteGame(Game game, onCompleteListener listener) {
        game.setDeleted(true);
        AppLocalDB.db.gameDao().delete(game);
        ModelFirebase.deleteGame(game, () -> {
            listener.onComplete();
        });
    }

    public interface CheckUserListener {
        void onComplete();
    }

    public void checkUser() {
        ModelFirebase.checkUser(new ModelFirebase.FirebaseCheckUserListener() {
            @Override
            public FirebaseUser onComplete(FirebaseUser firebaseUser) {
                return firebaseUser;
            }
        });
    }

    public interface UploadImageListener {
        void onComplete(String url);
    }

    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener){
        ModelFirebase.uploadImage(imageBmp,name,listener);
    }

    public UserData getUserDataByEmail(String email) {
        return AppLocalDB.db.userDataDao().getUserDataByEmail(email);
    }
}
