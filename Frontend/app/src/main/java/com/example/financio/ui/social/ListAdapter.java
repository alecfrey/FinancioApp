package com.example.financio.ui.social;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.financio.*;

import java.util.ArrayList;

/**
 * Public class ListAdapter
 * Extends ArrayAdapter<User>
 * Used for the listView of Users on the Social Fragment.
 */
public class ListAdapter extends ArrayAdapter<User> {

    private ArrayList<User> friendsList;
    private Context context;

    /**
     * Constructs ListAdapter with given context and userArrayList.
     * @param context
     * @param userArrayList
     */
    public ListAdapter(Context context, ArrayList<User> userArrayList) {
        super(context, R.layout.list_item, userArrayList);
        this.context = context;
        friendsList = userArrayList;
    }

    /**
     * Gets user at position and assigns textview with the appropriate info for that user.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            ImageView imgFriendPicture = convertView.findViewById(R.id.imgFriendPicture);
            TextView tvFullName = convertView.findViewById(R.id.tvFriendName);
            TextView tvUserLocation = convertView.findViewById(R.id.tvSocialLocation);
            TextView tvSocialUsername = convertView.findViewById(R.id.tvSocialUsername);

            //imgFriendPicture.setImageResource(user.);
            tvFullName.setText(friendsList.get(position).getFullName());
            tvUserLocation.setText(friendsList.get(position).getUserLocation());
            tvSocialUsername.setText(friendsList.get(position).getUsername());

            ImageView imgRemoveFriend = convertView.findViewById(R.id.imgRemoveFriend);
            imgRemoveFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeToast("Removed: " + SocialFragment.getFriend(position));
                    SocialFragment.removeFriend(position);
                }
            });
        }
        return convertView;
    }

    private void makeToast(String s) {
        Toast t = Toast.makeText(getContext(), s, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, t.getXOffset(), t.getYOffset());
        t.show();
    }
}
