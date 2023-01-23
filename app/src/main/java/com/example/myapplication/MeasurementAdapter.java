package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementAdapter.MyViewHolder> {
    Context context;
    ArrayList<Measurement> list;

    public MeasurementAdapter(Context context, ArrayList<Measurement> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.measuremententry,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Measurement measurement = list.get(position);
        holder.Technique.setText(measurement.getTechnique());
        holder.MeasurementDateTime.setText(measurement.getMeasurementDateTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Technique, MeasurementDateTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Technique = itemView.findViewById(R.id.technique);
            MeasurementDateTime = itemView.findViewById(R.id.measurementdatetime);
        }
    }
}
