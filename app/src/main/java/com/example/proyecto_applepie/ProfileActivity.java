package com.example.proyecto_applepie;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class ProfileActivity extends AppCompatActivity {

    TextView nameProfile, emailProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameProfile = findViewById(R.id.name);
        emailProfile = findViewById(R.id.email);

        if (MainScreen.acct != null) {
            String personName = MainScreen.acct.getDisplayName();
            String personEmail = MainScreen.acct.getEmail();
            String personId = MainScreen.acct.getId();
            //Uri profileImg = MainScreen.acct.getPhotoUrl();
            nameProfile.setText(personName);
            emailProfile.setText(personEmail);
            // URI to Image glide implementation (Not working anymore)
            //Glide.with(this).load(profileImg).into(profileImageView);
        }

    }

    public void changePayPalLink(View view){
        Intent inSettings = new Intent(this, SettingsActivity.class);
        startActivity(inSettings);
    }
}
