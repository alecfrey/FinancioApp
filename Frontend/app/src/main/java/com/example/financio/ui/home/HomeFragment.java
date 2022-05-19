package com.example.financio.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.financio.LoginActivity;
import com.example.financio.R;
import com.example.financio.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Public class HomeFragment
 *
 * One of the 3 main fragments and holds all the expense data information.
 * Has ability to add new expense as well as delete recent expenses.
 */
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    protected JSONObject user;

    SharedPreferences sharedPref;

    // Implement in future (add new ET whenever new category is made)
    private ArrayList<EditText> etArray = new ArrayList<>();

    private TextView tvTotalSpent;
    private TextView tvRestaurant;
    private TextView tvSubscriptions;
    private TextView tvEssentials;
    private TextView tvGrocery;
    private TextView tvGas;
    private TextView tvAlcohol;
    private TextView tvOther;

    public Fragment frAddExpense;
    private double totalSpent = 0;
    private Boolean boolEnabled = true;
    public LinkedList<Integer> expenseList;

    private MenuItem miAddExpenseCategory;
    private MenuItem miSaveNewlySpent;
    private MenuItem miResetExpenses;

    public static double totalSpentRestaurant;
    public static double totalSpentSubscriptions;
    public static double totalSpentEssentials;
    public static double totalSpentGrocery;
    public static double totalSpentGas;
    public static double totalSpentAlcohol;
    public static double totalSpentOther;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        expenseList = new LinkedList<Integer>();
        //addExpenseFragment = (AddExpense) getChildFragmentManager().findFragmentById(R.id.home_add_expense);
        frAddExpense = new AddExpense();
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            frAddExpense = getChildFragmentManager().getFragment(savedInstanceState, "R");
        }
        super.onCreate(savedInstanceState);

        //Retrieves the user who has logged in from the login screen
        try {
            user = new JSONObject(getActivity().getIntent().getStringExtra("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvTotalSpent = root.findViewById(R.id.tvTotalSpent);
        tvRestaurant = root.findViewById(R.id.tvExpenseCostRestaurant);
        tvSubscriptions = root.findViewById(R.id.tvExpenseCostSubscriptions);
        tvEssentials = root.findViewById(R.id.tvExpenseCostEssentials);
        tvGrocery = root.findViewById(R.id.tvExpenseCostGrocery);
        tvGas = root.findViewById(R.id.tvExpenseCostGas);
        tvAlcohol = root.findViewById(R.id.tvExpenseCostAlcohol);
        tvOther = root.findViewById(R.id.tvExpenseCostOther);

        getExpenseID();
        //getJSONRequest();

        return root;
    }

    /**
     * Takes a string and finds value by parsing.
     *
     * @param s
     * @return
     */
    private int valueOfIfInteger(String s) {
        try{
            int num = Integer.parseInt(s);
            return num;
            // is an integer!

        } catch (NumberFormatException e) {
            // not an integer!
            return  0;
        }
    }

    /**
     * Menu items and OnMenuClickListeners are defined.
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        //inflater.inflate(R.menu.top_bar_menu, menu);

        // hide menu item
        menu.findItem(R.id.miLeaderboards).setVisible(false);
        menu.findItem(R.id.miAddFriend).setVisible(false);
        menu.findItem(R.id.miLogout).setVisible(false);

        miAddExpenseCategory = menu.findItem(R.id.miAddCategory);
        miAddExpenseCategory.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //miAddExpenseCategory.setEnabled(false);
                FragmentTransaction fr = getChildFragmentManager().beginTransaction();
                fr.add(R.id.home_add_expense, frAddExpense);
                fr.addToBackStack(null);
                fr.setReorderingAllowed(true);
                fr.commit();
                return true;
            }
        });

        miResetExpenses = menu.findItem(R.id.miResetExpenses);
        miResetExpenses.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AddExpense.resetTotals();
                tvRestaurant.setText("0");
                tvSubscriptions.setText("0");
                tvEssentials.setText("0");
                tvGrocery.setText("0");
                tvGas.setText("0");
                tvAlcohol.setText("0");
                tvOther.setText("0");
                totalSpent = 0;
                tvTotalSpent.setText(String.format("$%,.2f", totalSpent));
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // handle menu item clicks
        if (item.getItemId() == R.id.miLeaderboards) {
            Toast.makeText(getActivity(), "Leaderboards", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * getJSONRequest method used for removing recent expense.
     *
     * Textview totals are then updated.
     *
     */
    public void getJSONRequest(int expenseID) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://coms-309-005.class.las.iastate.edu:8080/expense/" + expenseID + "/reported", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    totalSpentRestaurant = response.getDouble("resturant");
                    totalSpentSubscriptions = response.getDouble("subscriptions");
                    totalSpentEssentials = response.getDouble("essentials");
                    totalSpentGrocery = response.getDouble("grocery");
                    totalSpentGas = response.getDouble("gas");
                    totalSpentAlcohol = response.getDouble("alcohol");
                    totalSpentOther = response.getDouble("other");

                    tvRestaurant.setText(totalSpentRestaurant + "");
                    tvSubscriptions.setText(totalSpentSubscriptions + "");
                    tvEssentials.setText(totalSpentEssentials + "");
                    tvGrocery.setText(totalSpentGrocery + "");
                    tvGas.setText(totalSpentGas + "");
                    tvAlcohol.setText(totalSpentAlcohol + "");
                    tvOther.setText(totalSpentOther + "");

                    double total = totalSpentRestaurant + totalSpentSubscriptions + totalSpentEssentials + totalSpentGrocery + totalSpentGas + totalSpentAlcohol + totalSpentOther;
                    tvTotalSpent.setText(String.format("$%,.2f", (total)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(jsonObjectRequest);
    }

    protected void getExpenseID() {
        sharedPref.getString("expenseId", "");
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        //This implementation gets the list of all users (which is a terrible way to do this lol) and goes through the list checking if the username and password matches
//        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,  "http://coms-309-005.class.las.iastate.edu:8080/expense", null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                JSONArray budgets = response;
//                for (int i = 0; i < budgets.length(); i++) {
//                    try {
//                        String userObject = budgets.getJSONObject(i).get("user").toString();
//                        int userIDIndex = userObject.indexOf("userId");
//                        String userIDSubstring = userObject.substring(userIDIndex);
//                        String userID = userIDSubstring.replaceAll("[^0-9]+", "");
//
//                        if (LoginActivity.getLoginUserID().equals(userID)) {
//                            JSONObject object = budgets.getJSONObject(i);
//                            getJSONRequest(object.getInt("id"));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        totalSpent = 0;
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        totalSpent = 0;
    }

    @Override
    public void onPause() {
        super.onPause();
    //    saveProfileData();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //getChildFragmentManager().putFragment(outState, "R", frAddExpense);
        super.onSaveInstanceState(outState);
    }
}