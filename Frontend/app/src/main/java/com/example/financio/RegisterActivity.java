package com.example.financio;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * public class RegisterActivity
 *
 * This class extends LoginActivity and is used when user is creating their account.
 */
public class RegisterActivity extends LoginActivity {

    //Every TextView field
    protected TextView tFname, tLname, tAddr, tCity, tZip, tEmail, tUsername, tPassword, tGender;
    //Contents of every TextView field as strings
    protected static String sFname = "", sLname = "", sAddr = "", sCity = "", sZip = "", sEmail, sUsername = "", sPassword = "", sGender = "";
    protected Spinner state;
    private static int registerUserID;
    public boolean isUnique = true;
    private int intConfirm = 0;
    RequestQueue requestQueue;

    /**
     * Assigns all variables their xml elements and has onClickListener for back button.
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Assign TextViews
        tFname = findViewById(R.id.field_fname);
        tLname = findViewById(R.id.field_lname);
        tAddr = findViewById(R.id.field_address);
        tCity = findViewById(R.id.field_city);
        state = findViewById(R.id.spinner_state);
        tZip = findViewById(R.id.field_zip);
        tEmail = findViewById(R.id.field_email);
        tUsername = findViewById(R.id.field_uname);
        tPassword = findViewById(R.id.field_pword);
        tGender = findViewById(R.id.field_gender);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this.getApplicationContext());
    }

    /**
     * This method creates a user in the database using the given information filled out in all the textfields.
     * The information gathered includes name, password, location, date created, and email.
     * @param view
     * @throws JSONException
     */
    public void registerAccount(View view) throws JSONException {
        //Assign Strings the value entered in the TextFields
        sFname = tFname.getText().toString();
        sLname = tLname.getText().toString();
        sAddr = tAddr.getText().toString();
        sCity = tCity.getText().toString();
        sZip = tZip.getText().toString();
        sEmail = tEmail.getText().toString();
        sUsername = tUsername.getText().toString();
        sPassword = tPassword.getText().toString();
        sGender = tGender.getText().toString();

        //Checks if the username and password both contain spaces and does not register if they do.
        if (sUsername.contains(" ") || sPassword.contains(" ") || sEmail.contains(" ")) {
            makeToast("No spaces allowed in username password, or email.");
            return;
        }

        //Checks if the either username or password field is empty and does not register if they are.
        if (sUsername.isEmpty() || sPassword.isEmpty() || sEmail.isEmpty()) {
            makeToast("Enter username, password, and email.");
            return;
        }

        //Calls method to check if username is unique.
        //If username is unique (not taken), register the user in the database and log the user in
        checkIfUniqueUsername(sUsername);
    }

    /**
     * Creates toast with given string.
     * @param msg
     */
    private void makeToast(String msg) {
        Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        t.show();
    }

    /**
     * Checks if username is unique, registers the user, and logs them in if username is unique. Otherwise displays error message.
     * @param username The username to be tested and logged in with
     */
    public void checkIfUniqueUsername(String username) throws JSONException{
        //This implementation gets the list of all users (which is a terrible way to do this lol) and goes through the list checking if the username and password matches
        try {
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url + "/user", null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            if (username.equals(response.getJSONObject(i).getString("username"))) {
                                makeToast("Username has already been registered");
                                return;
                            }

                            if (sEmail.equals(response.getJSONObject(i).getString("email"))) {
                                makeToast("Email has already been registered");
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    try {volleyPostUser();}
                    catch (JSONException e) { e.printStackTrace(); }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * A helper method to keep checkIfUniqueUsername cleaner.
     * Adds provided user info to a JSONObject then sends that JSONObject to the server to add to the database.
     * If successful, the user is logged in with their credentials.
     * @throws JSONException
     */
    private void volleyPostUser() throws JSONException {
        JSONObject newUserInfo = new JSONObject();

        //there's probably a better way to do all of this
        //Puts all the info that the user has inputted in each field into a JSONObject
        newUserInfo.put("username", sUsername);
        newUserInfo.put("password", sPassword);
        newUserInfo.put("address", sAddr);
        newUserInfo.put("city", sCity);
        newUserInfo.put("state", state.getSelectedItem().toString());
        newUserInfo.put("firstName", sFname);
        newUserInfo.put("middleInitial", "");
        newUserInfo.put("picturePath", "");
        newUserInfo.put("lastName", sLname);
        newUserInfo.put("email", sEmail);
        newUserInfo.put("gender", sGender);

        //Checks that the zip code is the numbers 1-9.
        if (sZip.matches("[0-9]+")) {
            newUserInfo.put("zip", Integer.parseInt(sZip));
        } else {
            newUserInfo.put("zip", 0);
        }

        //Sends the new user's registration details to be added to the Users database and automatically logs the user in.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url + "/user", newUserInfo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //registerUserID = response.getInt("userId");
                            //System.out.println(response.toString());
                            newUserInfo.put("userId", response.getString("userId"));
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("userId", response.getString("userId"));
                            editor.apply();
                            makeToast("Registration Successful. User ID: " + response.getInt("userId"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        LoginActivity.setUsername(sUsername);

                        // Create expense
                        switchScreen(ExpenseActivity.class, newUserInfo.toString());

                        //login(sUsername, sPassword);
                    }
                }, new Response.ErrorListener(){
            public void onErrorResponse(VolleyError error){
                AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                alert.setTitle("Error");
                alert.setMessage(error.getMessage());
                alert.setPositiveButton("OK", null);
                alert.setNegativeButton("", null);
                alert.create().show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static int getRegisterUserID() {
        return registerUserID;
    }
}