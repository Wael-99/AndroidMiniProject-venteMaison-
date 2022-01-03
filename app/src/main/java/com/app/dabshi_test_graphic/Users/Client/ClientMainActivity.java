package com.app.dabshi_test_graphic.Users.Client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.dabshi_test_graphic.*;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import dabshi_test_graphic.R;

public class ClientMainActivity extends AppCompatActivity {

    public static MeowBottomNavigation clientBottomNavigation;

    @Override
    public void onBackPressed() {

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        Log.e("Client main :", " im here");

        try {
            // Initialisation of the Bottom Menu
            clientBottomNavigation = findViewById(R.id.clientnavigationmenu);
            clientBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home));
            clientBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.chat));
            clientBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_profil));

            clientBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {

                @Override
                public void onClickItem(MeowBottomNavigation.Model item) {
                    switch (item.getId()) {
                        case 1:
                            replace(new ClientHomeFragment());
                            break;
                        case 2:
                            replace(new ComplainFragment());
                            break;
                        case 3:
                            replace(new ClientProfilFragment());
                            break;

                    }
                }
            });


            clientBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
                @Override
                public void onShowItem(MeowBottomNavigation.Model item) {
                    if (item.getId() == 1) {
                        replace(new ClientHomeFragment());
                    }
                }
            });

            clientBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
                @Override
                public void onReselectItem(MeowBottomNavigation.Model item) {

                }
            });
//            clientBottomNavigation.setCount(1, "2");
            clientBottomNavigation.show(1, true);

        } catch (NullPointerException ignored) {
        }
    }


    private void replace(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.clientFrame, fragment);
        transaction.commit();
    }
    //    @Override
//    protected void onResume() {
//        super.onResume();
//        clientBottomNavigation.show(1, true);
//    }


}
