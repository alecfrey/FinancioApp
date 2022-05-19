package com.example.financio.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.financio.LoginActivity;
import com.example.financio.MainActivity;
import com.example.financio.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Public class AddExpense
 *
 * This fragment is opened from within the Home Fragment when clicking on the plus menu bar item
 * You are prompted to pick from the expense category and then enter how much you spent.
 *
 * After clicking okay, your new expense will be reflected onto the home fragment.
 *
 */
public class AddExpense extends HomeFragment implements View.OnClickListener {

    private Button btnAddExpense;
    private ImageButton imgBtnAddExpenseBack;
    public HomeFragment hf;

    private JSONObject reportedExpenses;
    private JSONObject newUserExpenses;

    private Spinner spnExpenseCategory;
    private EditText etExpenseCost;

    public static double totalSpentRestaurant;
    public static double totalSpentSubscriptions;
    public static double totalSpentEssentials;
    public static double totalSpentGrocery;
    public static double totalSpentGas;
    public static double totalSpentAlcohol;
    public static double totalSpentOther;

    // NEW
    public static double restaurant = 0;
    public static double subscriptions = 0;
    public static double essentials = 0;
    public static double grocery = 0;
    public static double gas = 0;
    public static double alcohol = 0;
    public static double other = 0;

    String url = "http://coms-309-005.class.las.iastate.edu:8080";

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        hf = new HomeFragment();

        if (savedInstanceState != null) {
            totalSpentRestaurant = savedInstanceState.getInt("R");
        }
        super.onCreate(savedInstanceState);

        newUserExpenses = new JSONObject();
        try {
            newUserExpenses.put("resturant", "");
            newUserExpenses.put("subscriptions", "");
            newUserExpenses.put("essentials", "");
            newUserExpenses.put("grocery", "");
            newUserExpenses.put("gas", "");
            newUserExpenses.put("alcohol", "");
            newUserExpenses.put("other", "");
            newUserExpenses.put("expenseId", sharedPref.getString("expenseId", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            totalSpentRestaurant = savedInstanceState.getInt("R");
        }
        super.onViewStateRestored(savedInstanceState);


    }

    /**
     * Resets totals of all category's.
     */
    public static void resetTotals() {
        totalSpentRestaurant = 0;
        totalSpentSubscriptions = 0;
        totalSpentEssentials = 0;
        totalSpentGrocery = 0;
        totalSpentGas = 0;
        totalSpentAlcohol = 0;
        totalSpentOther = 0;
    }

    /**
     * Creates fragment view and all XML views are assigned variables.
     * OnClickListeners are defined in this method.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        spnExpenseCategory = view.findViewById(R.id.spnExpenseName);
        etExpenseCost = view.findViewById(R.id.etExpenseCost);

        Button addExpense = view.findViewById(R.id.btnAddExpense);
        addExpense.setOnClickListener(this);

//        imgBtnAddExpenseBack = view.findViewById(R.id.imgBtnAddExpenseBack);
//        imgBtnAddExpenseBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
//                fr.replace(R.id.home_add_expense, hf);
//                fr.addToBackStack(null);
//                fr.setReorderingAllowed(true);
//                fr.commit();
//            }
//        });

        // When restarting app, fill in values
        if (totalSpentRestaurant == 0 && totalSpentSubscriptions == 0 && totalSpentEssentials == 0 && totalSpentGrocery == 0 && totalSpentGas == 0
                && totalSpentAlcohol == 0 && totalSpentOther == 0) {
          //  if (!tvRestaurant.getText().toString().equals("0.0")) {
        //        totalSpentRestaurant = Double.parseDouble(tvRestaurant.getText().toString());
        //    }
        }

        return view;
    }
    @Override
    public void onClick(View view) {
        String p = spnExpenseCategory.getSelectedItem().toString().toLowerCase(Locale.ROOT);
        if(p.equals("restaurant")) p = "resturant";

        switch (view.getId()) {
            case R.id.btnAddExpense:
                try {
                    newUserExpenses.put(p, etExpenseCost.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

                JsonObjectRequest reportedExpenses = new JsonObjectRequest(Request.Method.POST, url + "/reported_expense", newUserExpenses, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG);
                    }
                });
                requestQueue.add(reportedExpenses);
            case R.id.imgBtnAddExpenseBack:
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.replace(R.id.home_add_expense, hf);
                fr.addToBackStack(null);
                fr.setReorderingAllowed(true);
                fr.commit();
                break;

        }
//        try {
//            if (!etExpenseCost.getText().toString().equals("") && !spnExpenseCategory.toString().equals("")) {
//                String url = "http://coms-309-005.class.las.iastate.edu:8080/expense";
//                postJSONRequest(spnExpenseCategory.getSelectedItem().toString(), etExpenseCost.getText().toString(), url);
//                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
//                fr.replace(R.id.home_add_expense, hf);
//                fr.addToBackStack(null);
//                fr.setReorderingAllowed(true);
//                fr.commit();
//
//            } else {
//                Toast t = Toast.makeText(getContext(), "Enter the expense category and amount.", Toast.LENGTH_SHORT);
//                t.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, t.getXOffset(), t.getYOffset());
//                t.show();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public void back(View view) {
        FragmentTransaction fr = getParentFragmentManager().beginTransaction();
        fr.replace(R.id.home_add_expense, hf);
        fr.addToBackStack(null);
        fr.setReorderingAllowed(true);
        fr.commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void postJSONRequest(String expense, String cost, String url) throws JSONException {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // Update global variable with current expense of that category
        getJSON(expense, cost);

        JSONObject jsonExpense = new JSONObject();
        jsonExpense.put("resturant", totalSpentRestaurant);
        jsonExpense.put("subscriptions", subscriptions);
        jsonExpense.put("essentials", essentials);
        jsonExpense.put("grocery", grocery);
        jsonExpense.put("gas", gas);
        jsonExpense.put("alcohol", alcohol);
        jsonExpense.put("other", other);
        jsonExpense.put("userId", LoginActivity.getLoginUserID());

        // Fixes ghost values
        double sum = restaurant + subscriptions + essentials + grocery + gas + alcohol + other;
        if (sum == 0) {
            return;
        }

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonExpense, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void getJSON(String expense, String cost) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, "http://coms-309-005.class.las.iastate.edu:8080/expense", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray expenses = response;
                for (int i = 0; i < expenses.length(); i++) {
                    try {
                        String userObject = expenses.getJSONObject(i).get("user").toString();
                        int userIDIndex = userObject.indexOf("userId");
                        String userIDSubstring = userObject.substring(userIDIndex);
                        String userID = userIDSubstring.replaceAll("[^0-9]+", "");

                        if (userID.equals(LoginActivity.getLoginUserID())) {
                           // tvRestaurant.setText(expenses.getJSONObject(i).getDouble("resturant") + "");
                        //    tvSubscriptions.setText(expenses.getJSONObject(i).getDouble("subscriptions") + "");
                        //    tvEssentials.setText(expenses.getJSONObject(i).getDouble("essentials") + "");
                         //   tvGrocery.setText(expenses.getJSONObject(i).getDouble("grocery") + "");
                         ///   tvGas.setText(expenses.getJSONObject(i).getDouble("gas") + "");
                         //   tvAlcohol.setText(expenses.getJSONObject(i).getDouble("alcohol") + "");
                         //   tvOther.setText(expenses.getJSONObject(i).getDouble("other") + "");
                        }
                    } catch (Exception e) {
                    }
                }

                if (expense.equals("Restaurant")) {
                    restaurant += Double.parseDouble(cost);
                } else if (expense.equals("Subscriptions")) {
                    subscriptions += Double.parseDouble(cost);
                } else if (expense.equals("Essentials")) {
                    essentials += Double.parseDouble(cost);
                } else if (expense.equals("Grocery")) {
                    grocery += Double.parseDouble(cost);
                } else if (expense.equals("Gas")) {
                    gas += Double.parseDouble(cost);
                } else if (expense.equals("Alcohol")) {
                    alcohol += Double.parseDouble(cost);
                } else if (expense.equals("Other")) {
                    other += Double.parseDouble(cost);
                }

                totalSpentRestaurant = restaurant;
                totalSpentSubscriptions = subscriptions;
                totalSpentEssentials = essentials;
                totalSpentGrocery = grocery;
                totalSpentGas = gas;
                totalSpentAlcohol = alcohol;
                totalSpentOther = other;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
