package com.rcpl.ocr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class IndexActivity extends AppCompatActivity {
    CardView cv1,cv2,cv3,cv4;
    String s=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        cv1=findViewById(R.id.cv1);
        cv2=findViewById(R.id.cv2);
        cv3=findViewById(R.id.cv3);
        cv4=findViewById(R.id.cv4);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu");
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextact(View view) throws ClassNotFoundException {
        s="com.rcpl.ocr.";
        switch(view.getId())
        {
            case R.id.cv1:
                s=s+"SearchActivity";
                break;
            case R.id.cv2:
                s=s+"AddVehicle";
                break;
            case R.id.cv3:
                s=s+"BlackListActivity";
                break;
            case R.id.cv4:
                s=s+"IndexActivity";
                cv4.setTooltipText("Upcoming Feature");
                break;
        }
        Class c=Class.forName(s);
        Intent i=new Intent(this,c);
        startActivity(i);
    }
}
