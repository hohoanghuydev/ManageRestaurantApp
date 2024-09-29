package com.example.managerestaurantapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.adapters.VPAdapterTable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ActivityTable extends AppCompatActivity {

    TabLayout tabLayoutFloor;
    ViewPager2 vpTable;
    VPAdapterTable adapterTable;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        toolbar = findViewById(R.id.toolbarTable);
        setSupportActionBar(toolbar);

        tabLayoutFloor = (TabLayout) findViewById(R.id.tabLayoutFloor);
        vpTable = (ViewPager2) findViewById(R.id.vpTable);

        adapterTable = new VPAdapterTable(this);
        vpTable.setAdapter(adapterTable);

        new TabLayoutMediator(tabLayoutFloor, vpTable, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Tầng Trệt");
                        break;
                    case 1:
                        tab.setText("Tầng 1");
                        break;
                    default:
                        break;
                }
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_employee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemSelected = item.getItemId();
        if(itemSelected == R.id.itemLogout) {
            Intent intent = new Intent(ActivityTable.this, DangNhapActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}