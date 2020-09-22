package com.e_prescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    EditText n1,d1,a1,p1,e1,pass1;
    Button b1,b2;
    public boolean isValid(String s) {
        return (!s.trim().isEmpty());
    }
    public boolean passIsValid(String s) {
        return (s.length() >= 5 && isValid(s));
    }
    public int C2I(String st) {
        return Integer.parseInt(st);
    }
    private FirebaseAuth mAuth;
    String _id, _name, _phn, _mail, _pass,_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        n1=findViewById(R.id.name);
        d1=findViewById(R.id.deg);
        a1=findViewById(R.id.address);
        p1=findViewById(R.id.phn);
        e1=findViewById(R.id.mail);
        pass1=findViewById(R.id.pass);
        b1=findViewById(R.id.reg);
        b2=findViewById(R.id.log);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id = d1.getText().toString().trim();
                _name = n1.getText().toString().trim();
                _phn = p1.getText().toString().trim();
                _mail = e1.getText().toString().trim();
                _pass = pass1.getText().toString().trim();
                _add=a1.getText().toString().trim();
                if (TextUtils.isEmpty(_id) &&
                        TextUtils.isEmpty(_name) && TextUtils.isEmpty(_phn) && TextUtils.isEmpty(_mail) && TextUtils.isEmpty(_pass)&& TextUtils.isEmpty(_add)) {
                    d1.setError("Required");
                    n1.setError("Required");
                    p1.setError("Required");
                    e1.setError("Required");
                    a1.setError("Required");
                    pass1.setError("Required");
                } else if (TextUtils.isEmpty(_id) ){
                    d1.setError("Enter ID");
                } else if (TextUtils.isEmpty(_name)) {
                    n1.setError("Required");
                } else if (!emailValidator(_mail) ){
                    e1.setError("Please Enter Valid Email Address");
                } else if (TextUtils.isEmpty(_pass)) {
                    pass1.setError("Required");
                }else if (TextUtils.isEmpty(_phn)) {
                    p1.setError("Required");
                } else if (!phValidator(_phn)) {
                    p1.setError("Please Enter a Valid Mobile Number");
                } else if (!passIsValid(_pass)) {
                    pass1.setError("password is invalid");
                } else {
                    mAuth.createUserWithEmailAndPassword(_mail,_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            userdata();
                                            Toast.makeText(getApplicationContext(), "Registered successfully.Please check your email for verification!!", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(i);
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();


                                        }


                                    }
                                });
                               } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
            public void userdata(){

                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference register = firebaseDatabase.getReference().child("Doctors").child(mAuth.getUid());
                doctor reg = new doctor(_name,_id,_add,_phn,_mail,_pass);
                register.setValue(reg);
            }
            public boolean emailValidator(String email)
            {
                return(!TextUtils.isEmpty(email)&& Patterns.EMAIL_ADDRESS.matcher(email).matches());
            }
            private boolean phValidator(String phone){
                Pattern pattern;
                Matcher matcher;
                final String PHONE_PATTERN="^[0-9]{3}[0-9]{7}$";
                pattern=Pattern.compile(PHONE_PATTERN);
                matcher=pattern.matcher(phone);
                return matcher.matches();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
    }
}
