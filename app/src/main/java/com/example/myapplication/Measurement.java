package com.example.myapplication;


import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Measurement {
    private String Technique;
    private String MeasurementDateTime;

    public Measurement() {}

    public Measurement(String Technique, String MeasurementDateTime) {
        this.Technique = Technique;
        this.MeasurementDateTime = MeasurementDateTime;
    }

    public String getTechnique() {
        return Technique;
    }

    public void setTechnique(String technique) {
        Technique = technique;
    }

    public String getMeasurementDateTime() {
        return MeasurementDateTime;
    }

    public void setMeasurementDateTime(String measurementDateTime) {
        MeasurementDateTime = measurementDateTime;
    }

}
