package com.rcpl.ocr;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddVehicle extends AppCompatActivity implements View.OnClickListener {
    EditText oname,rno,model,brand,colour;
    RadioButton twowh,fourwh;
    SQLiteDatabase sd;
    RadioGroup rg;
    Button add;
    CheckBox blacklisted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Add Vehicle");
        sd=openOrCreateDatabase("vldsDB",MODE_PRIVATE,null);
        oname=findViewById(R.id.id);
        rno=findViewById(R.id.colour);
        model=findViewById(R.id.name);
        brand=findViewById(R.id.model);
        colour=findViewById(R.id.brand);
        twowh=findViewById(R.id.twowh);
        fourwh=findViewById(R.id.fourwh);
        rg=findViewById(R.id.rg);
        add=findViewById(R.id.add);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        blacklisted=findViewById(R.id.bl);
        add.setOnClickListener(this);
        sd.execSQL("create table if not exists vehicle(rno varchar primary key," +
                "name varchar not null,model varchar not null,brand varchar not null,colour varchar not null,vclass int not null,blacklisted int DEFAULT 0)");
    }

    @Override
    public void onClick(View view) {
        String name = oname.getText().toString();
        String vno = rno.getText().toString();
        String vmodel = model.getText().toString();
        String vbrand = brand.getText().toString();
        String vcolour = colour.getText().toString();
        int vclass;
        int bl;
        if (fourwh.isSelected() == true) {
            vclass = 4;
        } else {
            vclass = 2;
        }
        if (blacklisted.isChecked()) {
            bl = 1;
        } else {
            bl = 0;
        }
        try{
            String sql = "insert into vehicle values('" + vno + "','" + name + "','" + vmodel + "','" + vbrand + "','" + colour +
                    "'," + vclass + "," + bl + ")";
            sd.execSQL(sql);
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
