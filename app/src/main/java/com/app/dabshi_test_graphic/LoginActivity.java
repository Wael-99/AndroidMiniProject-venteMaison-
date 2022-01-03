package com.app.dabshi_test_graphic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.app.dabshi_test_graphic.Users.Client.ClientMainActivity;
import com.app.dabshi_test_graphic.Users.Owner.OwnerMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import dabshi_test_graphic.R;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextView forget, textsignup;
    private EditText email;
    private EditText password;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private DatabaseReference UserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View decorview;


        decorview = getWindow().getDecorView();
        decorview.setSystemUiVisibility(hideSystemBars());

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        textsignup = findViewById(R.id.text_signup);

        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        UserDatabase = FirebaseDatabase.getInstance().getReference("Users");
        // If the device user to login into an account but it was removed
        // this will delete the removed user from the FirebaseAuth
//        CheckUser();

        textsignup.setOnClickListener(v -> {

            Intent i = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(i);
        });



        login.setOnClickListener(v -> loginUser());

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void loginUser() {
        String txt_email = email.getText().toString();
        String txt_password = password.getText().toString();

        if (txt_email.isEmpty()) {
            email.setError("Enter Your Email");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
            email.setError("Enter Valid Email");
            email.requestFocus();
            return;
        }
        if (txt_password.isEmpty()) {
            password.setError("Enter Your Password");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        login.setEnabled(false);

        //SignIn with email and password
        auth.signInWithEmailAndPassword(txt_email, txt_password).addOnSuccessListener(authResult -> {

            //If teh user get into his deleted account this will remove his account from the FirebaseAuth
             CheckUser();


        }).addOnFailureListener(e -> {

            progressBar.setVisibility(View.GONE);
            login.setEnabled(true);
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

        });

    }

    private void CheckUser() {

        String txt_email = email.getText().toString();

        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (Objects.equals(snapshot.child("Client").child(Objects.requireNonNull(auth.getUid())).child("email").getValue(), txt_email)) {

                    Intent i = new Intent(LoginActivity.this, ClientMainActivity.class);
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                else if (Objects.equals(snapshot.child("Owner").child(auth.getUid()).child("email").getValue(), txt_email)) {

                    Intent i = new Intent(LoginActivity.this, OwnerMainActivity.class);
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_CLEAR_TASK);
                    progressBar.setVisibility(View.GONE);
                    startActivity(i);
                    finish();
                } else {
                    auth.getCurrentUser().delete();
                    Toast.makeText(LoginActivity.this, "Your Account was delete", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    login.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("database erreur", error.getMessage());
            }
        });

    }
    //Hide System bars
    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

}