package com.example.managerestaurantapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;

import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.adapters.AdapterTableDish;
import com.example.managerestaurantapp.models.Message;
import com.example.managerestaurantapp.models.Payment;
import com.example.managerestaurantapp.models.TableDish;
import com.example.managerestaurantapp.models.TableService;
import com.example.managerestaurantapp.services.ApiService;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCart extends AppCompatActivity {

    ImageButton imgBack;
    ListView lvCart;
    Button btnCheckout;
    TextView tvTotal;

    AdapterTableDish adapterOrder;
    List<TableDish> orders = new ArrayList<>();
    TableService service = new TableService();
    int tableId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intentTable = getIntent();
        tableId = intentTable.getIntExtra("tableId", 0);

        if(tableId == 0){
            Toast.makeText(this, "Error table invalid", Toast.LENGTH_SHORT).show();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Hóa đơn bàn " + tableId);

        ActionMenuView leftMenu = findViewById(R.id.menu);
        MenuInflater inflater = getMenuInflater();
        Menu menu = leftMenu.getMenu();
        inflater.inflate(R.menu.menu_item_back, menu);
        leftMenu.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.itemBack){
                finish();
                return true;
            }
            return false;
        });

        getTableService(tableId);

        lvCart = (ListView) findViewById(R.id.lvCart);
        btnCheckout = (Button) findViewById(R.id.btnCheckout);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(service.getServiceID() == 0){
                    Toast.makeText(ActivityCart.this, "Bàn này chưa đặt món", Toast.LENGTH_SHORT).show();
                }else{
                    java.util.Date currentDate = new java.util.Date();
                    checkoutService(service.getServiceID(), new Date(currentDate.getTime()));
                }
            }
        });
    }

    private void checkoutService(int serviceId, Date paymentTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(paymentTime);
        ApiService.apiService.updatePayment(serviceId, currentTime).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Message message = response.body();

                Toast.makeText(ActivityCart.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(ActivityCart.this, ActivityTable.class);
                startActivity(it);
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    void loadOrders(TableService service) {
        ApiService.apiService.getDishesByService(service.getServiceID()).enqueue(new Callback<List<TableDish>>() {
            @Override
            public void onResponse(Call<List<TableDish>> call, Response<List<TableDish>> response) {
                if(response.isSuccessful()){
                    orders = response.body();

                    if(orders == null){
                        Toast.makeText(ActivityCart.this, "Service này chưa có món nào cả", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    loadDataAdapterOrder(orders);
                }
            }

            @Override
            public void onFailure(Call<List<TableDish>> call, Throwable t) {

            }
        });
    }

    void loadTotalAmount(int serviceId) {
        ApiService.apiService.getPaymentByService(serviceId).enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if(response.isSuccessful()) {
                    Payment payment = response.body();

                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
                    symbols.setCurrencySymbol("₫");
                    symbols.setGroupingSeparator('.');
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0", symbols);

                    tvTotal.setText("Tổng tiền: " + decimalFormat.format(payment.getTotalAmount()) + " đ");
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {

            }
        });
    }

    private void loadDataAdapterOrder(List<TableDish> orders) {
        adapterOrder = new AdapterTableDish(ActivityCart.this, R.layout.layout_item_tabledish, orders);
        lvCart.setAdapter(adapterOrder);
    }

    void getTableService(int tableId) {
        ApiService.apiService.getServiceNotPay(tableId).enqueue(new Callback<TableService>() {
            @Override
            public void onResponse(Call<TableService> call, Response<TableService> response) {
                if(response.isSuccessful()){
                    service = response.body();

                    if(service == null || service.getServiceID() == 0){
                        return;
                    }

                    loadOrders(service);
                    loadTotalAmount(service.getServiceID());
                }
            }

            @Override
            public void onFailure(Call<TableService> call, Throwable t) {

            }
        });
    }
}