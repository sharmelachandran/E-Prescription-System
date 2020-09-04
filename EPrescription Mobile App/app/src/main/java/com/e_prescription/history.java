package com.e_prescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class history extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<PresHistory> list;
    private RecyclerAdapter adapter;
    private IntentIntegrator qrScan;
    private  SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient EP History");
       // toolbar.setDisplayHomeAsUpEnabled(true);

        sharedPreferences=getSharedPreferences("patientdetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String job=sharedPreferences.getString("job","NotDone");
        if(job.equals("Done")) {
            recyclerView = findViewById(R.id.recycle);
            layoutManager = new LinearLayoutManager(this);
            ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            list = new ArrayList<PresHistory>();
            String pid = sharedPreferences.getString("pnameandid", "dont");

            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Patients").child(pid).child("preslist");
            databaseReference1.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        PresHistory p = ds.getValue(PresHistory.class);
                        list.add(p);
                    }
                    adapter = new RecyclerAdapter(history.this, list);
                    recyclerView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Scan Qrcode to View Prescription History",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.pdf) {
            Intent i=new Intent(getApplicationContext(),Patientpres.class);
            startActivity(i);

        }
        return true;
    }



}
