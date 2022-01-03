package com.app.dabshi_test_graphic.Users.Client;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.app.dabshi_test_graphic.Property.Property;
import dabshi_test_graphic.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.Math.round;


public class ClientHomeFragment extends Fragment {
    private DatabaseReference propertydatabase;
    private RecyclerView recyclerView;
    private ClientHomeAdapter clientHomeAdapter;
    private ArrayList<Property> propertieslist;
    private SearchView searchView;
    private CardView filterCard;
    private ImageView filterIcon;
    private ChipGroup chipGroup;
    private LinearLayout priceView;
    private RangeSlider priceSlider;
    private EditText minPrice, maxPrice;
    private RadioGroup radioGroup;


    public ClientHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_client_home, container, false);


        filterCard = v.findViewById(R.id.cardView);
        filterIcon = v.findViewById(R.id.filterIcon);
        chipGroup = v.findViewById(R.id.chipGroup);
        priceView = v.findViewById(R.id.priceView);
        recyclerView = v.findViewById(R.id.client_list);
        priceSlider = v.findViewById(R.id.priceSlider);
        minPrice = v.findViewById(R.id.minPriceHold);
        maxPrice = v.findViewById(R.id.maxPriceHold);

        radioGroup = v.findViewById(R.id.radioGroup);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        propertieslist = new ArrayList<>();
        clientHomeAdapter = new ClientHomeAdapter(getContext(), propertieslist);
        recyclerView.setAdapter(clientHomeAdapter);

        valueListener();


        filterIcon.setOnClickListener(view -> {

            priceSlider.clearOnSliderTouchListeners();

            if (chipGroup.getVisibility() == VISIBLE) {

                // Hide Filters if Filter icon not clicked
                chipGroup.setVisibility(GONE);
                priceView.setVisibility(GONE);
                radioGroup.setVisibility(VISIBLE);

                // Clear checks when filter icon is gone
                radioGroup.clearCheck();
                chipGroup.clearCheck();
                priceSlider.clearOnSliderTouchListeners();
                priceSlider.clearOnChangeListeners();
                filterIcon.setBackground(getResources().getDrawable(R.drawable.ic_filter_icon));

            } else if (chipGroup.getVisibility() == GONE) {
                // set Filters Chips if filter icon is  clicked

                chipGroup.setVisibility(VISIBLE);
                filterIcon.setBackground(getResources().getDrawable(R.drawable.ic_filter_icon_selected));
                chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ChipGroup group, int checkedId) {

                        Chip chip = chipGroup.findViewById(checkedId);
                        if (chip != null) {
                            // On press chip action
                            switch (chip.getText().toString()) {
                                case "Price":
                                    priceView.setVisibility(VISIBLE);
                                    radioGroup.setVisibility(GONE);
                                    priceSlider.addOnChangeListener((slider, value, fromUser) -> {

                                        List<Float> val = slider.getValues();
                                        //  Getting min and max value from the sliderRange as String
                                        String min = String.valueOf(round(val.get(0)));
                                        String max = String.valueOf(round(val.get(1)));

                                        Log.e("price min", min);
                                        Log.e("price max", max);

                                        minPrice.setText(min + " M");
                                        maxPrice.setText(max + " M");

                                        // string array have min and max price
                                        String[] between = {min, max};

                                        filter(between);

                                    });

                                    break;
                                case "Most Recent":
                                    priceSlider.clearOnSliderTouchListeners();
                                    priceView.setVisibility(GONE);
                                    clientHomeAdapter.filterList(propertieslist);
                                    break;
                                case "Rooms":
                                    priceSlider.clearOnSliderTouchListeners();
                                    priceView.setVisibility(GONE);
                                    radioGroup.setVisibility(VISIBLE);
                                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                            String[] s0 = {"s+0"};
                                            String[] s1 = {"s+1"};
                                            String[] s2 = {"s+2"};
                                            String[] s3 = {"s+3"};
                                            String[] empty = {""};
                                            switch (i) {
                                                case R.id.s0:
                                                    filter(s0);
                                                    break;
                                                case R.id.s1:
                                                    filter(s1);
                                                    break;
                                                case R.id.s2:
                                                    filter(s2);
                                                    break;
                                                case R.id.s3:
                                                    filter(s3);
                                                    break;
                                                default:
                                                    filter(empty);
                                            }
                                        }
                                    });

                                    break;
                                case "City":


                                default:
                                    // if no chips checked
                                    radioGroup.clearCheck();
                                    priceSlider.clearOnSliderTouchListeners();
                                    priceView.setVisibility(GONE);
                                    radioGroup.setVisibility(GONE);
                            }
                        } else {
                            // If no chip pressed
                            priceView.setVisibility(GONE);
                            radioGroup.setVisibility(GONE);
                            priceSlider.clearOnSliderTouchListeners();



                        }
                    }
                });
            }
            chipGroup.scheduleLayoutAnimation();


        });



        return v;
    }


    private void valueListener() {

        propertydatabase = FirebaseDatabase.getInstance().getReference("Property");

        //Getting property data from database
        propertydatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear list of Property  : to get the new list is any changes happened
                propertieslist.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Property property = dataSnapshot.getValue(Property.class);
                    //adding to the list
                    propertieslist.add(property);

                }
                //notifyItemInserted(list.indexOf(user));
                clientHomeAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView.scheduleLayoutAnimation();

    }

    private void filter(String[] text) {
        // creating a new array list to filter our data.
        ArrayList<Property> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Property item : propertieslist) {
            // Catch NumberFormatException for casting string to int
            try {
                // checking if the entered string matched with any item of our recycler view.
                if (item.getTitle().toLowerCase().contains(text[0].toLowerCase())
                        || item.getRooms().toLowerCase().contains(text[0].toLowerCase())
                        || Integer.parseInt(item.getPrice()) > Integer.parseInt(text[0]) && Integer.parseInt(item.getPrice()) < Integer.parseInt(text[1])
                        || item.getLocation().toLowerCase().contains(text[0].toLowerCase())
                        || item.getDescription().toLowerCase().contains(text[0].toLowerCase())) {
                    // if the item is matched we are adding it to our filtered list.
                    filteredlist.add(0, item);
                }
            } catch (NumberFormatException e) {
            }

        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            clientHomeAdapter.filterList(filteredlist);
            recyclerView.scheduleLayoutAnimation();

        }
    }
}