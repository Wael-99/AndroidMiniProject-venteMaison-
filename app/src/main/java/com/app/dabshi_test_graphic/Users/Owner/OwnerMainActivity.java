package com.app.dabshi_test_graphic.Users.Owner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import dabshi_test_graphic.R;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class OwnerMainActivity extends AppCompatActivity {

    public static MeowBottomNavigation bottomNavigation;

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
        setContentView(R.layout.activity_owner_main);

        try {
            bottomNavigation = findViewById(R.id.navigationmenu);

            Log.e("Owner Main :", " im here");

            bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
            bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_add_circle_24));
            bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_profil));

            bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {

                @Override
                public void onClickItem(MeowBottomNavigation.Model item) {
                    switch (item.getId()) {
                        case 1:
                            replace(new OwnerHomeFragment());
                            break;
                        case 2:
                            replace(new OwnerAddScreen());
                            break;
                        case 3:
                            replace(new OwnerProfil());
                            break;
                    }
                }
            });
            bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
                @Override
                public void onShowItem(MeowBottomNavigation.Model item) {
                    if (item.getId() == 1) {
                        replace(new OwnerHomeFragment());
                    }
                }
            });

            bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
                @Override
                public void onReselectItem(MeowBottomNavigation.Model item) {

                }
            });

//        bottomNavigation.setCount(1, "1");
            bottomNavigation.show(1, true);

        } catch (NullPointerException ignored) {}
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        bottomNavigation.show(1, true);
//
//    }

    // for setting the fragment in the FrameLayout
    private void replace(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment, fragment);
        transaction.commit();

    }

}