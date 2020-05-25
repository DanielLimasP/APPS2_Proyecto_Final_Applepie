package com.example.proyecto_applepie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainScreen extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    Bitmap qrBits;
    TextView nameTV;
    ImageView photoIV;
    FragmentTransaction ft;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    String personName;
    static String personEmail;
    String personId;
    Uri personPhoto;
    static String paypalMe = "PayPal.Me";

    Button generatorBtn, mainviewBtn, readerBtn;

    public static GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Init service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        nameTV = findViewById(R.id.name);
        photoIV = findViewById(R.id.photo);
        generatorBtn = findViewById(R.id.btnGenerator);
        mainviewBtn = findViewById(R.id.btnMain);
        readerBtn = findViewById(R.id.btnReader);

        // Conf signin options using google email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Create google sign in client using the options specified at gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        acct = GoogleSignIn.getLastSignedInAccount(MainScreen.this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
            loginUser(personEmail, personName, personId);
        }

        mainFragmetnLoad();

        // Set mainview button to selected and other to non-selected
        mainviewBtn.setBackground(ContextCompat.getDrawable(MainScreen.this, R.drawable.roundbtnselected));
        mainviewBtn.setTextColor(Color.WHITE);
        generatorBtn.setBackground(ContextCompat.getDrawable(MainScreen.this, R.drawable.roundbtn));
        generatorBtn.setTextColor(Color.parseColor("#181A24"));
        readerBtn.setBackground(ContextCompat.getDrawable(MainScreen.this, R.drawable.roundbtn));
        readerBtn.setTextColor(Color.parseColor("#181A24"));
    }

    // Signup function that gets called each time the users gets access to the app
    private void loginUser(String email, String name, String google_id) {
        compositeDisposable.add(iMyService.loginUser(email, name, google_id, "PayPal.Me Link")
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
        // Option to go to the settings screen
        if(item.getItemId() == R.id.mnSettings){
            Intent inSettings = new Intent(this, SettingsActivity.class);
            startActivity(inSettings);
            return true;
        // Option to launch onboard screen from the settings menu
        }else if(item.getItemId() == R.id.mnOnboard){
            Intent onBoardIntent = new Intent(MainScreen.this, OnBoarding.class);
            startActivity(onBoardIntent);
            return true;
        // Option to logout
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
        }else if(item.getItemId() == R.id.mnHistory){
            Intent inHistory = new Intent(MainScreen.this, HistoryActivity.class);
            startActivity(inHistory);
            return true;
            // Option to logout
        }else if(item.getItemId() == R.id.mnNewLog){
            Intent inNewLog = new Intent(MainScreen.this, NewLogActivity.class);
            startActivity(inNewLog);
            return true;
            // Option to logout
        }

        return super.onOptionsItemSelected(item);
    }

    // Method to access the QR Generator Fragment
    public void goToGenerator(View view){
        generatorBtn.setBackground(ContextCompat.getDrawable(MainScreen.this, R.drawable.roundbtnselected));
        generatorBtn.setTextColor(Color.WHITE);
        mainviewBtn.setBackground(ContextCompat.getDrawable(MainScreen.this, R.drawable.roundbtn));
        mainviewBtn.setTextColor(Color.parseColor("#181A24"));
        final Generator gen = new Generator();
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        gen.setClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String data = gen.qrEdtTxt.getText().toString();
                QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 200);
                try{
                    qrBits = qrgEncoder.encodeAsBitmap();
                    gen.imgQrDisplay.setImageBitmap(qrBits);
                }catch(WriterException e){
                    e.printStackTrace();
                }
                // Bit to save the qr to the SD card
                String rootDirectory = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(rootDirectory + "/saved_qrs");
                myDir.mkdirs();
                String fileName = "qrcode_paypal.jpg";
                File file = new File(myDir, fileName);
                if(file.exists()) file.delete();
                try{
                    FileOutputStream out = new FileOutputStream(file);
                    // Bitmap line
                    qrBits.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    Toast.makeText(MainScreen.this, "QR Saved!", Toast.LENGTH_SHORT).show();
                    sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        // Continue with fragment replacement
        ft.replace(R.id.fragLayout, gen);
        ft.addToBackStack("stack");
        ft.commit();

    }

    public void gotoMain(View view){
        mainFragmetnLoad();
    }

    // Method to access the QR Reader Fragment
    public void goToReader(View view){
        mainviewBtn.setBackground(ContextCompat.getDrawable(MainScreen.this, R.drawable.roundbtn));
        mainviewBtn.setTextColor(Color.parseColor("#181A24"));
        readerBtn.setBackground(ContextCompat.getDrawable(MainScreen.this, R.drawable.roundbtnselected));
        readerBtn.setTextColor(Color.WHITE);
        readerBtn.setBackground(ContextCompat.getDrawable(MainScreen.this, R.drawable.roundbtn));
        readerBtn.setTextColor(Color.parseColor("#181A24"));
        Intent readerIntent = new Intent(MainScreen.this, ReaderActivity.class);
        startActivity(readerIntent);
    }

    public void mainFragmetnLoad(){
        generatorBtn.setBackground(ContextCompat.getDrawable(MainScreen.this, R.drawable.roundbtn));
        generatorBtn.setTextColor(Color.parseColor("#181A24"));
        mainviewBtn.setBackground(ContextCompat.getDrawable(MainScreen.this, R.drawable.roundbtnselected));
        mainviewBtn.setTextColor(Color.WHITE);
        MainFragment mainFragment = new MainFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        mainFragment.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSettings = new Intent(MainScreen.this, SettingsActivity.class);
                startActivity(inSettings);
            }
        });

        ft.replace(R.id.fragLayout, mainFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}



