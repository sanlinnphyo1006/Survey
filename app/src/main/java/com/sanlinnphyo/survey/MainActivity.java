package com.sanlinnphyo.survey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // first show loading screen
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_main, new LoadingFragment());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("survey").document("status").addSnapshotListener((snapshot, error) -> {
           if(snapshot != null && snapshot.exists()){
               if(String.valueOf(Objects.requireNonNull(snapshot.getData()).get("canSubmit")).equals("true")){
                   ft.replace(R.id.frame_main, new FormFragment());
                   ft.commit();
               }else{
                   ft.replace(R.id.frame_main, new LoadingFragment());
                   ft.commit();
               }
           }
        });
    }
}