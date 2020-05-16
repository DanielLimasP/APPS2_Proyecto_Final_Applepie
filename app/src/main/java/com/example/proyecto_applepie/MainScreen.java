package com.example.proyecto_applepie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.proyecto_applepie.Retrofit.IMyService;
import com.example.proyecto_applepie.Retrofit.RetrofitClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainScreen extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;

    TextView nameTV;
    ImageView photoIV;
    FragmentTransaction ft;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Init service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        nameTV = findViewById(R.id.name);
        photoIV = findViewById(R.id.photo);

        // Conf signin options using google email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Create google sign in client using the options specified at gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainScreen.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            loginUser(personEmail, personName, personId);
            nameTV.setText(personName);
            Glide.with(this).load(personPhoto).into(photoIV);
        }
    }

    // Signup function that gets called each time the users gets access to the app
    private void loginUser(String email, String name, String google_id) {
        compositeDisposable.add(iMyService.loginUser(email, name, google_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(MainScreen.this, ""+response, Toast.LENGTH_LONG).show();
                    }
                }));
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnSettings){
            Intent inSettings = new Intent(this, SettingsActivity.class);
            startActivity(inSettings);
            return true;
        // Option to launch onboard screen from the settings menu
        }else if(item.getItemId() == R.id.mnOnboard){
            Intent onBoardIntent = new Intent(MainScreen.this, OnBoarding.class);
            startActivity(onBoardIntent);
            return true;
        }else if(item.getItemId() == R.id.mnLogout){
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainScreen.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainScreen.this, MainActivity.class));
                            finish();
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to access the QR Generator Fragment
    public void goToGenerator(View view){
        final Generator gen = new Generator();
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,android.R.anim.fade_in, android.R.anim.fade_out);
        gen.setClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String data = gen.qrEdtTxt.getText().toString();

                QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 200);
                try{
                    Bitmap qrBits = qrgEncoder.encodeAsBitmap();
                    gen.imgQrDisplay.setImageBitmap(qrBits);
                }catch(WriterException e){
                    e.printStackTrace();
                }
            }
        });
        ft.replace(R.id.fragmentLayout1, gen);
        ft.addToBackStack("stack");
        ft.commit();
    }

    // Method to access the QR Reader Fragment
    public void goToReader(View view){

    }
}
