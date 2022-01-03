package com.app.dabshi_test_graphic.Users.Client;

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
import com.google.android.gms.tasks.OnSuccessListener;
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
import static com.app.dabshi_test_graphic.Users.Client.ClientMainActivity.clientBottomNavigation;
import static com.app.dabshi_test_graphic.SplashScreenActivity.isInternetAvailable;


public class ClientProfilFragment extends Fragment {
    private ImageView profileImg;
    private Button returnBtn, updateBtn, logoutBtn;
    private EditText username, password, email, phone;
    private TextView profileImgBtn;
    private FirebaseAuth auth;
    private DatabaseReference clientDatabase;
    private StorageReference storageRef;
    private Uri filePath;
    private static String linkImage;



    public ClientProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_client_profil, container, false);

        //View Initialisation
        username = v.findViewById(R.id.client_Edit_Username);
        password = v.findViewById(R.id.client_Edit_Password);
        email = v.findViewById(R.id.client_Edit_Email);
        phone = v.findViewById(R.id.client_Edit_Phone);
        profileImg = v.findViewById(R.id.profil_img);
        profileImgBtn = v.findViewById(R.id.profil_img_Btn);
        returnBtn = v.findViewById(R.id.client_profil_return);
        updateBtn = v.findViewById(R.id.client_Update_Btn);
        logoutBtn = v.findViewById(R.id.client_Logout_Btn);

        auth = FirebaseAuth.getInstance();
        //Get storage reference
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://rento-test-graphic.appspot.com/").child("images/").child(auth.getCurrentUser().getUid());    //change the url acco
        //Get Database reference
        clientDatabase = FirebaseDatabase.getInstance().getReference("Users").child("Client");
            // Fetching data from data base
            clientDatabase.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){

                    username.setText(Objects.requireNonNull(snapshot.child("username").getValue()).toString());
                    email.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                    phone.setText(Objects.requireNonNull(snapshot.child("phone").getValue()).toString());
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



        //Loading image profile from internet
        Picasso.get().load("https://flyclipart.com/thumb2/person-round-png-icon-free-download-137544.png")
                .into(profileImg);

        // Button Sets in view
        updateBtn.setOnClickListener(view -> {

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
                Log.d("Length of phone", String.valueOf(phoneTxt.charAt(0)));
                return;
            }
            if ((phoneTxt.startsWith("5") || phoneTxt.startsWith("2") || phoneTxt.startsWith("9")) == false) {
                phone.setError("Enter a valide phone number");
                phone.requestFocus();
                Log.d("Length of phone", String.valueOf(phoneTxt.charAt(0)));
                return;
            }
            if (!isInternetAvailable()) {
                auth.getCurrentUser().updateEmail(emailTxt).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void aVoid) {
                        Toast.makeText(getContext(), "Email updated!", Toast.LENGTH_SHORT).show();

                    }
                });
                auth.getCurrentUser().updatePassword(passwordTxt).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void aVoid) {
                        Toast.makeText(getContext(), " Update done!", Toast.LENGTH_SHORT).show();

                    }
                });
                clientDatabase.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        clientDatabase.child(auth.getCurrentUser().getUid()).child("username").setValue(usernameTxt);
                        clientDatabase.child(auth.getCurrentUser().getUid()).child("email").setValue(emailTxt);
                        clientDatabase.child(auth.getCurrentUser().getUid()).child("phone").setValue(phoneTxt);
                        if ((!snapshot.child("profileImage").getValue().toString().equals(" "))) {
                            Picasso.get().load(snapshot.child("profileImage").getValue().toString()).placeholder(R.drawable.load).into(profileImg);
                        }

                        Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(getContext(), "Failed to update your informations!", Toast.LENGTH_LONG).show();

                    }
                });
            } else {
                Toast.makeText(getContext(), "Check Your Connexion", Toast.LENGTH_SHORT).show();

            }

        });

        profileImgBtn.setOnClickListener(view -> {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 2);
        });
        returnBtn.setOnClickListener(view -> {
            clientBottomNavigation.show(1, true);
        });
        logoutBtn.setOnClickListener(view -> {
            if (!isInternetAvailable()) {
                getActivity().finish();
                auth.signOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else {
                Toast.makeText(getContext(), "Check Your Connexion", Toast.LENGTH_SHORT).show();

            }
        });


        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


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
                            clientDatabase.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    clientDatabase.child(auth.getCurrentUser().getUid()).child("profileImage").setValue(linkImage);
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
                    Toast.makeText(getContext(), "Select an image", Toast.LENGTH_SHORT).show();
                }
                //Setting image to ImageView
//                profileImg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}