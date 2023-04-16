package com.example.forum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private String userName;
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    SliderView sliderView;
    int[] images = {R.drawable.ul01, R.drawable.ul02, R.drawable.ul03, R.drawable.ul04};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_page);
        getUserName();
        sliderView = findViewById(R.id.image_slider);
        TextView title = findViewById(R.id.toolbar_title);
        ImageView menu_button = findViewById(R.id.menu_button);

        SliderAdapter sliderAdapter = new SliderAdapter(images);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = new Bundle();
                switch (item.getItemId()) {
                    case R.id.home:

                        bundle.putString("userName", userName);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.settings:

                        bundle.putString("userName", userName);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                        return true;

                }
                return false;
            }
        });

    }

    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.map_btn) {
                    Intent intent = new Intent(MainActivity.this, MapPage.class);
                    startActivity(intent);
                }

                if (menuItem.getItemId() == R.id.forum_btn) {
                    userName = getUserName();
                    Intent intent = new Intent(MainActivity.this, forum.class);
                    intent.putExtra("userName", userName);
                    startActivity(intent);
                }

                if (menuItem.getItemId() == R.id.timetable_btn) {
                    userName = getUserName();
                    Intent intent = new Intent(MainActivity.this, TimeTable.class);
                    intent.putExtra("userName", userName);
                    startActivity(intent);
                }

                if (menuItem.getItemId() == R.id.friends_btn) {
                    userName = getUserName();
                    Intent intent = new Intent(MainActivity.this, FriendSearchActivity.class);
                    intent.putExtra("userName", userName);
                    startActivity(intent);
                }

                return true;
            }
        });
        popupMenu.show();
    }

    public String getUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        return sharedPreferences.getString("userName", "");
    }

    public void logout(View view) {
        Intent intent = new Intent(MainActivity.this, login.class);
        startActivity(intent);
    }

    public void changePassword(View view) {

        View puWindow = getLayoutInflater().inflate(R.layout.changepassword, null);
        PopupWindow popupWindow = new PopupWindow(puWindow, 1000, 550, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        Button confirm = puWindow.findViewById(R.id.confirmBtn);
        Button cancel = puWindow.findViewById(R.id.cancelBtn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText oldView = puWindow.findViewById(R.id.oldpassword);
                EditText newView = puWindow.findViewById(R.id.newpassword);
                String oldPassword = oldView.getText().toString();
                String newPassword = newView.getText().toString();
                if (oldPassword.isEmpty()) {
                    oldView.setError("This field cannot be empty.");
                } else if (newPassword.isEmpty()) {
                    newView.setError("This field cannot be empty");
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    DocumentReference documentReference = db.document("user/" + userName);
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> map;
                                    map = document.getData();
                                    String password = map.get("password").toString();
                                    if (password.equals(oldPassword)) {
                                        map.put("password", newPassword);
                                        documentReference.update(map);
                                        popupWindow.dismiss();
                                        Intent intent = new Intent(MainActivity.this, login.class);
                                        startActivity(intent);
                                    }else{
                                        newView.setError("Password does not match");
                                    }
                                } else {
                                    newView.setError("user does not exist");
                                }
                            } else {
                                Log.e("login", "Something went wrong");
                            }
                        }
                    });
                }
            }
        });

    }
}