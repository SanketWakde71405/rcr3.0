package com.example.rcr30;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MoreFragment extends Fragment {

    View view;
    TextView NameUser;
    MaterialCardView cartActivityOpen;
    MaterialCardView historyActivityOpen;
    MaterialCardView feedbackOpen;
    MaterialCardView reviewOpen;
    TextView AboutSggsWebsiteOpener;
    TextView AboutRnxgWebsiteOpener;
    TextView TeamViewer;
    TextView ProjectViewer;
    TextView AchievementViewer;
    TextView TermsDisplay;
    TextView PrivacyPolicyDisplay;
    ImageView fb;
    ImageView insta;
    ImageView linkedin;
    ImageView yt;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String name;
    DatabaseReference reference;

    @SuppressLint("SetTextI18n")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /** Inflate the layout for this fragment **/

        view= inflater.inflate(R.layout.fragment_more, container, false);

        /** Initialization of other components of the fragment **/
        NameUser=view.findViewById(R.id.userName);
        cartActivityOpen=view.findViewById(R.id.myCartOpener);
        historyActivityOpen=view.findViewById(R.id.myOrderOpener);
        feedbackOpen=view.findViewById(R.id.feedBackOpener);
        reviewOpen=view.findViewById(R.id.reviewer);
        AboutSggsWebsiteOpener=view.findViewById(R.id.aboutSGGS);
        AboutRnxgWebsiteOpener=view.findViewById(R.id.aboutRNXG);
        TeamViewer=view.findViewById(R.id.teamRNXG);
        ProjectViewer=view.findViewById(R.id.projectsOpener);
        AchievementViewer=view.findViewById(R.id.achievementsOpener);
        TermsDisplay=view.findViewById(R.id.termsOpener);
        PrivacyPolicyDisplay=view.findViewById(R.id.privacyWebPageOpener);
        fb=view.findViewById(R.id.facebookOpener);
        insta=view.findViewById(R.id.instagramOpener);
        linkedin=view.findViewById(R.id.linkedInOpener);
        yt=view.findViewById(R.id.uTubeOpener);

        /** Firebase initializations **/
        mAuth=FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");

        /** greeting user by taking his/her name by fetching his name from database **/
        reference.child(user.getUid()).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name= snapshot.getValue(String.class);
                NameUser.setText("Hey! "+ name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /** Opens Cart Activity **/
        cartActivityOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart= new Intent(getContext(),Cart.class);
                startActivity(cart);
            }
        });

        /** Opens sggs website **/
        AboutSggsWebsiteOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aboutSggs= new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.sggs.ac.in/home/page/ABOUT-SGGS"));
                startActivity(aboutSggs);
            }
        });

        /** Opens rnxg website **/
        AboutRnxgWebsiteOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aboutRnxg= new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.rnxg.co.in/profiles"));
                startActivity(aboutRnxg);
            }
        });

        /** Opens rnxg website page where all projects are displayed **/
        ProjectViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Projects= new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.rnxg.co.in/projects"));
                startActivity(Projects);
            }
        });

        /** Displays terms and conditions of website and app **/
        TermsDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Conditions= new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.rnxg.co.in/Terms"));
                startActivity(Conditions);
            }
        });

        /** Displays privacy policy **/
        PrivacyPolicyDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Privacy= new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.rnxg.co.in/Privicy"));
                startActivity(Privacy);
            }
        });

        /** Opens Facebook Profile Page of RNXG **/
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RnxgFBProfile= new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/rnxgsggs/"));
                startActivity(RnxgFBProfile);
            }
        });

        /** Opens Insta Profile Page of RNXG **/
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RnxgInstaProfile= new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/sggs_rnxg/"));
                startActivity(RnxgInstaProfile);
            }
        });

        /** Opens LinkedIn Profile Page of Rnxg **/
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  RnxgInProfile= new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.linkedin.com/company/rnxg?original_referer=https%3A%2F%2Fwww.rnxg.co.in%2F"));
                startActivity(RnxgInProfile);
            }
        });

        /** Opens Youtube channel of RNXG **/
        yt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RnxgUtubeChannel= new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/channel/UC9fFrYx9UEpxDjS9YO6t2FA"));
                startActivity(RnxgUtubeChannel);
            }
        });

        /** Displays list of people of rnxg with their domain and profile pics **/
        TeamViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext(),Team.class);
                startActivity(intent);
            }
        });

        historyActivityOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),History.class);
                startActivity(intent);
            }
        });

        return view;
    }
}