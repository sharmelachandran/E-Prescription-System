package com.e_prescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button log,patient;
    TextView forgot,register;
    EditText id,pass;
    private ProgressDialog progressDialog;
    doctor doc;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database1;
    private DatabaseReference ref1;

    private  SharedPreferenceConfig preferenceConfig;
    public boolean isValid(String s) {
        return (!s.trim().isEmpty());
    }
    public boolean passIsValid(String s) {
        return (s.length() >= 5 && isValid(s));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        preferenceConfig=new SharedPreferenceConfig(getApplicationContext());
        if(preferenceConfig.readLoginStatus())
        {
            Intent i = new Intent(getApplicationContext(), home.class);
            startActivity(i);
            finish();

        }
        id = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        log=(Button)findViewById(R.id.login);
        register=findViewById(R.id.reg);
        forgot=findViewById(R.id.forgot);
        patient=findViewById(R.id.patient);
        progressDialog = new ProgressDialog(this);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(id.getText().toString().trim()) && TextUtils.isEmpty(pass.getText().toString().trim())) {
                    id.setError("Required");
                    pass.setError("Required");
                } else if (TextUtils.isEmpty(id.getText().toString().trim())) {
                    id.setError("Enter valid mail id");
                } else if (TextUtils.isEmpty(pass.getText().toString().trim())) {
                    pass.setError("Required");
                } else if (!passIsValid(pass.getText().toString().trim())) {
                    pass.setError("password is invalid");
                } else {
                    progressDialog.setMessage("Wait for a while!!!");
                    progressDialog.show();
                    String mail = id.getText().toString().trim();
                    String passw = pass.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(mail, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if(mAuth.getCurrentUser().isEmailVerified()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();
                                    DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(FirebaseAuth.getInstance().getUid());
                                    r.setValue(null);
                                    preferenceConfig.writeLoginStatus(true);
                                    Intent i = new Intent(getApplicationContext(), home.class);
                                    startActivity(i);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Please verify your email address", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Login failed. Invalid credentials", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                            }
                        }
                    });

                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),register.class);
                startActivity(i);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),forg.class);
                startActivity(i);
            }
        });
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),patient.class);
                startActivity(i);
            }
        });

    }
}
