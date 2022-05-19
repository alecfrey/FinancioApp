package com.example.financio;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ExpenseActivity extends RegisterActivity {

    private JSONObject user;

    public EditText restaurantBudget;
    public EditText subscriptionsBudget;
    public EditText essentialsBudget;
    public EditText groceryBudget;
    public EditText gasBudget;
    public EditText alcoholBudget;
    public EditText otherBudget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        restaurantBudget = findViewById(R.id.etExpenseActivityRestaurant);
        subscriptionsBudget = findViewById(R.id.etExpenseActivitySubscriptions);
        essentialsBudget = findViewById(R.id.etExpenseActivityEssentials);
        groceryBudget = findViewById(R.id.etExpenseActivityGrocery);
        gasBudget = findViewById(R.id.etExpenseActivityGas);
        alcoholBudget = findViewById(R.id.etExpenseActivityAlcohol);
        otherBudget = findViewById(R.id.etExpenseActivityOther);

        try {
            user = new JSONObject(getIntent().getStringExtra("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void confirmBudget(View view) throws JSONException {
            try {
                volleyPostExpense();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            login(user.getString("username"), user.getString("password"));
        }

    private void volleyPostExpense() throws JSONException {
        JSONObject newExpenseInfo = new JSONObject();

        //there's probably a better way to do all of this
        //Puts all the info that the user has inputted in each field into a JSONObject
        newExpenseInfo.put("resturant", restaurantBudget.getText().toString());
        newExpenseInfo.put("subscriptions", subscriptionsBudget.getText().toString());
        newExpenseInfo.put("essentials", essentialsBudget.getText().toString());
        newExpenseInfo.put("grocery", groceryBudget.getText().toString());
        newExpenseInfo.put("gas", gasBudget.getText().toString());
        newExpenseInfo.put("alcohol", alcoholBudget.getText().toString());
        newExpenseInfo.put("other", otherBudget.getText().toString());
        newExpenseInfo.put("userId", user.getString("userId"));

        //Sends the new user's registration details to be added to the Users database and automatically logs the user in.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url + "/expense", newExpenseInfo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast t = null;
                        try {
                            t = Toast.makeText(getApplicationContext(), "Budget successfully created. Expense ID: " + response.getString("expenseId"), Toast.LENGTH_LONG);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("expenseId", response.getString("expenseId"));
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        t.show();
                    }
                }, new Response.ErrorListener(){
            public void onErrorResponse(VolleyError error){
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
