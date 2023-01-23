package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;

public class CVMeasurement extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {

    FirebaseFirestore firestore;
    Date currentDateTime = Calendar.getInstance().getTime();
    Button Startbtn;
    Button Savebtn;
    Button Returnbtn;
    private ScatterChart mChart;
    private TextView UpperVertexPotentialtxt;
    private TextView LowerVertexPotentialtxt;
    private TextView ScanRatetxt;
    private TextView NoOfCrosstxt;
    private List<CVData> testdata = new ArrayList<>();
    private List<Double> Time = new ArrayList<>();
    private List<Double> Vtarget = new ArrayList<>();
    private List<Double> Vapplied = new ArrayList<>();
    private List<Double> Current = new ArrayList<>();
    private boolean ScanFlag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cv_measurement, container, false);
        UpperVertexPotentialtxt = view.findViewById(R.id.txtUpperVertexPotential);
        LowerVertexPotentialtxt = view.findViewById(R.id.txtLowerVertexPotential);
        ScanRatetxt = view.findViewById(R.id.txtScanRate);
        NoOfCrosstxt = view.findViewById(R.id.txtNoOfCross);
        Bundle bundle = this.getArguments();
        UpperVertexPotentialtxt.setText(bundle.getString("UpperVertexPotential"));
        LowerVertexPotentialtxt.setText(bundle.getString("LowerVertexPotential"));
        ScanRatetxt.setText(bundle.getString("ScanRate"));
        NoOfCrosstxt.setText(bundle.getString("NoOfCross"));
        Startbtn = view.findViewById(R.id.btnStart);
        Savebtn = view.findViewById(R.id.btnSave);
        Returnbtn = view.findViewById(R.id.btnReturn);
        Savebtn.setEnabled(false);

        readCSVData();

        mChart = view.findViewById(R.id.scatterChart);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.getDescription().setText("");
        mChart.setNoDataText("No Data Available");
//        ScatterData data = new ScatterData();
//        mChart.setData(data);

        XAxis x1 = mChart.getXAxis();
        x1.setAxisMaximum(800f);
        x1.setAxisMinimum(-800f);
        ScanFlag = false;

//        YAxis y1 = mChart.getAxisLeft();
//        y1.setAxisMaximum(50f);
//
//        YAxis y2 = mChart.getAxisLeft();
//        y2.setAxisMaximum(-50f);


        Startbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Startbtn.setEnabled(false);
                ScanFlag = true;
                Toast.makeText(getActivity(),"Start Scanning",Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(ScanFlag == true)
                        {
                            addEntry();
                            Log.d("MainActivity","Measurements Done ");
                        }
                        ScanFlag = false;
                    }
                }).start();
                Savebtn.setEnabled(true);
            }
        });



        Savebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Savebtn.setEnabled(false);
                Savebtn.setText("Measurement Saved");
                Toast.makeText(getActivity(),"Measurement Saved",Toast.LENGTH_SHORT).show();
                firestore = FirebaseFirestore.getInstance();
                Map<String, Object> users = new HashMap<>();
                users.put("Technique","CV");
                users.put("MeasurementDateTime",currentDateTime.toString());
                users.put("Vmax",bundle.getString("UpperVertexPotential"));
                users.put("Vmin",bundle.getString("LowerVertexPotential"));
                users.put("ScanRate",bundle.getString("ScanRate"));
                users.put("NoOfCross",bundle.getString("NoOfCross"));
                users.put("Time",Time);
                users.put("TargetVoltage",Vtarget);
                users.put("AppliedVoltage",Vapplied);
                users.put("Current",Current);
                firestore.collection("cv").add(users);

            }
        });

        Returnbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Returnbtn.setVisibility((View.GONE));
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainActivity,new Dashboard()).commit();
                Toast.makeText(getActivity(),"Back to Dashboard",Toast.LENGTH_SHORT).show();

            }
        });
        Log.d("MainActivity","Measurements - Interface Loaded ");
        return view;
    }

//    private void addEntry(){
//        ArrayList<Entry> data = new ArrayList<>();
//
//        if (data != null){
//            ScatterDataSet set = new ScatterDataSet(data,"CV Measurement");
//            if (set == null){
//                set = createSet();
//                data.addDateSet(set);
//            }
//
//            int i = 0;
//            while(i<testdata.size()) {
//                data.add(new Entry((float)testdata.get(i).getAppliedvoltage(),(float)testdata.get(i).getCurrent()));
//                Log.d("MainActivity", "Measurements: " + (float)testdata.get(i).getCurrent());
//                i++;
//            }
//            Log.d("MainActivity", "Measurements Size: " + i);
//            mChart.notifyDataSetChanged();
//
//        }
//
//
//        ScatterDataSet set1 = new ScatterDataSet(yValues,"CV Measurement");
//
//        ArrayList<IScatterDataSet> dataSets = new ArrayList<>();
//        set1.setScatterShapeSize(8f);
//        dataSets.add((set1));
//
//        ScatterData data = new ScatterData(dataSets);
//    }

        private void addEntry(){
            try{
                Thread.sleep(5000);
            } catch (InterruptedException e){
            }
            ArrayList<Entry> yValues = new ArrayList<>();
            int i = 0;
            while(i<testdata.size()) {
                yValues.add(new Entry((float)testdata.get(i).getAppliedvoltage(),(float)testdata.get(i).getCurrent()));
                Log.d("MainActivity", "Measurements: " + (float)testdata.get(i).getAppliedvoltage() + ", " + (float)testdata.get(i).getCurrent());
//                Log.d("MainActivity", "Measurements Size: " + i);
                ScatterDataSet set1 = new ScatterDataSet(yValues,"CV Measurement");
                set1.setDrawValues(false);
                set1.setScatterShape(ScatterChart.ScatterShape.SQUARE);
                set1.setColor(ColorTemplate.COLORFUL_COLORS[0]);
                ArrayList<IScatterDataSet> dataSets = new ArrayList<>();
                set1.setScatterShapeSize(8f);
                dataSets.add((set1));

                ScatterData data = new ScatterData(dataSets);
//        data.setValueTextColor(Color.WHITE);
                mChart.setData(data);
                mChart.invalidate();

                i++;
                try{
                    Thread.sleep(200);
                } catch (InterruptedException e){
                }
            }
//            Savebtn.setEnabled(true); // will crash
//            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.mainActivity,new Dashboard()).commit();
        }





//    private ScatterDataSet createSet(){
//        ScatterDataSet set = new ScatterDataSet(null,"CV Measurement");
//        set.setScatterShape(ScatterChart.ScatterShape.SQUARE);
//        set.setColor(ColorTemplate.COLORFUL_COLORS[0]);
//        return set;
//    }

    @Override
    public void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(ScanFlag == true)
                {
//                    addEntry();
                    Log.d("MainActivity","Measurements - Interface Loaded ");
                }
                ScanFlag = false;
            }
        }).start();;

    }


    private void readCSVData(){
        InputStream is = getResources().openRawResource(R.raw.apptestdata_cv_k6fecn3);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        try {
            reader.readLine(); // skip over headers
            while ((line = reader.readLine()) != null) {
                Log.d("MainActivity","Line: " + line);
                String[] tokens = line.split(",");

                CVData data = new CVData();
                if(tokens.length >=4 && tokens[0].length() > 0){
                    data.setTime(Double.parseDouble(tokens[0]));
                }else{
                    data.setTime(0);
                }

                if(tokens.length >=4 && tokens[1].length() > 0){
                    data.setTargetvoltage(Double.parseDouble(tokens[1]));
                }else{
                    data.setTargetvoltage(0);
                }

                if(tokens.length >=4 && tokens[2].length() > 0){
                    data.setAppliedvoltage(Double.parseDouble(tokens[2]));
                }else{
                    data.setAppliedvoltage(0);
                }

                if(tokens.length >=4 && tokens[3].length() > 0){
                    data.setCurrent(Double.parseDouble(tokens[3]));
                }else{
                    data.setCurrent(0);
                }
                testdata.add(data);
                Log.d("MainActivity","Just created: " + data);
            }
        } catch (IOException e) {
            Log.wtf("MainActivity", "Error reading data file on line" + line, e);

        }
        Log.d("MainActivity","Finished Reading");

        int i = 0;
        while(i<testdata.size()){
            Time.add(testdata.get(i).getTime());
            Vtarget.add(testdata.get(i).getTargetvoltage());
            Vapplied.add(testdata.get(i).getAppliedvoltage());
            Current.add(testdata.get(i).getCurrent());
            i++;
        }
//        Log.d("MainActivity","Time: " + Time);
//        Log.d("MainActivity","Vtarget: " + Vtarget);
//        Log.d("MainActivity","Vapplied: " + Vapplied);
//        Log.d("MainActivity","Current: " + Current);
        }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
