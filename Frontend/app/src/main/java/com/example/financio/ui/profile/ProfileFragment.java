package com.example.financio.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.android.volley.toolbox.Volley;
import com.example.financio.LoginActivity;
import com.example.financio.*;
import com.example.financio.databinding.FragmentProfileBinding;
import com.example.financio.ui.home.AddExpense;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Public class ProfileFragment
 *
 * One of the 3 main fragment classes.
 * This class allows you to see your userID, name, gender, age, and add a profile picture.
 * Also you can see your previous months expenses, and see the 2 months compared.
 */
public class ProfileFragment extends Fragment {

    private JSONObject user;
    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;

    private TextView tvUsername;
    private TextView tvGender;
    private TextView tvState;
    private TextView tvAddPicMessage;

    private ImageButton imgCompareMonths;
    private ImageButton imgProfilePicture;

    private int PICK_IMAGE = 1;
    private boolean editProfileToggle = true;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String spUserName = "userName";
    public static final String spUserAge = "userAge";
    public static final String spSpnSelection = "spnSelection";
    private String loadedName = "";
    private String loadedAge = "";
    private int loadedSpnSelection = 0;

    private static TextView tvProfileRestaurant;
    private static TextView tvProfileSubscriptions;
    private static TextView tvProfileEssentials;
    private static TextView tvProfileGrocery;
    private static TextView tvProfileGas;
    private static TextView tvProfileAlcohol;
    private static TextView tvProfileOther;
    private TextView tvProfileTotalBudget;
    private TextView tvUserID;

    // Created Budget values
    public static double restaurantBudget;
    public static double subscriptionsBudget;
    public static double essentialsBudget;
    public static double groceryBudget;
    public static double gasBudget;
    public static double alcoholBudget;
    public static double otherBudget;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieves the user who has logged in from the login screen
        try {
            user = new JSONObject(getActivity().getIntent().getStringExtra("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setHasOptionsMenu(true);
    }

    /**
     *
     * Creates fragment view and all XML views are assigned variables.
     * OnClickListeners are defined in this method.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvProfileRestaurant = root.findViewById(R.id.tvProfileRestaurant);
        tvProfileSubscriptions = root.findViewById(R.id.tvProfileSubscriptions);
        tvProfileEssentials = root.findViewById(R.id.tvProfileEssentials);
        tvProfileGrocery = root.findViewById(R.id.tvProfileGrocery);
        tvProfileGas = root.findViewById(R.id.tvProfileGas);
        tvProfileAlcohol = root.findViewById(R.id.tvProfileAlcohol);
        tvProfileOther = root.findViewById(R.id.tvProfileOther);
        tvProfileTotalBudget = root.findViewById(R.id.tvProfileTotalBudget);
        tvUserID = root.findViewById(R.id.tvUserID);
        tvUsername = root.findViewById(R.id.tvProfileUsername);
        tvGender = root.findViewById(R.id.tvProfileSetGender);
        tvState = root.findViewById(R.id.tvProfileSetState);
        tvAddPicMessage = root.findViewById(R.id.tvAddPictureMessage);

        loadInBudget();

        //toggleEditSpinner(spnGender, false);

        // IMPORTANT!~!!!!!!!!!!!!
        //tvUserID.setText("User ID: " + LoginActivity.getUserID());

        try {
            tvUserID.setText("User ID: " + user.get("userId"));

            tvGender.setText(user.get("gender").toString());
            tvState.setText(user.get("state").toString());
            if (tvState.getText().toString().equals("") || tvGender.getText().toString().equals("")) {
                tvState.setText("null");
                tvGender.setText("null");
            }

            if (!user.get("firstName").toString().isEmpty() && !user.get("lastName").toString().isEmpty()) {
                tvUsername.setText(user.get("firstName")+ " " + user.get("lastName"));
            } else if (!user.get("firstName").toString().isEmpty()) {
                tvUsername.setText(user.get("firstName").toString());
            } else {
                tvUsername.setText(user.get("username").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imgCompareMonths = root.findViewById(R.id.imgCompareMonths);
        imgCompareMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_compare, new ProfileCompareMonths());
                fr.addToBackStack(null);
                fr.setReorderingAllowed(true);
                fr.commit();
            }
        });

        imgProfilePicture = root.findViewById(R.id.imgProfilePicture);
        imgProfilePicture.setAdjustViewBounds(true);
        imgProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageGallery();
            }
        });

        //loadProfileData();
        //updateViews();
        return root;
    }

    private void loadInBudget() {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            //This implementation gets the list of all users (which is a terrible way to do this lol) and goes through the list checking if the username and password matches
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,  "http://coms-309-005.class.las.iastate.edu:8080/expense", null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    JSONArray budgets = response;
                    for (int i = 0; i < budgets.length(); i++) {
                        try {
                            String userObject = budgets.getJSONObject(i).get("user").toString();
                            int userIDIndex = userObject.indexOf("userId");
                            String userIDSubstring = userObject.substring(userIDIndex);
                            String userID = userIDSubstring.replaceAll("[^0-9]+", "");

                            if (LoginActivity.getLoginUserID().equals(userID)) {
                                JSONObject budget = budgets.getJSONObject(i);
                                restaurantBudget = budget.getDouble("resturant");
                                subscriptionsBudget = budget.getDouble("subscriptions");
                                essentialsBudget = budget.getDouble("essentials");
                                groceryBudget = budget.getDouble("grocery");
                                gasBudget = budget.getDouble("gas");
                                alcoholBudget = budget.getDouble("alcohol");
                                otherBudget = budget.getDouble("other");
                                double totalBudget = restaurantBudget + subscriptionsBudget + essentialsBudget + groceryBudget + gasBudget + alcoholBudget + otherBudget;

                                tvProfileRestaurant.setText("Restaurant: " + String.format("$%,.2f", restaurantBudget));
                                tvProfileSubscriptions.setText("Subscriptions: " + String.format("$%,.2f", subscriptionsBudget));
                                tvProfileEssentials.setText("Essentials: " + String.format("$%,.2f", essentialsBudget));
                                tvProfileGrocery.setText("Grocery: " + String.format("$%,.2f", groceryBudget));
                                tvProfileGas.setText("Gas: " + String.format("$%,.2f", gasBudget));
                                tvProfileAlcohol.setText("Alcohol: " + String.format("$%,.2f", alcoholBudget));
                                tvProfileOther.setText("Other: " + String.format("$%,.2f", otherBudget));
                                tvProfileTotalBudget.setText("Total: " + String.format("$%,.2f", totalBudget));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            requestQueue.add(jsonObjectRequest);
    }

    /**
     * Allows you to pick a profile picture from gallery.
     */
    private void pickImageGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        tvAddPicMessage.setAlpha(0.0f);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            Uri uri = data.getData();
            imgProfilePicture.setImageURI(uri);
        }
    }

    /**
     * Menu items and OnMenuClickListeners are defined.
     * Allows user to log out and return to log in screen via menu item.
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem logout = menu.findItem(R.id.miLogout);
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent backToLogin = new Intent(getContext(), LoginActivity.class);
                startActivity(backToLogin);
                return false;
            }
        });

        // hide menu item
        menu.findItem(R.id.miAddCategory).setVisible(false);
        menu.findItem(R.id.miLeaderboards).setVisible(false);
        menu.findItem(R.id.miAddFriend).setVisible(false);
        menu.findItem(R.id.miResetExpenses).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Saves data to local device.
     * Used for testing.

    public void saveProfileData() {
        SharedPreferences settings = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(spUserName, etUsername.getText().toString());
        editor.putString(spUserAge, etAge.getText().toString());
        editor.putInt(spSpnSelection, spnGender.getSelectedItemPosition());

        editor.apply();
    }*/

    /**
     * Loads back local storage data.
     * Used for testing.

    public void loadProfileData() {
        SharedPreferences settings = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // null is default value
        loadedName = settings.getString(spUserName, null);
        loadedAge = settings.getString(spUserAge, null);
        loadedSpnSelection = settings.getInt(spSpnSelection,0);
    }
     */

    /**
     * Updates views with saved data.
     * Used for testing.

    public void updateViews() {
        etUsername.setText(loadedName);
        etAge.setText(loadedAge);
        spnGender.setSelection(loadedSpnSelection);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}