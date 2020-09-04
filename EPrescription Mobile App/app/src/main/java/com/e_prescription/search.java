package com.e_prescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class search extends AppCompatActivity {
    EditText search_edit_text;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<String> medicinelist;
    SearchAdapter searchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search_edit_text=findViewById(R.id.search_edit_text);
        recyclerView=findViewById(R.id.recyclerview);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        medicinelist=new ArrayList<String>();

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if(!s.toString().isEmpty()){
                        setAdapter(s.toString());
                    }
            }
        });


    }
    private void setAdapter(final String searchedstring)
    {
        medicinelist.clear();
        recyclerView.removeAllViews();
        databaseReference.child("Medicines").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter=0;
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    String uid=snapshot.getKey();
                    String med=snapshot.child("caps").getValue(String.class);
                    if(med.contains(searchedstring)){
                        medicinelist.add(med);
                        search_edit_text.setText(searchedstring);
                        counter++;
                    }
                    if(counter==3)
                    {
                        break;
                    }
                }
                searchAdapter = new SearchAdapter(getApplicationContext(),medicinelist,"helo");
                recyclerView.setAdapter(searchAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
