package com.example.gamemanager.model;



import android.util.Log;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.Timestamp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ModelFirebase {
    final static String gangsCollection = "gangs";
    final static String userDataCollection = "userData";
    private ModelFirebase(){}

    public interface GetAllGangsListener {
        public void onComplete(List<Gang> gangs);
    }

    public static void getAllGangs(Long since,GetAllGangsListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(gangsCollection)
                .whereGreaterThanOrEqualTo(Gang.LAST_UPDATED,new Timestamp(since,0))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Gang> list = new LinkedList<Gang>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                list.add(Gang.create(document.getData()));
                            }
                        } else {
                        }

                        listener.onComplete(list);
                    }
                });
    }

    public static void saveGang(Gang gang, Model.onCompleteListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(gangsCollection).document(gang.id.toString())
                .set(gang.toJson())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Gang.uniqueId++;
                        listener.onComplete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onComplete();
                    }
                });
    }

    public interface FirebaseLoginListener {
        public void OnFirebaseLoginSuccess(FirebaseUser user);
        public void OnFirebaseLoginFailure();
    }

    public static void firebaseLogin(String email, String password, FirebaseLoginListener listener) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//                        Log.d("TAG",""+firebaseUser.getUid());
                        listener.OnFirebaseLoginSuccess(firebaseUser);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.OnFirebaseLoginFailure();
                    }
                });
    }

    public interface FirebaseCheckUserListener {
        public FirebaseUser onComplete(FirebaseUser firebaseUser);
    }

    public static void checkUser(FirebaseCheckUserListener listener) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        listener.onComplete(firebaseUser);
    }

    public interface FirebaseRegisterListener {
        public void OnFirebaseRegisterSuccess(FirebaseUser user);
        public void OnFirebaseRegisterFailure();
    }


    public static void firebaseRegister(String email, String password, UserData userData, FirebaseRegisterListener listener) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection(userDataCollection).document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .set(userData.toJson());
//                        FirebaseDatabase.getInstance().getReference("UserData")
//                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userData);
                        listener.OnFirebaseRegisterSuccess(firebaseUser);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.d("TAG",e.getMessage());
                        listener.OnFirebaseRegisterFailure();
                    }
                });
    }
}
