package com.e_prescription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<PresHistory> list;
    private Context context;

    public RecyclerAdapter(Context context,List<PresHistory> list) {
        this.list = list;
        this.context=context;
    }

    public RecyclerAdapter() {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.historydetails,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.doctor.setText(list.get(position).getDoctor());
        holder.sym.setText(list.get(position).getSymptoms());
        holder.med.setText(list.get(position).getMedicines());
        holder.date.setText(list.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView doctor,med,sym,date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            doctor=itemView.findViewById(R.id.doctor);
            med=itemView.findViewById(R.id.medicines);
            sym=itemView.findViewById(R.id.symptoms);
            date=itemView.findViewById(R.id.date);


        }
    }
}
