package com.example.financio;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * public class LoginActivity
 * This will be the default screen when opening the app, allowing you to sign into the main activity or register through the register activity.
 */
public class LoginActivity extends AppCompatActivity {
    public static TextView textUsername;
    public static TextView textPassword;
    private static String loginUserID;
    private JSONObject user;
    String url = "http://coms-309-005.class.las.iastate.edu:8080";
    JSONArray users;
    SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textUsername = findViewById(R.id.editTextUsername);
        textPassword = findViewById(R.id.editTextPassword);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //getBoolean(<key stored in sharedPref>, <default boolean value if fails>)
        //If saveUser is ticked in preferences, the app will automatically log the user back in after they close the app.
        if(sharedPref.getBoolean("saveUser", false)) login(sharedPref.getString("username", ""), sharedPref.getString("password", ""));
    }

    /**
     * This method deals with logging in the user by checking the username and password provided by the user in the respective fields.
     * It uses those two values to scan through the users in the database and finds the associated userid and logs the user in.
     * If no user is found with the provided username and password, an error dialogue will be shown.
     *
     * @param view
     */
    //As of right now this is very scuffed. Searching for the username and pword should probably occurr server-side
    public void loginPress(View view) {
        login(textUsername.getText().toString(), textPassword.getText().toString());
    }

    /**
     * This method is called when user wants to register.
     *
     * @param view
     */
    public void registerPress(View view) {
        switchScreen(RegisterActivity.class, null);
    }

    protected void login(String username, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        //This implementation gets the list of all users (which is a terrible way to do this lol) and goes through the list checking if the username and password matches
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url + "/user", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                users = response;
                for (int i = 0; i < users.length(); i++) {
                    try {
                        //Checks that the text supplied in textUsername and textPassword is equal to the "username" and "password" field in the JSONObject at index i of the JSONArray
                        if (username.equals(users.getJSONObject(i).get("username")) && password.equals(users.getJSONObject(i).get("password"))) {
                            //userID = i;
                            //getUserID(url + "/user/" + textUsername.getText().toString());
                            //Switches to MainActivity and sends the associated userId of the user so their relevant data can be retrieved in the next screen
                            loginUserID = users.getJSONObject(i).get("userId").toString();
                            user = users.getJSONObject(i);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("username", user.getString("username"));
                            editor.putString("password", user.getString("password"));
                            editor.apply();
                            switchScreen(MainActivity.class, user.toString());
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setTitle("Error");
                alert.setMessage("No user found with those credentials"); //This error message sucks lmao
                alert.setPositiveButton("OK", null);
                alert.setNegativeButton("", null);
                alert.create().show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setTitle("Error");
                alert.setMessage(error.getMessage());
                alert.setPositiveButton("OK", null);
                alert.setNegativeButton("", null);
                alert.create().show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * This method switches from the current activity to the activity class specified. It also allows for a user to be attached and passed to the next screen.
     * @param activityToSwitch The activity class to be switched to.
     * @param user The user as a JSONObject converted to a string.
     */
    protected void switchScreen(Class activityToSwitch, String user) {
        Intent nextScreen = new Intent(this, activityToSwitch);
        // Adds the id of the user as inputted in the parameters to be able to be sent to another screen in order to keep track of the user that is logged in
        if (user != null) {
            nextScreen.putExtra("user", user);
        }
        startActivity(nextScreen);
    }

    /**
     * Returns user name used to login.
     *
     * @return
     */
    public static String getUsername() {
        try {
            return textUsername.getText().toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static void setUsername(String username) {
        textUsername.setText(username);
    }

    public static String getLoginUserID(){
        return loginUserID;
    }
}
