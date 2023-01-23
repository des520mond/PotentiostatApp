package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CVProcedure extends Fragment {

    FirebaseFirestore firestore;
    Button Backbtn;
    Button Nextbtn;
    private TextInputEditText UpperVertexPotentialedt;
    private TextInputEditText LowerVertexPotentialedt;
    private TextInputEditText ScanRateedt;
    private TextInputEditText NoOfCrossedt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Firebase
//        firestore = FirebaseFirestore.getInstance();
//
//        Map<String, Object> users = new HashMap<>();
//        users.put("Procedure","CV");
//        users.put("Date","testDate");
//        users.put("Data","testData");
//
//        firestore.collection("cv").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Toast.makeText(getActivity(),"Success",Toast.LENGTH_LONG).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getActivity(),"Failure",Toast.LENGTH_LONG).show();
//            }
//        });


        // Inflate the layout for this fragment
        Toast.makeText(getActivity(),"CV Procedure Selected",Toast.LENGTH_SHORT).show();

        View view = inflater.inflate(R.layout.fragment_cv_procedure, container, false);

        Backbtn = view.findViewById(R.id.btnBack);
        Nextbtn = view.findViewById(R.id.btnNext);
        UpperVertexPotentialedt = view.findViewById(R.id.edtUpperVertexPotential);
        LowerVertexPotentialedt = view.findViewById(R.id.edtLowerVertexPotential);
        ScanRateedt = view.findViewById(R.id.edtScanRate);
        NoOfCrossedt = view.findViewById(R.id.edtNoOfCross);


        Backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Backbtn.setVisibility((View.GONE));
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainActivity,new Dashboard()).commit();
                Toast.makeText(getActivity(),"Back to Dashboard",Toast.LENGTH_SHORT).show();

            }
        });

        Nextbtn.setOnClickListener(new View.OnClickListener(){
            boolean next = true;
            @Override
            public void onClick(View view){

                Bundle bundle = new Bundle();
                bundle.putString("UpperVertexPotential",UpperVertexPotentialedt.getText().toString() + " mV");
                bundle.putString("LowerVertexPotential",LowerVertexPotentialedt.getText().toString()+ " mV");
                bundle.putString("ScanRate",ScanRateedt.getText().toString()+ " mV/s");
                bundle.putString("NoOfCross",NoOfCrossedt.getText().toString());
                CVMeasurement cvMeasurement = new CVMeasurement();
                cvMeasurement.setArguments(bundle);
                if(checkEmpty(UpperVertexPotentialedt) && checkEmpty(LowerVertexPotentialedt) && checkEmpty(ScanRateedt) && checkEmpty(NoOfCrossedt))
                {
                    Nextbtn.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainActivity,cvMeasurement).commit();
                    Toast.makeText(getActivity(),"Measurement Starts",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private boolean checkEmpty(TextInputEditText textInputEditText){
        String edt = textInputEditText.getText().toString();
        if (edt.isEmpty()){
            textInputEditText.setError("Field can't be empty");
            Toast.makeText(getActivity(),"Please fill out all the fields",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            textInputEditText.setError(null);
            return true;
        }

    }
}