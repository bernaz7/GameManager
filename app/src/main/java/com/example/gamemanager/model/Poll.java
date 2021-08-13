package com.example.gamemanager.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.gamemanager.GameManagerApp;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Poll implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long id ;
    public String manager;
    public ArrayList<String> voters = new ArrayList<String>();
    public Boolean isRunning;
    public Long lastUpdated;
    public Boolean isDeleted;
    public static Long uniqueId = Long.valueOf(0);

    final static String ID = "id";
    final static String MANAGER = "manager";
    final static String VOTERS = "voters";
    final static String IS_RUNNING = "isRunning";
    final static String LAST_UPDATED = "lastUpdated";
    private static final String GANG_LAST_UPDATE_DATE = "GangLastUpdate";
    final static String IS_DELETED = "isDeleted";

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public ArrayList<String> getVoters() {
        return voters;
    }

    public void setVoters(ArrayList<String> voters) {
        this.voters = voters;
    }

    public void addVoter(String memberEmail) {
        if(!this.voters.contains(memberEmail.toString()))
            this.voters.add(memberEmail.toString());
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, id);
        json.put(MANAGER, manager);
        json.put(VOTERS, voters);
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        json.put(IS_DELETED, isDeleted);
        json.put(IS_RUNNING,isRunning);
        return json;
    }

    static public Poll create(Map<String, Object> json) {
        Poll poll = new Poll();
        poll.id = (Long) json.get(ID);
        poll.manager = (String)json.get(MANAGER);
        poll.voters = (ArrayList<String>) json.get(VOTERS);
        poll.isRunning = (Boolean) json.get(IS_RUNNING);
        Timestamp ts = (Timestamp)json.get(LAST_UPDATED);
        if(ts != null) {
            poll.lastUpdated = new Long(ts.getSeconds());
        }
        else {
            poll.lastUpdated = new Long(0);
        }
        poll.isDeleted = (Boolean) json.get(IS_DELETED);
        if (poll.isDeleted == null) {
            poll.isDeleted = false;
        }
        return poll;
    }

    static public void setLocalLastUpdateTime (Long ts) {
        SharedPreferences.Editor editor = GameManagerApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        editor.putLong(GANG_LAST_UPDATE_DATE,ts);
        editor.commit();
    }

    static public Long getLocalLastUpdateTime () {
        return GameManagerApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong(GANG_LAST_UPDATE_DATE,0);

    }
}
