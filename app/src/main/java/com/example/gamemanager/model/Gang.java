package com.example.gamemanager.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.example.gamemanager.GameManagerApp;
import com.google.common.reflect.TypeToken;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Gang implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long id ;
    public String name;
    public String manager;
    public ArrayList<String> members = new ArrayList<String>();
    public Long lastUpdated;
    public Boolean isDeleted;
    public static Long uniqueId = Long.valueOf(0);

    final static String ID = "id";
    final static String NAME = "name";
    final static String MANAGER = "manager";
    final static String MEMBERS = "members";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public void addMember(String memberEmail) {
        if(!this.members.contains(memberEmail.toString()))
            this.members.add(memberEmail.toString());
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, id);
        json.put(NAME, name);
        json.put(MANAGER, manager);
        json.put(MEMBERS, members);
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        json.put(IS_DELETED, isDeleted);
        return json;
    }

    static public Gang create(Map<String, Object> json) {
        Gang gang = new Gang();
        gang.id = (Long) json.get(ID);
        gang.name = (String)json.get(NAME);
        gang.manager = (String)json.get(MANAGER);
        gang.members = (ArrayList<String>) json.get(MEMBERS);

        Timestamp ts = (Timestamp)json.get(LAST_UPDATED);
        if(ts != null) {
            gang.lastUpdated = new Long(ts.getSeconds());
        }
        else {
            gang.lastUpdated = new Long(0);
        }
        gang.isDeleted = (Boolean) json.get(IS_DELETED);
        if (gang.isDeleted == null) {
            gang.isDeleted = false;
        }
        return gang;
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
