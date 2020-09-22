package com.e_prescription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class QrScan extends AppCompatActivity {
    private IntentIntegrator qrScan;
    private TextView pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patienthistory);
        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        pd=findViewById(R.id.patientdetails);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    String pname=obj.getString("name");
                    String ppid=obj.getString("pid");
                    String pa=obj.getString("dob");
                    String padd=obj.getString("add");
                    String pphn=obj.getString("phn");
                    String pmail=obj.getString("mail");
                    padd=padd.trim();
                    String nn=pname+ppid;
                    String name="Patient Name:"+pname+"\nPatient-id:"+ppid+"\nAge:"+pa+"\nAddress:"+padd+"\nPhone:"+pphn+"\nMail-id:"+pmail+".";
                    //nn=pt.getText().toString();
                    SharedPreferences sharedPreferences=getSharedPreferences("patientdetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("details",name);
                    editor.putString("pnameandid",nn);
                    editor.putString("job","Done");
                    editor.apply();
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Patients").child(nn);
                    ref.child("name").setValue(pname);
                    ref.child("pid").setValue(ppid);
                    finish();
                   }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                    pd.setText("QrCode Scanned Successfully");

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



}
