package com.e_prescription;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class addvoice extends AppCompatActivity {
TextView txt;
ImageButton img;
    private ConstraintLayout mLayout;
private TextToSpeech mytts;
private SpeechRecognizer mySpeechRecognizer;
private static final int REQUEST_CODE_SPEECH_INPUT=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvoice);
        mLayout = findViewById(R.id.linearLayout);
        txt=findViewById(R.id.text);
        img=findViewById(R.id.voice);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
                mLayout.addView(createNewTextView(txt.getText().toString()));
            }
        });

        TextView textView = new TextView(this);
        textView.setText("New text");
    }
private void processResult(String msg)
{
    msg=msg.toLowerCase();
    txt.setText(msg);
}

    private void speak(){
        Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        try{
            startActivityForResult(i,REQUEST_CODE_SPEECH_INPUT);
        }catch(Exception e){
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_LONG).show();

        }

    }
    private TextView createNewTextView(String text) {
       // final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
       final ConstraintLayout.LayoutParams lparams= new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText(text);
        return textView;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if (resultCode==RESULT_OK&&null!=data)
                {
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txt.setText(result.get(0));
                }
                break;
            }
        }
    }
}









