package com.example.financio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.financio.databinding.ActivityMainBinding;

/**
 * public class MainActivity
 *
 * This activity incorporates the 3 main fragments (Social, Home, Profile)
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String user;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = getIntent().getStringExtra("user");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_social, R.id.navigation_profile).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return true;
    }

    /**
     * This method assigns certain toasts to different menu items.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.miClose) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Exit Financio?");
            alertDialogBuilder.setMessage("Click yes to exit!");
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.miAddCategory) {
            Toast.makeText(this, "You clicked on add category.", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.miSettings) {
            Toast.makeText(this, "You clicked on settings.", Toast.LENGTH_SHORT).show();
            Intent nextScreen = new Intent(this, PreferencesActivity.class);
            if(user != null) nextScreen.putExtra("user", user);
            startActivity(nextScreen);

        } else if (item.getItemId() == R.id.miResetExpenses) {
            Toast.makeText(this, "All expenses reset.", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.miLogout) {
            Toast.makeText(this, "User has logged out.", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putBoolean("saveUser", false);
            editor.apply();
        }

        return true;
    }
}