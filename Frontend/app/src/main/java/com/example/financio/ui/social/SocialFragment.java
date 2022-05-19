package com.example.financio.ui.social;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.financio.*;
import com.example.financio.databinding.FragmentSocialBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Public class SocialFragment
 * One of the 3 main fragments.
 *
 * Allows you to add a friend via their userID, and chat with them.
 */
public class SocialFragment extends Fragment {

    private SocialViewModel socialViewModel;
    private FragmentSocialBinding binding;
    private ImageView imgFriendPic;
    private TextView tvFriendName;

    private TextView tvSocialLocation;
    private TextView tvLastMessageTime;
    private static ListView listView;
    private static ArrayList<User> friends;
    private static ListAdapter adapter;
    private EditText etFriendName;
    private ImageButton imgAddFriend;

    public static User selectedUser = null;
    public static String selectedUsername = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
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
        socialViewModel = new ViewModelProvider(this).get(SocialViewModel.class);

        binding = FragmentSocialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imgFriendPic = root.findViewById(R.id.imgFriendPicture);
        tvFriendName = root.findViewById(R.id.tvFriendName);
        tvSocialLocation = root.findViewById(R.id.tvSocialLocation);
        tvLastMessageTime = root.findViewById(R.id.tvSocialUsername);

        etFriendName = root.findViewById(R.id.etEnterUserID);
        imgAddFriend = root.findViewById(R.id.imgAddFriend);
        listView = root.findViewById(R.id.lvFriends);
        listView.setEnabled(true);

        friends = new ArrayList<User>();

        // Open Chat -- WebSockets
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentTransaction fr = getChildFragmentManager().beginTransaction();
                fr.replace(R.id.social_chat, new ChatSocket());
                fr.addToBackStack(null);
                fr.setReorderingAllowed(true);
                fr.commit();

                selectedUser = (User) listView.getItemAtPosition(i);
                listView.setEnabled(false);
            }
        });

        adapter = new ListAdapter(getContext(), friends);
        listView.setAdapter(adapter);

        imgAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = etFriendName.getText().toString();
                if (userID == null || userID.length() == 0 || !isInteger(userID)) {
                    makeToast("Enter a User ID.");

                } else {
                    userID = userID.replaceFirst("^\\s+", "");
                    getUserJSON(tvFriendName, tvSocialLocation, userID);
                    closeKeyboard(view, getContext());
                    etFriendName.setText("");
                }
            }
        });

        return root;
    }

    /**
     * Closes keyboard after sending message.
     * @param v
     * @param c
     */
    private void closeKeyboard(View v, Context c) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * Checks if string is an integer.
     * @param s
     * @return
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Gets user at specific index of friends array list.
     * @param index
     * @return
     */
    public User getUser(int index) {
        return friends.get(index);
    }

    /**
     * Adds a friend via userID to listView.
     * @param userID
     * @param j
     */
    public void addFriend(int userID, JSONObject j) {
        User newUser = new User(userID);
        friends.add(newUser);

        try {
            if (!j.getString("firstName").isEmpty() && !j.getString("lastName").isEmpty()) {
                newUser.setFullName(j.getString("firstName") + " " + j.getString("lastName"));
                //selectedUsername = j.getString("firstName") + " " + j.getString("lastName");
            } else if (!j.getString("firstName").isEmpty()) {
                newUser.setFullName(j.getString("firstName"));
            } else {
                newUser.setFullName(j.getString("username"));
            }
            newUser.setUsername(j.getString("username"));
            newUser.setUserLocation(j.getString("city"), j.getString("state"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        makeToast("Added: " + newUser.getFullName());
        listView.setAdapter(adapter);
    }

    /**
     * Removes friend from list at given index.
     * @param index
     */
    public static void removeFriend(int index) {
        friends.remove(index);
        listView.setAdapter(adapter);
    }

    public static String getFriend(int index) {
        return friends.get(index).getFullName();
    }

    /**
     * Takes a given userID, and calls a getJSONRequest to add friend to list.
     * @param userName
     * @param userLocation
     * @param userID
     */
    public void getUserJSON(TextView userName, TextView userLocation, String userID) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String url = "http://coms-309-005.class.las.iastate.edu:8080/user/" + userID;

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                addFriend(Integer.parseInt(userID), response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                makeToast("Error: Invalid User ID");
            }
        });
        queue.add(jsonObjectRequest);
    }

    /**
     * Makes an andriod toast.
     * @param s
     */
    private void makeToast(String s) {
        Toast t = Toast.makeText(getContext(), s, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, t.getXOffset(), t.getYOffset());
        t.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        //inflater.inflate(R.menu.top_bar_menu, menu);

        // hide menu item
        menu.findItem(R.id.miLeaderboards).setVisible(true);
        menu.findItem(R.id.miAddCategory).setVisible(false);
        menu.findItem(R.id.miAddFriend).setVisible(false);
        menu.findItem(R.id.miLeaderboards).setVisible(false);
        menu.findItem(R.id.miResetExpenses).setVisible(false);
        menu.findItem(R.id.miLogout).setVisible(false);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}