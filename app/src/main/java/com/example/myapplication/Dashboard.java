package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Dashboard extends Fragment {

    Button CVbtn;
    Button CAPbtn;
    Button DPVbtn;
    Button EISbtn;
    RecyclerView recyclerView;
    ArrayList<Measurement> list;
    MeasurementAdapter adapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_dashboard, container, false);
        CVbtn = view.findViewById(R.id.btnCV);
        DPVbtn = view.findViewById(R.id.btnDPV);
        EISbtn = view.findViewById(R.id.btnEIS);
        CAPbtn = view.findViewById(R.id.btnCAP);


        progressDialog = new ProgressDialog((getActivity()));
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<Measurement>();
        adapter = new MeasurementAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
        EventChangeListener();


        CVbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                CVbtn.setVisibility((View.GONE));
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainActivity,new CVProcedure()).commit();
            }
        });

        CAPbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                CAPbtn.setVisibility((View.GONE));
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainActivity,new CVProcedure()).commit();
            }
        });

        DPVbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                DPVbtn.setVisibility((View.GONE));
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainActivity,new DPVProcedure()).commit();
            }
        });

        EISbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                EISbtn.setVisibility((View.GONE));
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainActivity,new EISProcedure()).commit();
            }
        });



        return view;
    }

    private void EventChangeListener() {
//        db.collection("users").orderBy("FirstName", Query.Direction.ASCENDING)
        db.collection("cv").orderBy("MeasurementDateTime", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Log.e("Firestore Error!", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc: value.getDocumentChanges()){

                            if (dc.getType() == DocumentChange.Type.ADDED){
                                list.add(dc.getDocument().toObject((Measurement.class)));
                            }
                            adapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                    }
                });


    }
}