package com.example.rcr30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpdateComponent1 extends AppCompatActivity {

    ImageView ComponentImageUpdate;
    ImageView backToUpdate;
    CardView ImageChanger;
    CardView CompleteUpdate;
    EditText ComponentNameEditor;
    ImageView editButton;
    Spinner UpdateType;
    EditText ComponentAvailableEditor;
    EditText ComponentWorkingEditor;
    EditText Component_Non_Working_Editor;
    EditText ComponentSpecsEditor;
    ProgressDialog progressDialog;

    int image_request_code=7;
    Uri FilePathUri;
    StorageReference storageReference;
    FirebaseDatabase database;
    DatabaseReference reference;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_component1);

        /** Initializations **/
        ComponentImageUpdate=findViewById(R.id.ComponentImageUpdate);
        backToUpdate=findViewById(R.id.back_to_updateList);
        ImageChanger=findViewById(R.id.ChangeImage);
        editButton=findViewById(R.id.EditButton);
        CompleteUpdate=findViewById(R.id.Update_Data);
        UpdateType=findViewById(R.id.type_update);
        ComponentNameEditor=findViewById(R.id.ComponentNameUpdate);
        ComponentAvailableEditor=findViewById(R.id.ComponentAvailableUpdate);
        ComponentWorkingEditor=findViewById(R.id.ComponentWorkingUpdate);
        Component_Non_Working_Editor=findViewById(R.id.ComponentNon_WorkingUpdate);
        ComponentSpecsEditor=findViewById(R.id.ComponentSpecsUpdate);
        progressDialog= new ProgressDialog(UpdateComponent1.this);


        /** Firebase Initialization **/
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Components");
        storageReference= FirebaseStorage.getInstance().getReference("Images");

        /** Adding an array list to spinner asking to select type of component **/
        List<String> componentType= new ArrayList<>();
        componentType.add(0,"Select Type");
        componentType.add("Electronics");
        componentType.add("Mechanical");

        UpdateType.setEnabled(false);
        ArrayAdapter<String> UpdateTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, componentType);
        UpdateTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UpdateType.setAdapter(UpdateTypeAdapter);

        /** Allowing user to change the 'type of component' used **/
        UpdateType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        Glide.with(ComponentImageUpdate).load(getIntent().getStringExtra("Component Image")).placeholder(R.drawable.placeholder).dontAnimate().into(ComponentImageUpdate);

        /** Initialization of editText and textViews with strings received from previous activity **/
        String name= getIntent().getStringExtra("Component Name");
        ComponentNameEditor.setText(name);
        ComponentNameEditor.setEnabled(false);

        int Available_Components=getIntent().getIntExtra("Available",0);
        ComponentAvailableEditor.setText(Integer.toString(Available_Components));
        ComponentAvailableEditor.setEnabled(false);

        int Working_Components=getIntent().getIntExtra("Working",0);
        ComponentWorkingEditor.setText(Integer.toString(Working_Components));
        ComponentWorkingEditor.setEnabled(false);

        int Non_Working_Components=getIntent().getIntExtra("Not Working",0);
        Component_Non_Working_Editor.setText(Integer.toString(Non_Working_Components));
        Component_Non_Working_Editor.setEnabled(false);

        String Specifications=getIntent().getStringExtra("Specifications");
        ComponentSpecsEditor.setText(Specifications);
        ComponentSpecsEditor.setEnabled(false);

        /** Finishing the current activity to go to previous one **/
        backToUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        /** Updating the image of the component **/
        ImageChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),image_request_code);
            }
        });

        /** Enabling all the editText and spinners to update the wrongly entered data **/
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentNameEditor.setEnabled(true);
                ComponentAvailableEditor.setEnabled(true);
                ComponentWorkingEditor.setEnabled(true);
                Component_Non_Working_Editor.setEnabled(true);
                ComponentSpecsEditor.setEnabled(true);
                UpdateType.setEnabled(true);

            }
        });

        /** Updates the data completely in the firebase database **/
        CompleteUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateData();
            }
        });



    }

    /** Method to get FileExtension **/
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    /** Method to update data in firebase database **/
    private void UpdateData() {
        String type_selected = UpdateType.getSelectedItem().toString().trim();
        String ComponentName = ComponentNameEditor.getText().toString().trim();
        String avail = ComponentAvailableEditor.getText().toString().trim();
        String work = ComponentWorkingEditor.getText().toString().trim();
        String non_work = Component_Non_Working_Editor.getText().toString().trim();
        int Available = Integer.parseInt(avail);
        int Working = Integer.parseInt(work);
        int Non_working = Integer.parseInt(non_work);
        String Specifications = ComponentSpecsEditor.getText().toString().trim();

        if (FilePathUri != null || TextUtils.isEmpty(ComponentName) || TextUtils.isEmpty(Specifications) ||
                TextUtils.isEmpty(avail) || TextUtils.isEmpty(work) || TextUtils.isEmpty(non_work) ||
                TextUtils.isEmpty(type_selected)) {

            progressDialog.setTitle("Data is Updating...");
            progressDialog.show();
            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateComponent1.this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Component component = new Component(ComponentName,url, Available, Working, Non_working, Specifications);

                                    if (type_selected.equals("Electronics"))
                                        reference.child("Electronics").child(ComponentName).setValue(component);
                                    else
                                        reference.child("Mechanical").child(ComponentName).setValue(component);

                                }
                            });
                        }
                    });


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == image_request_code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                Glide.with(ComponentImageUpdate).load(FilePathUri).placeholder(R.drawable.placeholder).dontAnimate().into(ComponentImageUpdate);

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}