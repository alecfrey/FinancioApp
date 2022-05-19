package com.example.financio;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.financio.ui.home.AddExpense;
import com.example.financio.ui.social.SocialFragment;

import org.hamcrest.CoreMatchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

//@RunWith(Parameterized.class)
public class SystemTest1 {
/**
    @Parameterized.Parameters(name = "{index}: isInteger({5})={true}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"5", true}, {"0.5", false}, {"13", true}, {"5.2", false}, {"0", true}, {"141412412", true}
        });
    }

    private String input;
    private boolean expected;

    public SystemTest1(String input, boolean expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void testIsIntegerUsingParameterizedTesting() {
        Assert.assertThat(SocialFragment.isInteger(input), CoreMatchers.is(CoreMatchers.equalTo(expected)));
    }
*/


    @Test
    public void testIsInteger() {
        Assert.assertThat(SocialFragment.isInteger("5"), CoreMatchers.is(CoreMatchers.equalTo(true)));
        Assert.assertThat(SocialFragment.isInteger("0.5"), CoreMatchers.is(CoreMatchers.equalTo(false)));
        Assert.assertThat(SocialFragment.isInteger("13"), CoreMatchers.is(CoreMatchers.equalTo(true)));
        Assert.assertThat(SocialFragment.isInteger("5.2"), CoreMatchers.is(CoreMatchers.equalTo(false)));
        Assert.assertThat(SocialFragment.isInteger("0"), CoreMatchers.is(CoreMatchers.equalTo(true)));
        Assert.assertThat(SocialFragment.isInteger("5123414"), CoreMatchers.is(CoreMatchers.equalTo(true)));
    }

    JSONObject jsonAddFriend = new JSONObject();

    @Test
    public void testAddFriend() {
        getJSONRequest();
        //Assert.assertThat(SocialFragment.addFriend(704, jsonAddFriend), CoreMatchers.is(CoreMatchers.equalTo()));

    }

    public void getJSONRequest() {
        String url = "http://coms-309-005.class.las.iastate.edu:8080/user/704";
        RequestQueue queue = Volley.newRequestQueue(null);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    jsonAddFriend.put("username", response.getString("username"));
                    jsonAddFriend.put("username", response.getString("firstName"));
                    jsonAddFriend.put("username", response.getString("lastName"));

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
}
