package com.example.managerestaurantapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.utils.Util;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemSelected = item.getItemId();
        if(itemSelected == R.id.itemDish) {
            Intent intentManage = new Intent(AdminActivity.this, MonAnActivity.class);
            startActivity(intentManage);
            return true;
        } else if (itemSelected == R.id.itemCategory) {
            Intent intentManage = new Intent(AdminActivity.this, LoaiMonAnActivity.class);
            startActivity(intentManage);
            return true;
        } else if (itemSelected == R.id.itemTable) {
            Intent intentManage = new Intent(AdminActivity.this, DinningTableMainActivity.class);
            startActivity(intentManage);
            return true;
        } else if (itemSelected == R.id.itemCustomer) {
            Intent intentManage = new Intent(AdminActivity.this, CustomerMainActivity.class);
            startActivity(intentManage);
            return true;
        } else if (itemSelected == R.id.itemRevenue) {
            Intent intentManage = new Intent(AdminActivity.this, RevenueActivity.class);
            startActivity(intentManage);
            return true;
        } else if(itemSelected == R.id.itemLogout) {
            finish();
            return true;
        } else {
            return false;
        }
    }
}