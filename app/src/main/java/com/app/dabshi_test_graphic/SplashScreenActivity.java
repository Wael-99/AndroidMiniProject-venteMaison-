package com.app.dabshi_test_graphic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import com.app.dabshi_test_graphic.Users.Client.ClientMainActivity;
import com.app.dabshi_test_graphic.Users.Owner.OwnerMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import dabshi_test_graphic.R;

public class SplashScreenActivity extends AppCompatActivity {
    private View decorview;
    private FirebaseAuth auth;
    private DatabaseReference userReference;
    private Boolean adminReference, ownerReference, clientReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //hide system bars
        decorview = getWindow().getDecorView();
        decorview.setSystemUiVisibility(hideSystemBars());

        //get current user with firebase auth
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        ImageView img = findViewById(R.id.fulllogo);


        // make new animation : get the ressource animation
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        //set animation for our image
        img.startAnimation(aniFade);


        //delay for 2sec to show the SplashScreen before running the functions
        //This method will be executed once the timer is over
        new Handler().postDelayed(() -> {

            // check if there is a logged User
            if (user != null) {

                //if we a user is already logged in ,this listener will check if it's exists in the database of users
                userReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        ownerReference = snapshot.child("Owner").child(user.getUid()).exists();
                        clientReference = snapshot.child("Client").child(user.getUid()).exists();

                        Log.e("owner", ownerReference.toString());
                        Log.e("client", clientReference.toString());
                        //check if user email equals user admin
                        if (ownerReference) {
                            Log.e("Owner Condition", " im here");
                            Intent i = new Intent(SplashScreenActivity.this, OwnerMainActivity.class);
                            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            //check if user UID exist in the Client table of Users
                        } else if (clientReference) {
                            Log.e("Client Condition", " im here");
                            Intent i = new Intent(SplashScreenActivity.this, ClientMainActivity.class);
                            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else {
                            // if user was deleted from database
                            try {
                                Log.e("deleted user :", " im here");
                                auth.getCurrentUser().delete();
                                auth.signOut();
                                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                // Make the new intent the first activity
                                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                Toast.makeText(getBaseContext(), "Didn't found your  Account! ", Toast.LENGTH_SHORT).show();
                            } catch (NullPointerException e) {

                                Toast.makeText(getBaseContext(), " Your Account was deleted", Toast.LENGTH_LONG).show();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                // if there is no  logged User the application will start in the login activity
            } else {
                Log.e("No User Condition :", " im here");
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                // Make the new intent the first activity
                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                // Shared Splash screen Logo  and background with Login Activity small logo image and the blue bg

                Pair bg = Pair.create(this.findViewById(R.id.splashscreen), "bg");
                Pair logo = Pair.create(this.findViewById(R.id.fulllogo), "Logo");
                ActivityOptions transitionAnimation = ActivityOptions
                        .makeSceneTransitionAnimation(this, bg, logo);
                startActivity(i, transitionAnimation.toBundle());
            }
        }, 2000);

        // Delay of 1.5sec to check the connection status
        new Handler().postDelayed(() -> {
            Log.e("Connection stats", String.valueOf(isInternetAvailable()));
            // show a dialog if there is no internet connection
            if (!isInternetAvailable()) {

                Log.e("Im Here", "Test connection");
                //set dialog alert for reload the splash activate
                new AlertDialog.Builder(this)
                        .setMessage("NO INTERNET CONNEXION")
                        .setPositiveButton("Retry", (dialogInterface, i) -> recreate()).setCancelable(false).show();
            }

        }, 4000);


    }

    // check if there  is an internet connexion
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAdd = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAdd.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

}
