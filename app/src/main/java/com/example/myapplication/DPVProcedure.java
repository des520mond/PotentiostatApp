package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class DPVProcedure extends Fragment {

    FirebaseFirestore firestore;
    Button Backbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Firebase
        firestore = FirebaseFirestore.getInstance();

        Map<String, Object> users = new HashMap<>();
        users.put("Procedure","DPV");
        users.put("Date","testDate");
        users.put("Data","testData");

        firestore.collection("dpv").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(),"Success",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Failure",Toast.LENGTH_LONG).show();
            }
        });


        // Inflate the layout for this fragment
        Toast.makeText(getActivity(),"DPV Procedure Selected",Toast.LENGTH_SHORT).show();

        View view = inflater.inflate(R.layout.fragment_dpv_procedure, container, false);

        Backbtn = view.findViewById(R.id.btnBack);

        Backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Backbtn.setVisibility((View.GONE));
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainActivity,new Dashboard()).commit();
                Toast.makeText(getActivity(),"Back to Dashboard",Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }
}