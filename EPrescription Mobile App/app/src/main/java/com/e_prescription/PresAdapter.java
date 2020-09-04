package com.e_prescription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PresAdapter extends RecyclerView.Adapter<PresAdapter.PresViewHolder> {
    Context context;
    ArrayList<String> fullNameList;
    ArrayList<String> nodayslist;
    ArrayList<String> fornoonlist;
    ArrayList<String> afternoonlist;
    ArrayList<String> nightlist;
    class PresViewHolder extends RecyclerView.ViewHolder {

        TextView full_name;
        Button b1, b2, b3;
        EditText nd;

        public PresViewHolder(View itemView) {
            super(itemView);
            full_name = (TextView) itemView.findViewById(R.id.full_name);
            b1 = itemView.findViewById(R.id.fn);
            b2 = itemView.findViewById(R.id.an);
            b3 = itemView.findViewById(R.id.ni8);
            nd = itemView.findViewById(R.id.days);

        }
    }
        public PresAdapter(Context context, ArrayList<String> fullNameList/*,ArrayList<String> nodayslist,ArrayList<String> fornoonlist,ArrayList<String> afternoonlist, ArrayList<String> nightlist*/) {
            this.context = context;
            this.fullNameList = fullNameList;
           /* this.nodayslist = nodayslist;
            this.fornoonlist = fornoonlist;
            this.afternoonlist = afternoonlist;
            this.nightlist = nightlist;*/
        }

        @Override
        public PresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.pres_list_items, parent, false);
            return new PresViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull PresViewHolder holder, int position) {
        holder.full_name.setText(fullNameList.get(position));

        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Forenoon",Toast.LENGTH_LONG).show();
            }
        });
        holder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Afternoon",Toast.LENGTH_LONG).show();
            }
        });
        holder.b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Night",Toast.LENGTH_LONG).show();
            }
        });
       /* holder.full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //f= (String) holder.full_name.getText();
               // Toast.makeText(context, "Full Name Clicked "+f, Toast.LENGTH_SHORT).show();
                final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(FirebaseAuth.getInstance().getUid()).child("Medicines");
               // databaseReference.push().setValue(" "+f);
                Toast.makeText(context,"saved",Toast.LENGTH_LONG).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return fullNameList.size();
    }
}




