package com.example.gamemanager.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.gamemanager.GameManagerApp;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Gang {
    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String manager;
    //public int[] members; TODO: add members
    public Long lastUpdated;
    public Boolean isDeleted;

    final static String ID = "id";
    final static String NAME = "name";
    final static String MANAGER = "manager";
    //final static String MEMBERS = "members";
    final static String LAST_UPDATED = "lastUpdated";
    private static final String GANG_LAST_UPDATE_DATE = "GangLastUpdate";
    final static String IS_DELETED = "isDeleted";

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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

//    public int[] getMembers() {
//        return members;
//    }
//
//    public void setMembers(int[] members) {
//        this.members = members;
//    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, id);
        json.put(NAME, name);
        json.put(MANAGER, manager);
        //json.put(MEMBERS, members);
        //TODO: after firebase:
        // json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        json.put(IS_DELETED, isDeleted);
        return json;
    }

    static public Gang create(Map<String, Object> json) {
        Gang st = new Gang();
        st.id = (String)json.get(ID);
        st.name = (String)json.get(NAME);
        st.manager = (String)json.get(MANAGER);
        //st.members = (int[])json.get(MEMBERS);

        //TODO: after firebase
        // Timestamp ts = (Timestamp)json.get(LAST_UPDATED);
//        if(ts != null) {
//            st.lastUpdated = new Long(ts.getSeconds());
//        }
//        else {
//            st.lastUpdated = new Long(0);
//        }
        st.isDeleted = (Boolean) json.get(IS_DELETED);
        if (st.isDeleted == null) {
            st.isDeleted = false;
        }
        return st;
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
