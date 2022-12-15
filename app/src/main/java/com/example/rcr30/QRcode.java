package com.example.rcr30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QRcode extends AppCompatActivity {

    String UID;
    ImageView qrImage;
    ProgressBar qrLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        UID=getIntent().getStringExtra("User ID");
        qrImage=findViewById(R.id.qrCode);
        qrLoader=findViewById(R.id.qrcodeProgress);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MultiFormatWriter multiFormatWriter= new MultiFormatWriter();
                try {
                    BitMatrix matrix=multiFormatWriter.encode(UID, BarcodeFormat.QR_CODE,350,350);

                    BarcodeEncoder encoder= new BarcodeEncoder();
                    Bitmap bitmap= encoder.createBitmap(matrix);

                    qrImage.setImageBitmap(bitmap);
                    qrLoader.setVisibility(View.INVISIBLE);

                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        }, 5000);


    }
}