package com.example.myapplication;

public class CVData {
    private double time;
    private double targetvoltage;
    private double appliedvoltage;
    private double current;

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getTargetvoltage() {
        return targetvoltage;
    }

    public void setTargetvoltage(double targetvoltage) {
        this.targetvoltage = targetvoltage;
    }

    public double getAppliedvoltage() {
        return appliedvoltage;
    }

    public void setAppliedvoltage(double appliedvoltage) {
        this.appliedvoltage = appliedvoltage;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "CVData{" +
                "time=" + time +
                ", targetvoltage=" + targetvoltage +
                ", appliedvoltage=" + appliedvoltage +
                ", current=" + current +
                '}';
    }
}
