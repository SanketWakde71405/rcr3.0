package com.example.rcr30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddComponent extends AppCompatActivity {

    ImageView backToAdmin;
    EditText etComponentName,etAvailable,etWorking,etNon_Working,etSpecifications;
    ImageView UploadImage;
    CardView Browse_Image,Upload_Data;
    Spinner Type;

    Uri FilePathUri;
    StorageReference storageReference;
    FirebaseDatabase database;
    DatabaseReference reference;
    int image_request_code=7;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_component);

        /** Initializations **/
        backToAdmin=findViewById(R.id.back_to_admin);
        etComponentName=findViewById(R.id.EtComponentName);
        etAvailable=findViewById(R.id.EtAvailable);
        etWorking=findViewById(R.id.EtWorking);
        etNon_Working=findViewById(R.id.EtNon_Working);
        etSpecifications=findViewById(R.id.EtSpecifications);
        UploadImage=findViewById(R.id.upload_image);
        Browse_Image=findViewById(R.id.browse_image);
        Upload_Data=findViewById(R.id.upload);
        Type=findViewById(R.id.type);
        progressDialog= new ProgressDialog(AddComponent.this);

        /** Firebase initializations **/
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Components");
        storageReference= FirebaseStorage.getInstance().getReference("Images");

        /** Adding arraylist to the spinner to select the type of the component **/
        List<String>  compType= new ArrayList<>();
        compType.add(0,"Select Type");
        compType.add("Electronics");
        compType.add("Mechanical");

        /** Setting up adapter to the spinner **/
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, compType);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Type.setAdapter(typeAdapter);

        /** Showing user the type he has selected **/
        Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Type")) {
                    //Umaan is great

                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "Selected:" + item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Spinner Type = findViewById(R.id.spinner3);
                Type.setSelection(0);
            }
        });

        /** Finishes the current activity to go back to the previous one **/
        backToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** Opens the gallery or file manager of the phone to select the image to upload **/
        Browse_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),image_request_code);
            }
        });

        /** Uploads the data to the firebase database **/
        Upload_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == image_request_code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                UploadImage.setImageBitmap(bitmap);
                UploadImage.setVisibility(View.VISIBLE);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    /** Uploads image as well as other data to the firebase database and storage **/
    public void uploadImage() {

        String type_selected= Type.getSelectedItem().toString().trim();
        String ComponentName= etComponentName.getText().toString().trim();
        String avail=etAvailable.getText().toString().trim();
        String work=etWorking.getText().toString().trim();
        String non_work=etNon_Working.getText().toString().trim();
        int Available= Integer.parseInt(avail);
        int Working=Integer.parseInt(work);
        int Non_working=Integer.parseInt(non_work);
        String Specifications= etSpecifications.getText().toString().trim();

        if (FilePathUri != null || TextUtils.isEmpty(ComponentName) || TextUtils.isEmpty(Specifications)||
        TextUtils.isEmpty(avail)|| TextUtils.isEmpty(work)||TextUtils.isEmpty(non_work)||
        TextUtils.isEmpty(type_selected)) {

            progressDialog.setTitle("Data is Uploading...");
            progressDialog.show();
            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddComponent.this, "Data Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Component component= new Component(ComponentName,url,Available,Working,Non_working,Specifications);

                                    if (type_selected.equals("Electronics"))
                                        reference.child("Electronics").child(ComponentName).setValue(component);
                                    else
                                        reference.child("Mechanical").child(ComponentName).setValue(component);

                                }
                            });
                                }
                            });


        }
        else {

            Toast.makeText(AddComponent.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }


}