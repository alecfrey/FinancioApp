package com.example.financio.ui.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.financio.LoginActivity;
import com.example.financio.R;
import com.example.financio.ui.home.AddExpense;
import com.example.financio.ui.home.HomeFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileCompareMonths extends ProfileFragment {

    ArrayList barListTopBudget;
    ArrayList barListBottomCurrent;
    private ImageButton btnBackToProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_compare, container, false);

        btnBackToProfile = view.findViewById(R.id.imgBtnCompareBack);
        btnBackToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_compare, new ProfileFragment());
                fr.addToBackStack(null);
                fr.setReorderingAllowed(true);
                fr.commit();
            }
        });

        BarChart previousMonthChart = view.findViewById(R.id.bcCompareTop);
        BarChart currentMonthChart = view.findViewById(R.id.bcCompareBottom);
        getData();

        BarDataSet barDataSetPrevious = new BarDataSet(barListTopBudget, "Targeted Budget");
        BarDataSet barDataSetCurrent = new BarDataSet(barListBottomCurrent, "Current Expenses");

        BarData barDataPrevious = new BarData(barDataSetPrevious);
        BarData barDataCurrent = new BarData(barDataSetCurrent);

        previousMonthChart.setData(barDataPrevious);
        currentMonthChart.setData(barDataCurrent);

        barDataSetPrevious.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSetCurrent.setColors(ColorTemplate.COLORFUL_COLORS);

        barDataSetPrevious.setValueTextColor(Color.BLACK);
        barDataSetCurrent.setValueTextColor(Color.BLACK);

        barDataSetPrevious.setValueTextSize(16f);
        barDataSetCurrent.setValueTextSize(16f);

        Description d = new Description();
        d.setText("");
        previousMonthChart.getDescription().setEnabled(true);
        previousMonthChart.setDescription(d);
        currentMonthChart.getDescription().setEnabled(true);
        currentMonthChart.setDescription(d);

        return view;
    }

    private void getData() {
        barListTopBudget = new ArrayList();
        barListTopBudget.add(new BarEntry(2f, (int) restaurantBudget));
        barListTopBudget.add(new BarEntry(3f, (int) subscriptionsBudget));
        barListTopBudget.add(new BarEntry(4f, (int) essentialsBudget));
        barListTopBudget.add(new BarEntry(5f, (int) groceryBudget));
        barListTopBudget.add(new BarEntry(6f, (int) gasBudget));
        barListTopBudget.add(new BarEntry(7f, (int) alcoholBudget));
        barListTopBudget.add(new BarEntry(8f, (int) otherBudget));

        barListBottomCurrent = new ArrayList();
        barListBottomCurrent.add(new BarEntry(2f, (int) HomeFragment.totalSpentRestaurant));
        barListBottomCurrent.add(new BarEntry(3f, (int) HomeFragment.totalSpentSubscriptions));
        barListBottomCurrent.add(new BarEntry(4f, (int) HomeFragment.totalSpentEssentials));
        barListBottomCurrent.add(new BarEntry(5f, (int) HomeFragment.totalSpentGrocery));
        barListBottomCurrent.add(new BarEntry(6f, (int) HomeFragment.totalSpentGas));
        barListBottomCurrent.add(new BarEntry(7f, (int) HomeFragment.totalSpentAlcohol));
        barListBottomCurrent.add(new BarEntry(8f, (int) HomeFragment.totalSpentOther));
    }
}