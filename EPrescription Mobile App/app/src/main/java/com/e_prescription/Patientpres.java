package com.e_prescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Patientpres extends AppCompatActivity {
    ListView listView2;
    DatabaseReference ref;
    StorageReference store,ref1;
    String  pname,pid;
    List<urlist> list;
    urlist file;
    ArrayAdapter<urlist> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientpres);
        SharedPreferences sharedPreferences = getSharedPreferences("patientdetails", Context.MODE_PRIVATE);
         pname = sharedPreferences.getString("pnameandid", "dont");
         listView2=findViewById(R.id.listview1);
        // listView2 = (ListView) findViewById(R.id.listView1);
        list = new ArrayList<>();
        file= new urlist();
        ref=FirebaseDatabase.getInstance().getReference().child("Patients").child(pname).child("Preslist");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    file= ds.getValue(urlist.class);
                    list.add(file);
                }
                String[] uploads = new String[list.size()];
                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = list.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                listView2.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               urlist urlclass=list.get(i);

                download(urlclass.geturl(),urlclass.getName());
            }
        });
    }
    public  void  download(String url,String fname){
        //store= FirebaseStorage.getInstance().getReference();
        //ref1=store.child("VIDEOS");
        downloadfiles(getApplicationContext(),fname,".pdf", Environment.DIRECTORY_DOWNLOADS,url);

    }
    public void downloadfiles(Context context, String videoname, String videoextension, String destination, String videourl) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(videourl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destination, videoname + videoextension);
        assert downloadManager != null;
        downloadManager.enqueue(request);

    }
}
