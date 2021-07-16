package com.example.gamemanager.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {
    final static String gangsCollection = "gangs";
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
}
