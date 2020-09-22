package com.e_prescription;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Pharmacy extends AppCompatActivity {
    ListView lv;
    DatabaseReference databaseReference,db,dr;
    ArrayList<String> medlist=new ArrayList<>();
    ArrayList<String> statuslist=new ArrayList<>();
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayList<String> nameList=new ArrayList<>();
    ArrayList<String> maillist=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    int len=0;
    String name = null,add,mail,phn,tot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);
        lv=findViewById(R.id.listview);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db= FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("Medicines");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String value= Objects.requireNonNull(ds.getValue()).toString();
                    medlist.add(value);
                    len++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        lv.setAdapter(arrayAdapter);
        dr=FirebaseDatabase.getInstance().getReference().child("pharmacy");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DataSnapshot dd=ds.child("Medicine Names");
                    int flag=0;
                    for(int i=0;i<len;i++)
                    {
                        if(Objects.equals(dd.child(medlist.get(i)).child("stockstatus").getValue(), "in"))
                        {
                            statuslist.add("in");
                            flag+=1;
                        }
                        else
                        {
                            statuslist.add("out");
                        }

                    }
                    if(flag==len) {
                        name = Objects.requireNonNull(ds.child("pharmacyname").getValue()).toString();
                        add = Objects.requireNonNull(ds.child("pharmacyaddress").getValue()).toString();
                        mail = Objects.requireNonNull(ds.child("mailid").getValue()).toString();
                        phn = Objects.requireNonNull(ds.child("pharmacynum").getValue()).toString();
                        tot = " " + name + "\n" + add + "\n" + mail + "\n" + phn + ".";
                        nameList.add(name);
                        maillist.add(mail);
                        arrayList.add(tot);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                if(arrayList.isEmpty())
                {
                    for(int i=0;i<len;i++)
                    {
                        if(statuslist.get(i).equals("out"))
                        {
                            final String m=medlist.get(i);
                            final DatabaseReference dbref=FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("Medicines");
                            dbref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds: dataSnapshot.getChildren())
                                    {
                                        String name= Objects.requireNonNull(ds.getValue()).toString();
                                        if(name.equals(m)){
                                            dbref.child(Objects.requireNonNull(ds.getKey())).setValue(null);

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            DatabaseReference dbref1=FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("PRES");
                            dbref1.child(m).setValue(null);
                            //dbref1.child(m).child("no_of_days").setValue(null);
                            String ans="Medicine "+m+" is not available all pharmacy";
                            arrayList.add(ans);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*databaseReference= FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(FirebaseAuth.getInstance().getUid()).child("Pharmacylist");
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        lv.setAdapter(arrayAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String value=ds.getValue().toString();
                        arrayList.add(value);
                        arrayAdapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s=arrayList.get(i);
                String m=maillist.get(i);
                String name=nameList.get(i);
                if(s.contains("is not available all pharmacy"))
                {
                    Intent ij=new Intent(getApplicationContext(),home.class);
                    startActivity(ij);
                }
                else{
                    final DatabaseReference dbref=FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                    dbref.child("Pharmacy").setValue(maillist.get(i));
                    //dbref.child("Medicines").setValue(null);//clear all medicines
                    Intent intent=new Intent(getApplicationContext(),details.class);
                    intent.putExtra("Pharmacy",s);
                    intent.putExtra("mail",m);
                    intent.putExtra("name",name);
                    startActivity(intent);
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear) {
            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(FirebaseAuth.getInstance().getUid());
            databaseReference2.setValue(null);
        }
        else if(id==R.id.logout)
        {
            SharedPreferenceConfig preferenceConfig=new SharedPreferenceConfig(getApplicationContext());
            preferenceConfig.writeLoginStatus(false);
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();

        }
        else if(id==R.id.history)
        {

        }
        else if(id==R.id.about)
        {

        }
        return true;
    }



}
