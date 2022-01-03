package com.app.dabshi_test_graphic.Users.Client;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dabshi_test_graphic.R;
import com.app.dabshi_test_graphic.Users.Complain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.app.dabshi_test_graphic.Users.Client.ClientMainActivity.clientBottomNavigation;


public class ComplainFragment extends Fragment {

    private Button returnBtn,sendBtn;
    private EditText subject,message;
    private DatabaseReference complainDatabase,userDatabase;
    private FirebaseAuth auth;
    // push database key for the complain
    public static String idComplain;

    public ComplainFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_complain, container, false);

        subject = v.findViewById(R.id.complainSubject);
        message = v.findViewById(R.id.complainMessage);
        sendBtn = v.findViewById(R.id.sendComplainButton);
        returnBtn =v.findViewById(R.id.client_complain_return);

        complainDatabase = FirebaseDatabase.getInstance().getReference("Complains");
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();

        sendBtn.setOnClickListener(view -> {
            sendBtn.setEnabled(false);
            SendComplain();
        });

        returnBtn.setOnClickListener(view -> {
            clientBottomNavigation.show(1, true);

        });

        return v;

    }

    private void SendComplain() {

        String input_subject = subject.getText().toString();
        String input_message = message.getText().toString();
        idComplain = complainDatabase.push().getKey();



        if (input_subject.isEmpty()) {
            subject.setError("What's the problem about?");
            subject.requestFocus();
            return;
        }
        if (input_message.isEmpty()) {
            message.setError("Explain your problem to us !");
            message.requestFocus();
            return;
        }

        //Getting the user information from the database
        userDatabase.child("Client").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String id = snapshot.child("id").getValue().toString();
                String name = snapshot.child("username").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String image = snapshot.child("profileImage").getValue().toString();

                //Creating complain instance
                Complain complain =new Complain(id,idComplain,image,name,email,input_subject,input_message);
                //adding the complain to the database
                complainDatabase.child(idComplain).setValue(complain).addOnSuccessListener(aVoid -> {
                    //Success message
                    Toast.makeText(getContext(), "Send Successfully", Toast.LENGTH_LONG).show();
                    sendBtn.setEnabled(true);
                    subject.getText().clear();
                    message.getText().clear();

                }).addOnFailureListener(e -> {

                    Toast.makeText(getContext(), "Didn't Send", Toast.LENGTH_LONG).show();
                    sendBtn.setEnabled(true);

                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}