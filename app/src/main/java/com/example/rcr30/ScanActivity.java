package com.example.rcr30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    Dialog taskDialog;
    BarcodeDetector barcodeDetector;
    TextView ScannedText;
    LinearLayout linear;
    EditText ReturnApprovalDate;
    TextView descriptive;
    TextView buttonText;
    CardView OrderInfoOpen;
    MaterialCardView ReturnComponentOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        surfaceView=findViewById(R.id.scanner);
        ScannedText=findViewById(R.id.scannedText);

        taskDialog= new Dialog(ScanActivity.this);

        taskDialog.setContentView(R.layout.task_dialog);
        taskDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        taskDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        OrderInfoOpen=taskDialog.findViewById(R.id.ComponentIssue);
        ReturnComponentOpen=taskDialog.findViewById(R.id.ComponentReceive);
        linear=taskDialog.findViewById(R.id.linear2);
        descriptive=taskDialog.findViewById(R.id.description);
        buttonText=taskDialog.findViewById(R.id.componentIssueText);
        ReturnApprovalDate=taskDialog.findViewById(R.id.approvalDateForReturn);


        barcodeDetector=new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource=new CameraSource.Builder(getApplicationContext(),barcodeDetector).setRequestedPreviewSize(640,480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                try {
                    cameraSource.start(surfaceHolder);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode= detections.getDetectedItems();
                if(qrCode.size()!=0){
                    ScannedText.post(new Runnable() {
                        @Override
                        public void run() {
                            ScannedText.setVisibility(View.VISIBLE);
                            ScannedText.setText(qrCode.valueAt(0).displayValue);

                            taskDialog.show();

                            OrderInfoOpen.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent= new Intent(getApplicationContext(),OrderInfo.class);
                                    intent.putExtra("UserID",ScannedText.getText().toString());
                                    startActivity(intent);
                                }
                            });

                            ReturnComponentOpen.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onClick(View view) {
                                    descriptive.setVisibility(View.VISIBLE);
                                    linear.setVisibility(View.VISIBLE);
                                    buttonText.setText("GO");

                                    OrderInfoOpen.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                               Intent intent= new Intent(getApplicationContext(),Return.class);
                                               intent.putExtra("User ID",ScannedText.getText().toString());
                                               intent.putExtra("Approval Date",ReturnApprovalDate.getText().toString());
                                               startActivity(intent);
                                        }
                                    });
                                }
                            });
                        }
                    });

                }
            }
        });
    }
}