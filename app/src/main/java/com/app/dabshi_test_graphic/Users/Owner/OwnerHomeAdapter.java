package com.app.dabshi_test_graphic.Users.Owner;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dabshi_test_graphic.Property.Property;
import dabshi_test_graphic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.app.dabshi_test_graphic.Users.Owner.OwnerMainActivity.bottomNavigation;

public class OwnerHomeAdapter extends RecyclerView.Adapter<OwnerHomeAdapter.PropertyViewHolder> {

    private Context context;
    private ArrayList<Property> propertylist;
    private FirebaseAuth auth;
    private AlertDialog.Builder alertDialog;
    private DatabaseReference propertydatabase;
    public static String propertyId;

    public OwnerHomeAdapter(Context context, ArrayList<Property> propertylist) {
        this.context = context;
        this.propertylist = propertylist;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.row_owner_property, parent, false);

        return new PropertyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {

        Property property = propertylist.get(position);

        propertydatabase = FirebaseDatabase.getInstance().getReference("Property");
        alertDialog = new AlertDialog.Builder(context);
        auth = FirebaseAuth.getInstance();

        Picasso.get().load(property.getImageLink()).placeholder(R.drawable.load).into(holder.Image);
        holder.Title.setText(property.getTitle());
        holder.Location.setText(property.getLocation());
        holder.Price.setText(property.getPrice());
        holder.Room.setText(property.getRooms());

        holder.editBtn.setOnClickListener(view -> {

            propertyId = property.getIdproperty();

            replace(new OwnerEditPost());
            bottomNavigation.show(0, true);

        });
        holder.deleteBtn.setOnClickListener(view -> {

            alertDialog.setMessage("Are you sure you want to delete This Post ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {

                        // delete user from realtime database
                        propertydatabase.child(property.getIdproperty()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Data", propertydatabase.child(property.getIdproperty()).getKey());
//                                    replace(new HomeScreen());
                                    Toast.makeText(context, " Your post was deleted ", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Delete Error" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                        });

                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {
                        dialogInterface.cancel();

                    });
            alertDialog.show();
        });
        holder.rowOwnerProperty.setOnClickListener(view -> {
            // Send property information to the SinglePropertyActivity
            Intent i = new Intent(context, OwnerSingleProperty.class);
            i.putExtra("IdOwner", property.getIdowner());
            i.putExtra("IdProperty", property.getIdproperty());
            i.putExtra("Title", property.getTitle());
            i.putExtra("Price", property.getPrice());
            i.putExtra("Location", property.getLocation());
            i.putExtra("Rooms", property.getRooms());
            i.putExtra("Description", property.getDescription());
            i.putExtra("ImageLink", property.getImageLink());
            Log.e("Title of property in adapter", property.getTitle());

            // Shared Property adapter image with Single property  Activity image
            ActivityOptions transitionAnimation = ActivityOptions
                    .makeSceneTransitionAnimation((Activity) context, view.findViewById(R.id.ownerpropertyimagecard), "propertyOwnerImage");
            context.startActivity(i, transitionAnimation.toBundle());
        });

    }

    @Override
    public int getItemCount() {
        return propertylist.size();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        private ImageView Image;
        private TextView Title, Price, Location, Room;
        private Button editBtn, deleteBtn;
        private LinearLayout rowOwnerProperty;


        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);

            Image = itemView.findViewById(R.id.ownerpropertyimage);
            Title = itemView.findViewById(R.id.ownerpropertytitle);
            Price = itemView.findViewById(R.id.ownerprice);
            Location = itemView.findViewById(R.id.ownerpropertylocation);
            Room = itemView.findViewById(R.id.ownerroom);
            editBtn = itemView.findViewById(R.id.editPropertyBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            rowOwnerProperty = itemView.findViewById(R.id.rowOwnerProperty);


        }
    }

    private void replace(Fragment fragment) {

        FragmentTransaction transaction = ((OwnerMainActivity) context).getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment, fragment);
        transaction.commit();

    }
}


