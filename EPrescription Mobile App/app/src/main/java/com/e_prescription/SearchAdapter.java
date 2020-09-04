package com.e_prescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> fullNameList;
    String mode;
   static String f;


    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView full_name, user_name;

        public SearchViewHolder(View itemView) {
            super(itemView);
            full_name = (TextView) itemView.findViewById(R.id.full_name);

        }
    }
    public SearchAdapter(Context context, ArrayList<String> fullNameList,String mode) {
        this.context = context;
        this.fullNameList = fullNameList;
        this.mode=mode;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_items, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, final int position) {
        holder.full_name.setText(fullNameList.get(position));
        holder.full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f= (String) holder.full_name.getText();
               // Toast.makeText(context, "Full Name Clicked "+f, Toast.LENGTH_SHORT).show();
                fullNameList.clear();
                final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("PRESCRIPTION").child(FirebaseAuth.getInstance().getUid()).child(mode);
                if(mode.equals("Symptoms"))
                {
                    final DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("Symptoms");
                    databaseReference1.child(f).child("caps").setValue(f);

                }

                databaseReference.child(f).setValue(f);
                //opendialog();
               // databaseReference.child("fn").setValue("yes");
                //databaseReference.child("an").setValue("yes");
                //databaseReference.child("ni8").setValue("yes");
                //databaseReference.child("nod").setValue("10")
                SharedPreferences sharedPreferences=context.getSharedPreferences("savedata",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                String text=sharedPreferences.getString("value","dont");
                    if(text.equals("dont")) {
                        editor.putString("value", "makeempty");
                        editor.apply();
                    }

                Toast.makeText(context,"Added",Toast.LENGTH_LONG).show();
            }
        });
    }
    /*public void opendialog(){
        ExampleDialog exampleDialog=new ExampleDialog();
        exampleDialog.show(AppCompatActivity.getSupportFragmentManager(),"Example dialog");

    }*/

    @Override
    public int getItemCount() {
        return fullNameList.size();
    }
}
