package com.sanlinnphyo.survey;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormFragment extends Fragment {

    public FormFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnSubmit = view.findViewById(R.id.btn_submit);

        RadioGroup rg1 = view.findViewById(R.id.rg_1);
        RadioGroup rg2 = view.findViewById(R.id.rg_2);
        RadioGroup rg3 = view.findViewById(R.id.rg_3);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        btnSubmit.setOnClickListener(v -> {
            if(rg1.getCheckedRadioButtonId() == -1 && rg2.getCheckedRadioButtonId() == -1 && rg3.getCheckedRadioButtonId() == -1){
                Toast.makeText(getContext(), "You have to select at least one.", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> data = new HashMap<>();

            if(rg1.getCheckedRadioButtonId() != -1){
                RadioButton rb = view.findViewById(rg1.getCheckedRadioButtonId());
                data.put("1", String.valueOf(rb.getText()));
            }

            if(rg2.getCheckedRadioButtonId() != -1){
                RadioButton rb = view.findViewById(rg2.getCheckedRadioButtonId());
                data.put("2", String.valueOf(rb.getText()));
            }

            if(rg3.getCheckedRadioButtonId() != -1){
                RadioButton rb = view.findViewById(rg3.getCheckedRadioButtonId());
                data.put("3", String.valueOf(rb.getText()));
            }

            Toast.makeText(getContext(), "Submitting data.", Toast.LENGTH_SHORT).show();
            btnSubmit.setText("Submitting");
            btnSubmit.setEnabled(false);

            db.collection("survey")
                    .document("datasDocument")
                    .collection("datasCollection")
                    .add(data)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "Successfully uploaded data.", Toast.LENGTH_SHORT).show();
                        rg1.clearCheck();
                        rg2.clearCheck();
                        rg3.clearCheck();
                        btnSubmit.setText("Submit");
                        btnSubmit.setEnabled(true);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Oops! Something wrong in uploading data.", Toast.LENGTH_SHORT).show();
                        rg1.clearCheck();
                        rg2.clearCheck();
                        rg3.clearCheck();
                        btnSubmit.setText("Submit");
                        btnSubmit.setEnabled(true);
                    });
        });
    }
}