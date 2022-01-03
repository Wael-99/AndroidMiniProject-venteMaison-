package com.app.dabshi_test_graphic.Users.Client;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dabshi_test_graphic.Property.Property;
import dabshi_test_graphic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClientHomeAdapter extends RecyclerView.Adapter<ClientHomeAdapter.PropertyViewHolder> {

    private Context context;
    private ArrayList<Property> propertylist;

    public ClientHomeAdapter(Context context, ArrayList<Property> propertylist) {
        this.context = context;
        this.propertylist = propertylist;
    }


    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.row_client_property, parent, false);

        return new PropertyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertylist.get(position);

        Picasso.get().load(property.getImageLink()).placeholder(R.drawable.load).into(holder.Image);
        holder.Title.setText(property.getTitle());
        holder.Location.setText(property.getLocation());
        holder.Price.setText(property.getPrice());
        holder.Room.setText(property.getRooms());
        holder.rowProperty.setOnClickListener(view -> {
            // Send property information to the SinglePropertyActivity
            Intent i = new Intent(context, SinglePropertyActivity.class);
            i.putExtra("IdOwner", property.getIdowner());
            i.putExtra("IdProperty", property.getIdproperty());
            i.putExtra("Title", property.getTitle());
            i.putExtra("Price", property.getPrice());
            i.putExtra("Location", property.getLocation());
            i.putExtra("Rooms", property.getRooms());
            i.putExtra("Description", property.getDescription());
            i.putExtra("ImageLink", property.getImageLink());


            // Shared Property adapter image with Single property  Activity image
            ActivityOptions transitionAnimation = ActivityOptions
                    .makeSceneTransitionAnimation((Activity) context, view.findViewById(R.id.clientPropertyImageCard), "propertyImage");
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
        private LinearLayout rowProperty;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);


            Image = itemView.findViewById(R.id.clientPropertyImage);
            Title = itemView.findViewById(R.id.clientPropertyTitle);
            Price = itemView.findViewById(R.id.clientPrice);
            Location = itemView.findViewById(R.id.clientPropertyLocation);
            Room = itemView.findViewById(R.id.clientRoom);
            rowProperty = itemView.findViewById(R.id.row_client_property);


        }
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<Property> filterList) {
        //  add our filtered list in our property array list.
        propertylist = filterList;
        // notify our adapter as change in recycler view data.
        notifyDataSetChanged();
    }
}


