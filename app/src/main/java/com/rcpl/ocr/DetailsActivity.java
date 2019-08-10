package com.rcpl.ocr;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    TextView name,id,model,brand,colour,bl,vclass;
    SQLiteDatabase sd;
    Cursor c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        name=findViewById(R.id.name);
        id=findViewById(R.id.id);
        model=findViewById(R.id.model);
        brand=findViewById(R.id.brand);
        colour=findViewById(R.id.colour);
        bl=findViewById(R.id.blacklisted);
        vclass=findViewById(R.id.vclass);
        Bundle b=getIntent().getExtras();
        id.setText(b.getString("data"));
        String sql="select * from vehicle where rno='"+b.getString("data")+"')";
//        String sql="select * from vehicle where id='"+b.getString("data")+"')";
        c=sd.rawQuery("select * from vehicle where rno='CG00B5104'",null);
        while(c.moveToNext()) {
            name.setText(c.getString(1));
            model.setText(c.getString(2));
            brand.setText(c.getString(3));
            colour.setText(c.getString(4));
            vclass.setText(c.getString(5) + " wheeler");
            if (c.getInt(6) == 1) {
                bl.setText("Blacklisted : YES");
                getSupportActionBar().setTitle(id + " : BLACKLISTED");
            } else {
                bl.setText("" + id);
            }
        }
    }
}
