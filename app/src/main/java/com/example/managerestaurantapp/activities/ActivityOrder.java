package com.example.managerestaurantapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;

import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.adapters.AdapterOrderDish;
import com.example.managerestaurantapp.adapters.BottomSheetDialogOrder;
import com.example.managerestaurantapp.services.ApiService;
import com.example.managerestaurantapp.models.Dish;
import com.example.managerestaurantapp.models.DishCategory;
import com.example.managerestaurantapp.models.Message;
import com.example.managerestaurantapp.models.Order;
import com.example.managerestaurantapp.models.Payment;
import com.example.managerestaurantapp.models.TableDish;
import com.example.managerestaurantapp.models.TableService;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityOrder extends AppCompatActivity implements BottomSheetDialogOrder.OnDataPassListener {

    Button btnCart;
    Spinner spinnerLoaiMon;
    ListView lvMon;

    AdapterOrderDish adapterMon;
    ArrayAdapter<DishCategory> adapterLoaiMon;

    List<Dish> dishes = new ArrayList<>();
    List<DishCategory> categories = new ArrayList<>();
    Order newOrder = new Order();
    int tableId;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        addControls();

        toolbar = findViewById(R.id.toolbarOrder);
        setSupportActionBar(toolbar);

        Intent intentTable = getIntent();
        Bundle bundleDataTable = intentTable.getExtras();
        if(bundleDataTable != null){
            tableId = bundleDataTable.getInt("tableId");

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }

            TextView toolbarTitle = findViewById(R.id.toolbarTitle);
            toolbarTitle.setText("Bàn " + tableId);

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
        }

        categories.add(new DishCategory(0, "Chọn loại món..."));

        loadDishes();

        loadCategories();

        addEvents();
    }

    void loadCategories() {
        ApiService.apiService.getAllCategories().enqueue(new Callback<List<DishCategory>>() {
            @Override
            public void onResponse(Call<List<DishCategory>> call, Response<List<DishCategory>> response) {
                if(response.isSuccessful()){
                    List<DishCategory> lstCategory = new ArrayList<>();
                    lstCategory = response.body();

                    categories.addAll(lstCategory);

                    loadDataAdapterCategories(categories);
                }
            }

            @Override
            public void onFailure(Call<List<DishCategory>> call, Throwable t) {

            }
        });
    }

    private void loadDataAdapterCategories(List<DishCategory> categories) {
        adapterLoaiMon = new ArrayAdapter<>(ActivityOrder.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
        spinnerLoaiMon.setAdapter(adapterLoaiMon);
    }

    void loadDishes() {
        ApiService.apiService.getAllDishes().enqueue(new Callback<List<Dish>>() {
            @Override
            public void onResponse(Call<List<Dish>> call, Response<List<Dish>> response) {
                if(response.isSuccessful()){
                    dishes = response.body();

                    loadDataAdapterDishes(dishes);
                }
            }

            @Override
            public void onFailure(Call<List<Dish>> call, Throwable t) {

            }
        });
    }

    void loadDataAdapterDishes(List<Dish> dishes) {
        adapterMon = new AdapterOrderDish(ActivityOrder.this, R.layout.layout_item_orderdish, dishes);
        lvMon.setAdapter(adapterMon);
    }

    private List<TableService> loadHoaDon(){
        List<TableService> services = new ArrayList<>();
        services.add(new TableService(1, 1, 1, Date.valueOf("2024-03-20 14:22:11")));
        return services;
    }
    private void addEvents() {
        lvMon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dish dish = dishes.get(i);
                onClickOrder(dish);
            }
        });
        spinnerLoaiMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DishCategory category = categories.get(i);
                if(category.getCategoryName().equals("Chọn loại món...")){
                    loadDataAdapterDishes(dishes);
                }else{
                    List<Dish> lstDishFilter = new ArrayList<>();
                    lstDishFilter = dishes.stream().filter(r -> r.getCategoryID() == category.getCategoryID()).collect(Collectors.toList());
                    loadDataAdapterDishes(lstDishFilter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCart = new Intent(ActivityOrder.this, ActivityCart.class);
                intentCart.putExtra("tableId", tableId);
                startActivity(intentCart);
            }
        });
    }

    private void onClickOrder(Dish dish) {
        Order order = new Order(dish.getDishID(), dish.getDishName(), 1, dish.getUnitPrice());
        BottomSheetDialogOrder dialogOrder = BottomSheetDialogOrder.newInstance(order);
        dialogOrder.show(getSupportFragmentManager(), dialogOrder.getTag());
        dialogOrder.setCancelable(false);
    }

    void addControls(){
        spinnerLoaiMon = (Spinner) findViewById(R.id.spinnerLoaiMon);
        lvMon = (ListView) findViewById(R.id.lvMon);
        btnCart = (Button) findViewById(R.id.btnCart);
    }

    @Override
    public void onDataPass(Order order) {
        newOrder = order;
        //Tạo mới service
        //Toast.makeText(this, order.toString(), Toast.LENGTH_SHORT).show();
        //Thêm món với service trên
//        TableDish dish = new TableDish(2, order.getDishID(), order.getQuantity(), order.getDishPrice() * order.getQuantity(), "Khong");
//        Toast.makeText(this, dish.toString(), Toast.LENGTH_SHORT).show();
        getServiceByTable();
    }

    void getServiceByTable() {
        ApiService.apiService.getServiceNotPay(tableId).enqueue(new Callback<TableService>() {
            @Override
            public void onResponse(Call<TableService> call, Response<TableService> response) {
                if(response.isSuccessful()){
                    TableService service = response.body();

                    if(service == null || service.getServiceID() == 0){
//                        Toast.makeText(ActivityOrder.this, "Tạo service mới", Toast.LENGTH_SHORT).show();
                        service.setCustomerID(1);

                        java.util.Date currentDate = new java.util.Date();

                        service.setStartTime(new Date(currentDate.getTime()));
                        createService(service);
                        return;
                    }
                    //kiem tra xem mon do co trong service khong
                    //neu co goi api update
                    //neu khong goi api create
                    //Gia su chua co mon do
                    //Toast.makeText(ActivityOrder.this, service.toString(), Toast.LENGTH_SHORT).show();
                    addFoodToTableDish(service.getServiceID());
                }
            }

            @Override
            public void onFailure(Call<TableService> call, Throwable t) {

            }
        });
    }

    void createService(TableService service) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Toast.makeText(this, dateFormat.format(service.getStartTime()), Toast.LENGTH_SHORT).show();
        String currentTime = dateFormat.format(service.getStartTime());
        ApiService.apiService.createNewServiceByTable(service.getCustomerID(), service.getTableID(), currentTime).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    Message message = response.body();

//                    Toast.makeText(ActivityOrder.this, message.getMessage(), Toast.LENGTH_SHORT).show();

                    int newService = Integer.parseInt(message.getMessage());

                    //Tao payment (EmployeeID phai dang nhap) tam thoi la 1
                    Payment payment = new Payment(newService, 1, 0, 0, null);
                    
                    createPayment(payment);

                    addFoodToTableDish(newService);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    void addFoodToTableDish(int serviceID) {
        checkDishByService(serviceID, newOrder.getDishID());
    }

    private void checkDishByService(int serviceID, int dishID) {
        ApiService.apiService.getDishOrder(serviceID, dishID).enqueue(new Callback<TableDish>() {
            @Override
            public void onResponse(Call<TableDish> call, Response<TableDish> response) {
                if(response.isSuccessful()){
                    TableDish dishOrder = response.body();

                    if(dishOrder.getServiceID() == 0){
                        //Tao mon moi
                        dishOrder.setServiceID(serviceID);
                        dishOrder.setDishID(newOrder.getDishID());
                        dishOrder.setQuantity(newOrder.getQuantity());
                        dishOrder.setUnitPrice(newOrder.getQuantity() * newOrder.getDishPrice());

                        addOrder(dishOrder);
                    }else{
                        //Update so luong
                        dishOrder.setQuantity(newOrder.getQuantity());
                        dishOrder.setUnitPrice(newOrder.getQuantity() * newOrder.getDishPrice());

                        updateOrder(dishOrder);
                    }
                }else{
                    Toast.makeText(ActivityOrder.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TableDish> call, Throwable t) {
                Toast.makeText(ActivityOrder.this, "Khong tim thay mon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOrder(TableDish tableDish) {
        ApiService.apiService.updateQuantityDish(tableDish.getServiceID(), tableDish.getDishID(), tableDish.getQuantity(), tableDish.getUnitPrice(), tableDish.getNote()).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    Message message = response.body();

                    Toast.makeText(ActivityOrder.this, "Thêm món thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    private void addOrder(TableDish tableDish) {
        ApiService.apiService.addDishToOrders(tableDish.getServiceID(), tableDish.getDishID(), tableDish.getQuantity(), tableDish.getUnitPrice(), tableDish.getNote()).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    Message message = response.body();

                    Toast.makeText(ActivityOrder.this, "Thêm món thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    private void createPayment(Payment payment) {
        ApiService.apiService.createNewPaymentByService(payment.getServiceID(), payment.getEmployeeID()).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    Message message = response.body();

//                    Toast.makeText(ActivityOrder.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(ActivityOrder.this, "Payment này đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}