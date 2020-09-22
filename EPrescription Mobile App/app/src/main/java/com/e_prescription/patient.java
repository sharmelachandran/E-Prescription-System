package com.e_prescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class patient extends AppCompatActivity {
    TextView n,pid;
    Button log;
    ImageButton scanqr;
    private IntentIntegrator qrScan;
    String pname="name",ppid="id",nn;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        n=findViewById(R.id.name);
        pid=findViewById(R.id.id);
        log=findViewById(R.id.login);
        scanqr=findViewById(R.id.scan);
        qrScan = new IntentIntegrator(this);
        progressDialog = new ProgressDialog(this);
        scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
                
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Wait for a while!!!");
                progressDialog.show();
                if (TextUtils.isEmpty(pid.getText().toString().trim()) && TextUtils.isEmpty(n.getText().toString().trim())) {
                    pid.setError("Required");
                    n.setError("Required");
                } else if (TextUtils.isEmpty(pid.getText().toString().trim())) {
                    pid.setError("Enter valid mail id");
                } else if (TextUtils.isEmpty(n.getText().toString().trim())) {
                    n.setError("Required");
                } else {
                    final String pname = n.getText().toString().trim();
                    final String ppid = pid.getText().toString().trim();
                    Intent i=new Intent(getApplicationContext(),Patientpres.class);
                    startActivity(i);
                   /* DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Patients").child(pname+ppid);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(patname.equals(null)||patid.equals(null))
                            {
                                Toast.makeText(getApplicationContext(),"invalid details",Toast.LENGTH_LONG).show();
                                patname= dataSnapshot.child("name").getValue().toString();
                                patid= dataSnapshot.child("pid").getValue().toString();

                            }
                            else if(patid.equals(ppid)&&patname.equals(pname))
                            {
                                patname= dataSnapshot.child("name").getValue().toString();
                                patid= dataSnapshot.child("pid").getValue().toString();
                                Intent i=new Intent(getApplicationContext(),Patientpres.class);
                                i.putExtra("name",patname);
                                i.putExtra("id",patid);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"invalid details",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/


                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    pname=obj.getString("name");
                    ppid=obj.getString("pid");
                    nn=pname+ppid;
                    SharedPreferences sharedPreferences=getSharedPreferences("patientdetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("pnameandid",nn);
                    editor.apply();

                    //String name="Patient Name:"+pname+"Patient-id:"+ppid;
                    n.setText(pname);
                    pid.setText(ppid);
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Patients").child(nn);
                    ref.child("name").setValue(pname);
                    ref.child("pid").setValue(ppid);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
