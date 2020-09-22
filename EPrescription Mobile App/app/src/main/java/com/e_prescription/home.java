package com.e_prescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class home extends AppCompatActivity {

    EditText ed;
    RecyclerView recyclerView;
    Button bt2;
    ListView medview;
    ImageButton voice;
    private static final int STORAGE_CODE = 1000;
    ArrayList<String> medicinelist, preslist,symlist;
    DatabaseReference databaseReference, databaseReference1, databaseReference12,databaseReference2;
    FirebaseUser firebaseUser, firebaseUser1;
    SearchAdapter searchAdapter;
    ExampleDialog exampleDialog;
    RadioGroup radioGroup;
    RadioButton radioButton;
    SharedPreferenceConfig preferenceConfig;

    public static String select="Symptoms";
    //PresAdapter presAdapter;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);
        medview = findViewById(R.id.medls);
        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(home.this, android.R.layout.simple_list_item_1, preslist);

        medview.setAdapter(myArrayAdapter);
        preferenceConfig=new SharedPreferenceConfig(getApplicationContext());


        select = radioButton.getText().toString();
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(FirebaseAuth.getInstance().getUid()).child(select);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                preslist.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String value = ds.getValue(String.class);

                    preslist.add(value);
                    myArrayAdapter.notifyDataSetChanged();
                }
                SharedPreferences sharedPreferences=getSharedPreferences("savedata", Context.MODE_PRIVATE);
                String text=sharedPreferences.getString("value","dont");
                if(text.equals("makeempty"))
                {
                    ed.setText("");
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("value","dont");
                    editor.apply();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        voice = findViewById(R.id.imageButton);
        ed = findViewById(R.id.search);
        bt2 = findViewById(R.id.check);
        recyclerView = findViewById(R.id.review);
        medview = findViewById(R.id.medls);
        radioGroup = findViewById(R.id.radioGroup);
       Toolbar toolbar= findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

       preferenceConfig=new SharedPreferenceConfig(getApplicationContext());

       databaseReference = FirebaseDatabase.getInstance().getReference();
       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


       //this is for search recycle
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        medicinelist = new ArrayList<>();
        //this for medicine added in prescription
        preslist = new ArrayList<>();
        symlist=new ArrayList<>();
        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(home.this, android.R.layout.simple_list_item_1, preslist);
        medview.setAdapter(myArrayAdapter);

    medview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(select.equals("Medicines"))
            {
                String s = preslist.get(i);
                opendialog(s);
            }
              }
    });

        //voice search
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
        ed.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    setAdapter(s.toString());
                }
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select.equals("Symptoms"))
                {
                    select="Medicines";
                    databaseReference1 = FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(FirebaseAuth.getInstance().getUid()).child(select);
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            preslist.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String value = ds.getValue(String.class);
                                preslist.add(value);
                                myArrayAdapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                final String[] ch = new String[1];
                databaseReference12 = FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(FirebaseAuth.getInstance().getUid()).child("PRES");
                databaseReference12.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int flag = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            for (int i = 0; i < preslist.size(); i++) {
                                ch[0] = preslist.get(i).toString();
                                if (Objects.equals(ds.getKey(), ch[0])) {
                                    flag++;
                                }
                            }
                        }
                        if (flag == preslist.size()) {
                            Intent intent = new Intent(getApplicationContext(), Pharmacy.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Add dosage details for all medicines ", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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
            databaseReference2 = FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(FirebaseAuth.getInstance().getUid());
            databaseReference2.setValue(null);
            ed.setText("");

        }
        else if(id==R.id.logout)
        {
            preferenceConfig.writeLoginStatus(false);
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();

        }

        else if(id==R.id.scan)
        {
            Intent i=new Intent(getApplicationContext(), QrScan.class);
            startActivity(i);

        }

        else if(id==R.id.history)
        {
            Intent i=new Intent(getApplicationContext(),history.class);
            startActivity(i);

        }
        else if(id==R.id.about)
        {

        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    final String s = result1.get(0);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        final DatabaseReference medicines = firebaseDatabase.getReference().child(select);
                        medicines.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int flag = 0;
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String u=s.toLowerCase();
                                    MEDICINES MM;
                                    String m1;

                                        MM = ds.getValue(MEDICINES.class);
                                        m1 = MM.getCaps().toString();

                                    if (u.equals(m1)) {
                                        assign(u);
                                        flag = 0;
                                        break;
                                    } else {
                                        flag = 1;
                                    }

                                }
                                if (flag == 1) {
                                    back();
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }


                break;
                }
            }
        }


    private void back() {
        Toast.makeText(getApplicationContext(), select+" not found", Toast.LENGTH_LONG).show();
    }

    private void assign(String str) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(FirebaseAuth.getInstance().getUid()).child(select);
        databaseReference.push().setValue(str);
    }

    private void speak() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        try {
            startActivityForResult(i, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }

    private void setAdapter(final String searchedstring) {
        medicinelist.clear();

        recyclerView.removeAllViews();

            databaseReference.child(select).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int counter = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String uid = snapshot.getKey();
                        String med = snapshot.child("caps").getValue(String.class);
                        if (med.contains(searchedstring)) {
                            medicinelist.add(med);
                            counter++;
                        }

                        if (counter == 3) {
                            break;
                        }
                    }
                    if(counter==0)
                    {
                        medicinelist.clear();;
                        medicinelist.add(searchedstring);

                    }


                    searchAdapter = new SearchAdapter(getApplicationContext(), medicinelist,select);
                    recyclerView.setAdapter(searchAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    recyclerView.removeAllViews();
                }
            });


    }

    public void opendialog(String s) {
        exampleDialog = ExampleDialog.newInstance(s);
        exampleDialog.show(getSupportFragmentManager(), "Example dialog");

    }

    public void dialogDone(boolean b, final String med, String nod1, String dose) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(FirebaseAuth.getInstance().getUid()).child("PRES").child(med);
        db.child("no_of_days").setValue(nod1);
        db.child("Dosage").setValue(dose);
        Toast.makeText(getApplicationContext(), "Name: " + med + "\nNo of Days: " + nod1 + "\nDosage: " + dose, Toast.LENGTH_LONG).show();
        exampleDialog.dismiss();
    }


}

