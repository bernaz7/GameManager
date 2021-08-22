package com.example.gamemanager.model;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.gamemanager.GameManagerApp;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class UserData {
    @PrimaryKey
    @NonNull
    public String id;
    public String fullName;
    public String emailId;
    public Long lastUpdated;
    public Boolean isDeleted;
    //public String imageUrl;
    // TODO: add image for user.

    final static String ID = "id";
    final static String FULLNAME = "fullname";
    final static String EMAILID = "emailid";
    final static String LAST_UPDATED = "lastUpdated";
    private static final String USERDATA_LAST_UPDATE_DATE = "UserDataLastUpdate";
    final static String IS_DELETED = "isDeleted";


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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

    public UserData() {} // constructor
    public UserData(String fullName, String emailId) {
        this.fullName = fullName;
        this.emailId = emailId;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, id);
        json.put(FULLNAME, fullName);
        json.put(EMAILID, emailId);
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        json.put(IS_DELETED, isDeleted);
        return json;
    }

    static public UserData create(Map<String, Object> json) {
        UserData userData = new UserData();
        userData.id = (String) json.get(ID);
        userData.fullName = (String) json.get(FULLNAME);
        userData.emailId = (String)json.get(EMAILID);


        Timestamp ts = (Timestamp)json.get(LAST_UPDATED);
        if(ts != null) {
            userData.lastUpdated = new Long(ts.getSeconds());
        }
        else {
            userData.lastUpdated = new Long(0);
        }
        userData.isDeleted = (Boolean) json.get(IS_DELETED);
        if (userData.isDeleted == null) {
            userData.isDeleted = false;
        }
        return userData;
    }

    static public void setLocalLastUpdateTime (Long ts) {
        SharedPreferences.Editor editor = GameManagerApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        editor.putLong(USERDATA_LAST_UPDATE_DATE,ts);
        editor.commit();
    }

    static public Long getLocalLastUpdateTime () {
        return GameManagerApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong(USERDATA_LAST_UPDATE_DATE,0);

    }
}
