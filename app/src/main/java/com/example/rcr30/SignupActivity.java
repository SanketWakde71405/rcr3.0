package com.example.rcr30;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SignupActivity extends AppCompatActivity {
    public EditText mail;
    public EditText name;
    public EditText password;
    public EditText confirm;
    TextView Login_start;
    Spinner domain, Year, Branch;
    FirebaseAuth Auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;
    String Name, Email, Password, confirmation, domain_selected, branch_selected, year_selected;
    Button signup;
    ProgressBar progressBar_signup;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /** Initialization **/
        Login_start=findViewById(R.id.go_to_login);
        name = findViewById(R.id.editTextTextPersonName2);
        mail = findViewById(R.id.editTextTextEmailAddress2);
        confirm = findViewById(R.id.editTextNumberPassword2);
        password = findViewById(R.id.editTextNumberPassword);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        Auth = FirebaseAuth.getInstance();
        Branch = findViewById(R.id.spinner3);
        progressBar_signup = findViewById(R.id.signup_progress);


        /** Adding an array list to the spinner **/
        List<String> branch = new ArrayList<>();
        branch.add(0, "Select branch:");
        branch.add("IT");
        branch.add("CSE");
        branch.add("ECE");
        branch.add("Electrical");
        branch.add("Chemical");
        branch.add("Mechanical");
        branch.add("Textile");
        branch.add("Production");

        /** Adding an adapter to the spinner **/
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branch);
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Branch.setAdapter(myAdapter1);


        /** Extracting string from selected item **/
        Branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select branch:")) {
                    //Umaan is great

                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "Selected:" + item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Spinner Branch = findViewById(R.id.spinner3);
                Branch.setSelection(0);
            }
        });


        Year = findViewById(R.id.spinner4);

        /** Adding Arraylist to the year spinner **/
        List<String> year = new ArrayList<>();
        year.add(0, "Select Year");
        year.add("FY");
        year.add("SY");
        year.add("TY");
        year.add("Final Year");

        /** Adding adapter to the year spinner **/
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, year);
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Year.setAdapter(myAdapter2);


        /** Extracting string from the selected item **/
        Year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Year")) {
                    //No ones gonna know
                } else {
                    String item1 = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "Selected" + item1, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Spinner Year = findViewById(R.id.spinner4);
                Year.setSelection(0);
            }
        });
        domain = findViewById(R.id.spinner5);

        /** Adding arrayList to the domain spinner **/
        List<String> Domain = new ArrayList<>();
        Domain.add(0, "Select Field of Interest");
        Domain.add("Software");
        Domain.add("Electronics");
        Domain.add("Mechanical");

        /** Adding adapter to the domain spinner **/
        ArrayAdapter<String> myAdapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Domain);
        myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        domain.setAdapter(myAdapter3);

        /** Extracting string from the selected item **/
        domain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Field of Interest")) {

                } else {
                    String item3 = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "selected " + item3, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Spinner domain = findViewById(R.id.spinner5);
                domain.setSelection(0);
            }
        });

        signup = findViewById(R.id.button3);


        /** Making signup button invisible till the last required data field do not have text **/
        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 0) {
                    signup.setVisibility(View.GONE);
                } else {
                    signup.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /** Changing colour of the text that asks user if he is current use then he can login directly **/
        Login_start.setPaintFlags(Login_start.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        /** On clicking text that sends to the login the colour of the text changes **/
        Login_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                Login_start.setTextColor(Color.parseColor("#551A8B"));
            }
        });

        /** User registration **/
        signup.setOnClickListener(v -> {
            Name = name.getText().toString().trim();
            Email = mail.getText().toString().trim();
            Password = password.getText().toString().trim();
            confirmation = confirm.getText().toString().trim();
            domain_selected = domain.getSelectedItem().toString();
            branch_selected = Branch.getSelectedItem().toString();
            year_selected = Year.getSelectedItem().toString();


            if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(confirmation))
                Toast.makeText(SignupActivity.this, "You missed something:", Toast.LENGTH_SHORT).show();
            else if (!Password.equals(confirmation))
                Toast.makeText(SignupActivity.this, "The entered and confirmation password doesn't match.", Toast.LENGTH_SHORT).show();
            else if (Password.length() < 6)
                password.setError("The password length should be greater than 6.");
            else {
                progressBar_signup.setVisibility(View.VISIBLE);
                progressBar_signup.bringToFront();

                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        progressBar_signup.setProgress(counter);
                        counter++;

                        if (counter == 100) {
                            timer.cancel();
                            Auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    user = Auth.getCurrentUser();

                                    Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    if (user != null) {
                                        user.sendEmailVerification().addOnSuccessListener(unused -> {
                                            String UID = user.getUid();
                                            // adding user's data to the database
                                            reference.child("Users").child(UID).child("Name").setValue(Name);
                                            reference.child("Users").child(UID).child("Email").setValue(Email);
                                            reference.child("Users").child(UID).child("Branch").setValue(branch_selected);
                                            reference.child("Users").child(UID).child("Year").setValue(year_selected);
                                            reference.child("Users").child(UID).child("Domain").setValue(domain_selected);
                                            progressBar_signup.setVisibility(View.INVISIBLE);
                                            Toast.makeText(SignupActivity.this, "Email verification has been sent.", Toast.LENGTH_SHORT).show();


                                        }).addOnFailureListener(e -> Toast.makeText(SignupActivity.this, "Email is not verified.", Toast.LENGTH_SHORT).show());
                                    }

                                } else
                                    Toast.makeText(SignupActivity.this, "Registration unsuccessful", Toast.LENGTH_SHORT).show();
                                    progressBar_signup.setVisibility(View.INVISIBLE);
                            });
                        }
                    }

                };

                timer.schedule(timerTask, 100, 100);

            }
        });
    }
}
