package com.e_prescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;

public class details extends AppCompatActivity {
TextView p,pt,dc,mm,sym;
Button send,scanqr,pdf;
urlist filelist;
private SharedPreferenceConfig preferenceConfig;
    private IntentIntegrator qrScan;
    DatabaseReference ref;
    String pname,ppid,pa,padd,pphn,pmail,n4,dname,nn,pharmdetails,presid;
    String mfilepath,mailid,currentdate,date;
    String downloadUrl;
    ArrayList<String> medlist,symlist;
    private StorageReference mStorageRef;
    UploadTask uploadTask;
    private static final int STORAGE_CODE =1000;
    DatabaseReference ref2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        p = findViewById(R.id.phar);
        pt = findViewById(R.id.patient);
        mm = findViewById(R.id.med);
        sym = findViewById(R.id.sym);
        dc = findViewById(R.id.doctor);
        qrScan = new IntentIntegrator(this);
        send = findViewById(R.id.mail);
        pdf = findViewById(R.id.save);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Calendar calendar = Calendar.getInstance();
        currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date = DateFormat.getDateInstance().format(calendar.getTime());

        TextView date = findViewById(R.id.date);
        date.setText(currentdate);
        SharedPreferences sharedPreferences = getSharedPreferences("patientdetails", Context.MODE_PRIVATE);
        String job = sharedPreferences.getString("job", "NotDone");
        if (job.equals("NotDone")) {
            // Create the object of
            // AlertDialog Builder class
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(details.this);

            // Set the message show for the Alert time
            builder.setMessage("Please Scan Qr Code to Send Mail.");

            // Set Alert Title
            builder.setTitle("Alert !");

            // Set Cancelable false
            // for when the user clicks on the outside
            // the Dialog Box then it will remain show
            builder.setCancelable(false);

            // Set the positive button with yes name
            // OnClickListener method is use of
            // DialogInterface interface.

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog,
                                    int which) {

                    dialog.cancel();

                }
            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();

            // Show the Alert Dialog box
            alertDialog.show();
        }
        String text = sharedPreferences.getString("details", "dont");
        nn = sharedPreferences.getString("pnameandid", "dont");
        pt.setText(text);
        preferenceConfig=new SharedPreferenceConfig(getApplicationContext());

        medlist=new ArrayList<>();
        symlist=new ArrayList<>();
        p.setText("Pharmacy Details: \n"+getIntent().getStringExtra("Pharmacy"));
        pharmdetails=p.getText().toString();

        mailid=(getIntent().getStringExtra("mail"));
        dname=(getIntent().getStringExtra("name"));
        final DatabaseReference ref1=FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("PRES");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String a=mm.getText().toString()+"      "+"Days "+"      "+"Timings";

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String n= ds.getKey();
                    assert n != null;
                    String dos= Objects.requireNonNull(ds.child("Dosage").getValue()).toString();
                    String nod= Objects.requireNonNull(ds.child("no_of_days").getValue()).toString();
                    String f="\n"+n+"      "+nod+"        "+dos+" ";
                    a=a+"\n"+f;
                    mm.setText(a);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        ref2=FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("Symptoms");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String symp="Symptoms:";
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String n= ds.getKey().toString();
                    String dos= dataSnapshot.child(n).getValue().toString();
                    symp=symp+' '+dos+',';
                    }
                sym.setText(symp);



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        final DatabaseReference register = firebaseDatabase.getReference().child("Doctors").child(FirebaseAuth.getInstance().getUid());
        register.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String n= Objects.requireNonNull(dataSnapshot.child("add").getValue()).toString();
                String n1= Objects.requireNonNull(dataSnapshot.child("deg").getValue()).toString();
                String n2= Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                String n3= Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                String n5= Objects.requireNonNull(dataSnapshot.child("phn").getValue()).toString();
                n4="Prescribed By,\nDr."+n3+" "+n1+"\n"+n+",\n"+n5+",\n"+n2+".";
                dc.setText(n4);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    }
                    else{
                        savepdf();
                    }
                }else{
                    savepdf();
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }
    private void task()
    {
        Random rand=new Random();
        presid=String.valueOf(Math.abs(rand.nextInt()));
        task1();

    }
    private void task1()
    {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Prescription ID");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                outer: for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String value = ds.getKey();
                    if(value.equals(presid))
                    {
                        task();
                        break outer;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void savepdf() {
        task();
        ref = FirebaseDatabase.getInstance().getReference().child("Prescription ID");
        ref.child(presid).setValue(nn);

        final Document mDoc=new Document();
        String mfilename=new SimpleDateFormat("MMdd_HHmm", Locale.getDefault()).format(System.currentTimeMillis());
        final String p=nn+mfilename;//nn=pname+pid
         mfilepath= Environment.getExternalStorageDirectory()+"/"+p+".pdf";
        try {
            PdfWriter.getInstance(mDoc,new FileOutputStream(mfilepath));
            mDoc.addCreationDate();
            mDoc.setMargins(10,10,10,10);
            mDoc.setMarginMirroringTopBottom(true);
            mDoc.addTitle("PRESCRIPTION ");
            mDoc.open();
            mDoc.addAuthor(n4);
            mDoc.left(100);
            mDoc.add(new Paragraph("\n E-Prescription"));
            mDoc.add(new Paragraph("\nPrescribed on "+currentdate+"\n"));
            mDoc.add(new Paragraph("\nE-Prescription Number:"+presid+"\n"));

            String patient=pt.getText().toString();
            mDoc.add(new Paragraph("\nPatient Details:\n"));
            mDoc.add(new Paragraph("\n"+patient));
            final String medc=mm.getText().toString();
            mDoc.add(new Paragraph("\n\n"));
            mDoc.add(new Paragraph(medc));
            final String symp=sym.getText().toString();

            mDoc.add(new Paragraph("\n\n"));
            mDoc.add(new Paragraph(symp));

            mDoc.add(new Paragraph("\n\n"));
            mDoc.add(new Paragraph(n4));
            mDoc.add(new Paragraph("\n\n"));
            mDoc.add(new Paragraph(pharmdetails));

            mDoc.close();
            final Uri file = Uri.fromFile(new File(mfilepath));
            final StorageReference riversRef = mStorageRef.child("Prescription").child(dname).child(p);
            riversRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url=String.valueOf(uri);
                            filelist=new urlist(p,url);
                            ref=FirebaseDatabase.getInstance().getReference().child("Doctors").child(FirebaseAuth.getInstance().getUid()).child("Preslist");
                            ref.child(nn).push().setValue(filelist);
                            downloadUrl=url;
                        }
                    });
                    Toast.makeText(getBaseContext(), "pdf stored.\nYou can sent mail now", Toast.LENGTH_SHORT).show();
                }
            });
            final StorageReference riversRef1 = mStorageRef.child("Prescription").child(nn);
            riversRef1.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    riversRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url=String.valueOf(uri);
                            filelist=new urlist(p,url);
                            ref=FirebaseDatabase.getInstance().getReference().child("Patients").child(nn).child("Preslist");
                            ref.push().setValue(filelist);
                            ref=FirebaseDatabase.getInstance().getReference().child("Patients").child(nn).child("preslist").push();
                            ref.child("symptoms").setValue(symp);
                            ref.child("medicines").setValue(medc);
                            ref.child("doctor").setValue(n4);
                            ref.child("date").setValue(date);



                        }
                    });
                    Toast.makeText(getBaseContext(), "pdf stored.\nYou can sent mail now", Toast.LENGTH_SHORT).show();
                }
            });

            SharedPreferences sharedPreferences=getSharedPreferences("patientdetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("details","");
            editor.putString("pnameandid","");
            editor.putString("job","NotDone");
            editor.apply();

        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
       // sendMail();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case STORAGE_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        savepdf();
                }
                else {
                    Toast.makeText(this,"Permission Denied...!",Toast.LENGTH_LONG).show();
                }
        }
    }
    /*private void clearpresdata()
    {
        //DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        //reference.setValue(null);//for medicine clear
        //ref2.setValue(null);//for symptoms clear
        //reference.setValue(null);

    }*/
    private void sendMail()
    {
        String mail=mailid;
        String msg=downloadUrl;
        String sub=pt.getText().toString().trim();
        JavaMailAPI javaMailAPI=new JavaMailAPI(this,mail,sub,msg);
        javaMailAPI.execute();
    }
    /*private void sendMail()
    {
        String mail=mailid;
        String msg=downloadUrl;
        String sub=pt.getText().toString().trim();
        JavaMailAPI javaMailAPI=new JavaMailAPI(this,mail,sub,msg);
        javaMailAPI.execute();
        //clearpresdata();
        }*/
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
            databaseReference2.child("Medicines").setValue(null);
            databaseReference2.child("Symptoms").setValue(null);
            databaseReference2.child("PRES").setValue(null);

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
            Intent i=new Intent(getApplicationContext(), history.class);
            startActivity(i);

        }
        else if(id==R.id.about)
        {

        }
        else if(id==R.id.scan)
        {
            Intent i=new Intent(getApplicationContext(), QrScan.class);
            startActivity(i);

        }

        return true;
    }

}
