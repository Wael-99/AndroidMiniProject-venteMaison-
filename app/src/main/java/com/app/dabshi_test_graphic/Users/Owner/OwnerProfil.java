package com.app.dabshi_test_graphic.Users.Owner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dabshi_test_graphic.LoginActivity;
import dabshi_test_graphic.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.app.dabshi_test_graphic.Users.Owner.OwnerMainActivity.bottomNavigation;
import static com.app.dabshi_test_graphic.SplashScreenActivity.isInternetAvailable;


public class OwnerProfil extends Fragment {

    private ImageView profileImg;
    private Button returnBtn, updateBtn, logoutBtn;
    private EditText username, password, email, phone;
    private TextView profileImgBtn;
    private FirebaseAuth auth;
    private StorageReference storageRef;
    private DatabaseReference ownerDatabase;
    private Uri filePath;
    private static String linkImage;


    public OwnerProfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_owner_profil, container, false);

        // GETTING VIEWS FROM FRAGMENT
        username = v.findViewById(R.id.owner_Edit_Username);
        password = v.findViewById(R.id.owner_Edit_Password);
        email = v.findViewById(R.id.owner_Edit_Email);
        phone = v.findViewById(R.id.owner_Edit_Phone);
        profileImg = v.findViewById(R.id.profil_img);
        profileImgBtn = v.findViewById(R.id.profil_img_Btn);
        returnBtn = v.findViewById(R.id.owner_profil_return);
        updateBtn = v.findViewById(R.id.owner_Update_Btn);
        logoutBtn = v.findViewById(R.id.owner_Logout_Btn);

        auth = FirebaseAuth.getInstance();
        //Get storage reference
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://rento-test-graphic.appspot.com/").child("images/").child(auth.getCurrentUser().getUid());    //change the url acco
        //Get Database reference
        ownerDatabase = FirebaseDatabase.getInstance().getReference("Users").child("Owner");


        ownerDatabase.child(Objects.requireNonNull(auth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username.setText(snapshot.child("username").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                    phone.setText(snapshot.child("phone").getValue().toString());
                    //check if there is a link in the database
                    if ((!snapshot.child("profileImage").getValue().toString().equals(" "))) {
                        Picasso.get().load(snapshot.child("profileImage").getValue().toString()).placeholder(R.drawable.load).into(profileImg);

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database erreur", error.getMessage());
            }
        });

        profileImgBtn.setOnClickListener(view -> {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 2);
        });

        updateBtn.setOnClickListener(view -> {
            //Call the update funtion
            update();
            Log.e("update btn", " this is brn update profile owner");
        });
        returnBtn.setOnClickListener(view -> bottomNavigation.show(1, true));
        logoutBtn.setOnClickListener(view -> {

            if (!isInternetAvailable()) {
                auth.signOut();
                getActivity().finish();
                Intent i = new Intent(getContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else {
                Toast.makeText(getContext(), "Check Your Connexion", Toast.LENGTH_SHORT).show();

            }

        });

        return v;

    }

    private void update() {

        String usernameTxt = username.getText().toString();
        String passwordTxt = password.getText().toString();
        String emailTxt = email.getText().toString();
        String phoneTxt = phone.getText().toString();

        if (usernameTxt.isEmpty()) {
            username.setError(" Need your name");
            username.requestFocus();
            return;
        }
        if (passwordTxt.isEmpty()) {
            password.setError("Need your password");
            password.requestFocus();
            return;
        }
        if (passwordTxt.length() < 6) {
            password.setError("Length of password is more than 6");
            password.requestFocus();
            return;
        }
        if (emailTxt.isEmpty()) {
            email.setError("Email is empty");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
            email.setError("Enter the valid email");
            email.requestFocus();
            return;
        }
        if (phoneTxt.isEmpty()) {
            phone.setError("Forget your phone number");
            phone.requestFocus();
            return;
        }
        if (phoneTxt.length() != 8) {
            phone.setError("Phone number have 8 numbers");
            phone.requestFocus();
            return;
        }
        if (!(phoneTxt.startsWith("5") || phoneTxt.startsWith("2") || phoneTxt.startsWith("9"))) {
            phone.setError("Enter a valide phone number");
            phone.requestFocus();
            return;
        }

        Objects.requireNonNull(auth.getCurrentUser()).updateEmail(emailTxt).addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Email updated!", Toast.LENGTH_LONG).show());
        auth.getCurrentUser().updatePassword(passwordTxt).addOnSuccessListener(aVoid -> Toast.makeText(getContext(), " Update done!", Toast.LENGTH_LONG).show());
        ownerDatabase.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ownerDatabase.child(auth.getCurrentUser().getUid()).child("username").setValue(usernameTxt);
                ownerDatabase.child(auth.getCurrentUser().getUid()).child("email").setValue(emailTxt);
                ownerDatabase.child(auth.getCurrentUser().getUid()).child("phone").setValue(phoneTxt);

                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), "Failed to update your informations!", Toast.LENGTH_LONG).show();

            }
        });

    }

    // RESULT OF THE IMAGES
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Deactivate user interaction until the upload done
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                if (filePath != null) {
                    final StorageReference ref = storageRef.child("ProfileImage"); // saving image under name of Profile image
                    UploadTask uploadTask = ref.putFile(filePath);

                    Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                        if (!task.isSuccessful()) {

                            Toast.makeText(getContext(), "Couldn't upload the image", Toast.LENGTH_SHORT).show();

                        }
                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }).addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            linkImage = task.getResult().toString();
                            //Adding the image link to the database
                            ownerDatabase.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ownerDatabase.child(auth.getCurrentUser().getUid()).child("profileImage").setValue(linkImage);
                                    //Reactivate the user interaction
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            // set the uri in the imageView
                        } else {
                            Toast.makeText(getContext(), "Failed !", Toast.LENGTH_SHORT).show();

                            //Reactivate the user interaction
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);                            // ...
                        }
                    });

                } else {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Toast.makeText(getContext(), "Select an image", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}