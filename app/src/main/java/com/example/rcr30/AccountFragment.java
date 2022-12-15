package com.example.rcr30;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Objects;


public class AccountFragment extends Fragment {

   CircularImageView ProfilePic;
   EditText ProfileName;
   ImageView NameEditor;
   EditText ProfileMail;
   ImageView MailEditor;
   TextView ProfileBranch;
   TextView ProfileDomain;
   EditText ProfileYear;
   ImageView YearEditor;
   EditText EtNew_password;
   MaterialCardView ProfilePicChanger,PasswordChanger;
   CardView PasswordResetter;
   MaterialCardView ExitButton;
   ProgressBar progressBar;
   ProgressDialog progressDialog;
   CircularImageView Robot1,Robot2,Robot3,Camera,Gallery;
   Dialog dialog;
   Dialog passwordDialog;
   View view;

   ActivityResultLauncher<String> mGetContent;
   ActivityResultLauncher<Intent> cGetContent;
   DatabaseReference reference;
   StorageReference storageReference;
   FirebaseAuth mAuth;
   FirebaseUser firebaseUser;
   User user;
   Uri resultUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        ProfileName = view.findViewById(R.id.DisplayName);
        NameEditor = view.findViewById(R.id.nameEdit);
        ProfileMail = view.findViewById(R.id.ProfileMailID);
        MailEditor = view.findViewById(R.id.emailEdit);
        ProfileBranch = view.findViewById(R.id.DisplayBranch);
        ProfileYear = view.findViewById(R.id.DisplayYear);
        YearEditor = view.findViewById(R.id.yearEdit);
        ProfileDomain = view.findViewById(R.id.DisplayDomain);
        ProfilePic = view.findViewById(R.id.ProfilePic);
        progressBar = view.findViewById(R.id.progressBarProfile);
        ProfilePicChanger = view.findViewById(R.id.EditProfile);
        PasswordChanger = view.findViewById(R.id.ResetPassword);
        progressDialog= new ProgressDialog(getContext());
        dialog=new Dialog(getContext());
        passwordDialog= new Dialog(getContext());

        ProfileName.setEnabled(false);
        ProfileMail.setEnabled(false);
        progressBar.bringToFront();
        ProfileYear.setEnabled(false);

        /** Profile Pic Changer Dialog box **/
        dialog.setContentView(R.layout.customdialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Robot1=dialog.findViewById(R.id.profilePic1);
        Robot2=dialog.findViewById(R.id.profilePic2);
        Robot3=dialog.findViewById(R.id.profilePic3);
        Camera=dialog.findViewById(R.id.CameraOpener);
        Gallery=dialog.findViewById(R.id.GalleryOpener);

        /** Password resetter dialog box **/
        passwordDialog.setContentView(R.layout.resetpassworddialog);
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        passwordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        PasswordResetter=passwordDialog.findViewById(R.id.passwordReset);
        ExitButton=passwordDialog.findViewById(R.id.negativeButton);
        EtNew_password=passwordDialog.findViewById(R.id.resetPass);




        storageReference= FirebaseStorage.getInstance().getReference("Images").child("Profile Pics");
        reference = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {

         /**                      Fetching data from database                     **/
         reference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 user=snapshot.getValue(User.class);
                 assert user != null;
                 ProfileName.setText(user.getName());
                 ProfileYear.setText(user.getYear());
                 ProfileDomain.setText(user.getDomain());
                 ProfileBranch.setText(user.getBranch());
                 ProfileMail.setText(user.getEmail());

                 if (user.getProfilePic()!=null){
                         Glide.with(ProfilePic).load(user.getProfilePic()).placeholder(R.drawable.robot1).dontAnimate().into(ProfilePic);
                 }
                 progressBar.setVisibility(View.GONE);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
             }
         });

         /**                      Allowing user to edit their name             **/
         NameEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileName.setEnabled(true);
                ProfileName.setFocusable(true);
            }
        });

         /**                      Updating userName on pressing Enter key               **/
         ProfileName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    String Name = ProfileName.getText().toString();
                    if (TextUtils.isEmpty(Name)) {
                        ProfileName.setError("Please enter new name");
                    }
                  else
                      {
                    reference.child(firebaseUser.getUid()).child("name").setValue(Name).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Name changed successfully", Toast.LENGTH_SHORT).show();
                            ProfileName.setEnabled(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                    return true;
                } else
                    return false;
            }
        });

         /**                      Allowing user to change their email                  **/
         MailEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileMail.setEnabled(true);
                ProfileMail.setFocusable(true);
            }
        });

         /**                Initiating email update process on pressing Enter key             **/
         ProfileMail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)){
                    String Email=ProfileMail.getText().toString();

                    if(TextUtils.isEmpty(Email)){
                        ProfileMail.setError("Please Enter new Email");
                        ProfileMail.requestFocus();
                    }
                    else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                        ProfileMail.setError("Enter a valid Email");
                        ProfileMail.requestFocus();
                    }
                    else if (Objects.requireNonNull(firebaseUser.getEmail()).matches(Email)){
                        ProfileMail.setError("New Email cannot be same as old Email");
                    }
                    else {
                        progressDialog.setTitle("Email is updating");
                        progressDialog.show();
                        updateEmail(firebaseUser,Email);

                    }

                    return true;
                }
                return false;
            }
        });

         /**                Allowing user to change his/her year of study               **/
         YearEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileYear.setEnabled(true);
                ProfileYear.setFocusable(true);
            }
        });

         /**               Updating user's year of study on pressing Enter key            **/
         ProfileYear.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)){
                    String Year= ProfileYear.getText().toString();
                        reference.child(firebaseUser.getUid()).child("year").setValue(Year).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Year Updated Successfully", Toast.LENGTH_SHORT).show();
                                ProfileYear.setEnabled(false);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    return true;
                }
                return false;
            }
        });

         /** Showing custom dialog box to change image on clicking Edit Profile Pic Button **/
         ProfilePicChanger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                }
            });

         /** Adding default profile pic of robot 1 to the firebase database **/
         Robot1.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onClick(View view) {
                    Glide.with(ProfilePic).load("https://firebasestorage.googleapis.com/v0/b/rcr-3-8c92e.appspot.com/o/Images%2Frobot1.png?alt=media&token=d82d3284-8183-4e90-919d-87beb9863416").override(125,125).placeholder(R.drawable.robot1).
                            dontAnimate().into(ProfilePic);
                    dialog.dismiss();
                    reference.child(firebaseUser.getUid()).child("profilePic").setValue("https://firebasestorage.googleapis.com/v0/b/rcr-3-8c92e.appspot.com/o/Images%2Frobot1.png?alt=media&token=d82d3284-8183-4e90-919d-87beb9863416");
                }
            });

         /** Adding default pic of robot2 to the firebase database **/
         Robot2.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onClick(View view) {
                    Glide.with(ProfilePic).load("https://firebasestorage.googleapis.com/v0/b/rcr-3-8c92e.appspot.com/o/Images%2Frobot2.png?alt=media&token=d367e019-6d66-4259-bddf-72cc80e6cf3d").override(125,125).placeholder(R.drawable.robot1).
                            dontAnimate().into(ProfilePic);
                    dialog.dismiss();
                    reference.child(firebaseUser.getUid()).child("profilePic").setValue("https://firebasestorage.googleapis.com/v0/b/rcr-3-8c92e.appspot.com/o/Images%2Frobot2.png?alt=media&token=d367e019-6d66-4259-bddf-72cc80e6cf3d");
                }
            });

         /** Adding default pic of robot3 to the firebase databae **/
         Robot3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Glide.with(ProfilePic).load("https://firebasestorage.googleapis.com/v0/b/rcr-3-8c92e.appspot.com/o/Images%2Frobot3.png?alt=media&token=ac8b37ad-e982-4139-a50b-5fb56683d483").override(125,125).placeholder(R.drawable.robot1).
                            dontAnimate().into(ProfilePic);
                    dialog.dismiss();
                    reference.child(firebaseUser.getUid()).child("profilePic").setValue("https://firebasestorage.googleapis.com/v0/b/rcr-3-8c92e.appspot.com/o/Images%2Frobot3.png?alt=media&token=ac8b37ad-e982-4139-a50b-5fb56683d483");
                }
            });

         /** Opening gallery to choose image to crop and upload it in firebase storage**/
         Gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mGetContent.launch("image/*");
                }
            });

         Camera.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                Intent takePic= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cGetContent.launch(takePic);

             }
         });

         /** Starting CropperActivity with data of the image selected **/
         mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(),
                 new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    Intent intent= new Intent(getContext(),CropperActivity.class);
                    intent.putExtra("DATA",result.toString());
                    startActivityForResult(intent,101);
                }
            });

         cGetContent=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                 new ActivityResultCallback<ActivityResult>() {
             @RequiresApi(api = Build.VERSION_CODES.O)
             @Override
             public void onActivityResult(ActivityResult result) {
                 Bundle extras=result.getData().getExtras();
                 Uri imageUri;
                 Bitmap bitmap=(Bitmap) extras.get("data");


                 WeakReference<Bitmap> result1= new WeakReference<>(Bitmap.createBitmap(bitmap).copy(Bitmap.Config.RGB_565,true));

                 Bitmap imageBitmap=result1.get();
                 imageUri= saveImage(imageBitmap,getContext());
                 Intent intent= new Intent(getContext(),CropperActivity.class);
                 intent.putExtra("DATA",imageUri.toString());
                 startActivityForResult(intent,101);

             }
         });

         /** Shows dialog box asking user to update his/her password **/
         PasswordChanger.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                   passwordDialog.show();
             }
         });

         /** The button in reset password dialog box that initiates the process of updating password **/
         PasswordResetter.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String new_Pass= EtNew_password.getText().toString();
                 if (TextUtils.isEmpty(new_Pass)){
                     EtNew_password.setError("Type new password");
                 }

                 else {
                     firebaseUser.updatePassword(new_Pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {
                             passwordDialog.dismiss();
                             Toast.makeText(getContext(), "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             passwordDialog.dismiss();
                             Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                         }
                     });
                 }
             }
         });

         /** The button in reset password dialog box that dismisses the dialog box itself **/
         ExitButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 passwordDialog.dismiss();
             }
         });

    }
        return view;
    }

    private Uri saveImage(Bitmap imageBitmap, Context context) {
        File imagesFolder=new File(context.getCacheDir(),"images");
        Uri uri=null;
        try {
            imagesFolder.mkdirs();
            File file= new File(imagesFolder,"captured image.png");
            FileOutputStream stream= new FileOutputStream(file);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(context.getApplicationContext(),BuildConfig.APPLICATION_ID+".provider",file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    /** Method used while updating Email **/
    private void updateEmail(FirebaseUser firebaseUser, String email) {
        firebaseUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()){
                    firebaseUser.sendEmailVerification();
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "E-mail is updated. Please verify your new email", Toast.LENGTH_SHORT).show();
                    reference.child(firebaseUser.getUid()).child("email").setValue(email);
                    ProfileMail.setEnabled(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Adding cropped image to the ProfilePic image view **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==-1 && requestCode==101){
            String result= data.getStringExtra("RESULT");
            resultUri=null;

            if (result!=null){
                resultUri=Uri.parse(result);
            }

            Glide.with(ProfilePic).load(resultUri).override(125,125).placeholder(R.drawable.robot1).
                    dontAnimate().into(ProfilePic);
            dialog.dismiss();
            UpdateProfilePic();

        }
    }

    /** Adding image uri to the firebase storage as well as updating its value in database **/
    private void UpdateProfilePic() {
        if (resultUri!=null){
            progressDialog.setTitle("Image is uploading");
            progressDialog.show();

            StorageReference storageReference2=storageReference.child(user.getName() + ".png" );

            storageReference2.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Profile pic changed succesfully", Toast.LENGTH_SHORT).show();

                    storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url= uri.toString();
                            reference.child(firebaseUser.getUid()).child("profilePic").setValue(url);
                        }
                    });
                }
            });



        }
    }
}