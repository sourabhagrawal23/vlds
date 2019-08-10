package com.rcpl.ocr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    CameraSource  mCameraSource;
    final int requestPermissionID=2000;
    SQLiteDatabase sd;
    TextView mTextView;
    Cursor c;
    SurfaceView mCameraView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView=findViewById(R.id.text_view);
        mCameraView=findViewById(R.id.surfaceView);
        startCameraSource();
        sd=openOrCreateDatabase("vldsDB",MODE_PRIVATE,null);
    }
    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0 ){

                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i=0;i<items.size();i++){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                mTextView.setText(stringBuilder.toString());
                                String s=stringBuilder.toString().trim();
                                s=s.replaceAll("\\s", "");
                                s=s.replaceAll("-", "");
                                c=sd.rawQuery("Select * from vehicle where rno='"+s+"'",null);
                                if(c.moveToNext())
                                {
                                    Intent intent=new Intent(getApplicationContext(),DetailsActivity.class);
                                    intent.putExtra("data",s);
                                    startActivity(intent);
                                    if(c.getInt(6)==1)
                                    {
                                        Toast t=Toast.makeText( MainActivity.this,"BLACKLISTED VEHICLE", Toast.LENGTH_LONG);
                                        View toastView=t.getView();
                                        t.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 300);
                                        toastView.setBackgroundColor(Color.RED);
                                        t.show();
//
//                                          Snackbar.make(mCameraView, "OWNER: "+c.getString(1), Snackbar.LENGTH_INDEFINITE)
//                                                .setAction("VIEW DETAILS", new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View view) {
//                                                        Intent intent=new Intent(getApplicationContext(),DetailsActivity.class);
//                                                        intent.putExtra("data",s);
//                                                        startActivity(intent);
//                                                    }
//                                                }).show();
                                    }
                                }
                                else
                                {
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
