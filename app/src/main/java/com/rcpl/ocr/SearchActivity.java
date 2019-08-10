package com.rcpl.ocr;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {
    EditText rno;
    SQLiteDatabase sd;
    Cursor c;
    Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rno=findViewById(R.id.colour);
        search=findViewById(R.id.search);
        getSupportActionBar().setTitle("Search Vehicle");
        FloatingActionButton fab = findViewById(R.id.fab);
        sd=openOrCreateDatabase("vldsDB",MODE_PRIVATE,null);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void search(View view) {
        final String s=rno.getText().toString();
        c=sd.rawQuery("Select * from vehicle where rno='"+s+"'",null);

        if(c.moveToNext())
        {
            Toast t=Toast.makeText(this, "BLACKLISTED VEHICLE", Toast.LENGTH_LONG);
            View toastView=t.getView();
            t.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 300);
            toastView.setBackgroundColor(Color.RED);
            t.show();
                    Snackbar.make(view, "OWNER: "+c.getString(1), Snackbar.LENGTH_INDEFINITE)
                    .setAction("VIEW DETAILS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getApplicationContext(),DetailsActivity.class);
                            intent.putExtra("data",s);
                            startActivity(intent);
                        }
                    }).show();
        }
        else
        {
            Toast.makeText(this, "VEHICLE NOT REGISTERED", Toast.LENGTH_SHORT).show();
            Snackbar.make(view, "REGISTER NEW VEHICLE", Snackbar.LENGTH_INDEFINITE)
                    .setAction("CLICK HERE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getApplicationContext(),AddVehicle.class);
                            startActivity(intent);
                        }
                    }).show();
        }
    }
}
