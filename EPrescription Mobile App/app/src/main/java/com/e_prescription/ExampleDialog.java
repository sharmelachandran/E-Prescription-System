package com.e_prescription;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExampleDialog extends AppCompatDialogFragment {
    EditText nod;
    TextView t1;
    CheckBox b1,b2,b3;
    String nod1,med,dose=" ";
    int flag=0;
    public static ExampleDialog newInstance(String title)
    {
        ExampleDialog exampleDialog=new ExampleDialog();
        Bundle bundle=new Bundle();
        bundle.putString("MEDICINE_NAME",title);
        exampleDialog.setArguments(bundle);
        return exampleDialog;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.layout_dialog,null);
        final home parent=(home)getActivity();
        nod=view.findViewById(R.id.nod);
        t1=view.findViewById(R.id.textView4);
        b1=view.findViewById(R.id.checkBox);
        b2=view.findViewById(R.id.checkBox2);
        b3=view.findViewById(R.id.checkBox3);
        t1.setText(getArguments().getString("MEDICINE_NAME"));
        med=t1.getText().toString().trim();

      // final DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(FirebaseAuth.getInstance().getUid()).child("Medicines").child(s);
        //ref.child("nod").setValue(nod.getText().toString());
       b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dose=dose+".FN ";
                flag=1;
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dose=dose+".AN";
               flag=1;
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dose=dose+".Night";
               flag=1;
            }
        });
        builder.setView(view).setTitle("DOSAGE DETAILS").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getContext(),"Medicine Deleted",Toast.LENGTH_LONG).show();
            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               nod1=nod.getText().toString();
               if(nod1.isEmpty()||dose.isEmpty()||flag==0)
               {
                   Toast.makeText(getContext(),"Enter Dosage details ",Toast.LENGTH_LONG).show();
               }else{
                   parent.dialogDone(true,med,nod1,dose);
               }

            }
        });
        return builder.create();
    }
}
