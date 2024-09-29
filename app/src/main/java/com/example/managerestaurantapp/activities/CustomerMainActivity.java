package com.example.managerestaurantapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.adapters.CustomAdapterCustomer;
import com.example.managerestaurantapp.models.Customer;
import com.example.managerestaurantapp.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerMainActivity extends AppCompatActivity {

    Button btnAddCustomer,btnDeleteCustomer,btnUpdateCustomer,btnResetCustomer;
    ListView lv_Customer;
    EditText edtCustomerID,edtCustomerName,edtAdress,edtPhoneNumber;

    ArrayList<Customer> lsCustomer = new ArrayList<>();
    CustomAdapterCustomer customAdapterCustomer;
    String url = Util.BASE_URL + "Phuong/CustomerShow.php";
    String urlInsert = Util.BASE_URL + "Phuong/insertCustomer.php";
    String urlDelete = Util.BASE_URL + "Phuong/deleteCustomer.php";
    String urlUpdate = Util.BASE_URL + "Phuong/updateCustomer.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        addControls();
        loadDatabase();
        addEvents();
    }

    private void addControls()
    {
        edtCustomerID = findViewById(R.id.edtCustomerID);
        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtAdress = findViewById(R.id.edtAdress);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        lv_Customer = findViewById(R.id.lv_Customer);
        btnAddCustomer = findViewById(R.id.btnAddCustomer);
        btnDeleteCustomer = findViewById(R.id.btnDeleteCustomer);
        btnUpdateCustomer = findViewById(R.id.btnUpdateCustomer);
        btnResetCustomer = findViewById(R.id.btnResetCustomer);
    }

    private void addEvents()
    {

        lv_Customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtCustomerID.setText(String.valueOf(lsCustomer.get(position).getCustomerId()));
                edtCustomerID.setEnabled(false);
                edtCustomerName.setText(lsCustomer.get(position).getCustomerName());
                edtAdress.setText(lsCustomer.get(position).getAdress());
                edtPhoneNumber.setText(lsCustomer.get(position).getPhoneNumber());
            }
        });

        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sunmitFormInsert();
                removeEditText();
                customAdapterCustomer=new CustomAdapterCustomer(getApplicationContext(),R.layout.layout_item_customer,
                        lsCustomer);
                lv_Customer.setAdapter(customAdapterCustomer);
            }
        });

        btnDeleteCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sunmitFormDelete();
                removeEditText();
                customAdapterCustomer=new CustomAdapterCustomer(getApplicationContext(),R.layout.layout_item_customer,
                        lsCustomer);
                lv_Customer.setAdapter(customAdapterCustomer);
            }
        });
        btnUpdateCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sunmitFormUpdate();
                removeEditText();
                customAdapterCustomer=new CustomAdapterCustomer(getApplicationContext(),R.layout.layout_item_customer,
                        lsCustomer);
                lv_Customer.setAdapter(customAdapterCustomer);
            }
        });

        btnResetCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEditText();
            }
        });

    }

    public void loadDatabase()
    {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseJsonData(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerMainActivity.this);
        requestQueue.add(request);
    }

    private void removeEditText()
    {
        edtCustomerID.setText("");
        edtCustomerID.setEnabled(true);
        edtCustomerName.setText("");
        edtAdress.setText("");
        edtPhoneNumber.setText("");
        edtCustomerID.setFocusable(true);
    }

    //Đọc và hiển thị dữ liệu
    public void  parseJsonData(String response) throws JSONException {
        JSONObject object  = new JSONObject(response);
        JSONArray dssvObject = object.getJSONArray("customers");

        for(int i=0;i<dssvObject.length();i++)
        {
            JSONObject table = dssvObject.getJSONObject(i);
            Customer customer  = new Customer();
            customer.setCustomerId(table.getInt("CustomerID"));
            customer.setCustomerName(table.getString("CustomerName"));
            customer.setAdress(table.getString("Address"));
            customer.setPhoneNumber(table.getString("PhoneNumber"));

            lsCustomer.add(customer);
        }
        customAdapterCustomer=new CustomAdapterCustomer(getApplicationContext(),R.layout.layout_item_customer,
                lsCustomer);
        lv_Customer.setAdapter(customAdapterCustomer);
    }

    private void sunmitFormInsert()
    {
        int id =  Integer.parseInt(edtCustomerID.getText().toString());
        String name = edtCustomerName.getText().toString();
        String address = edtAdress.getText().toString();
        String phone = edtPhoneNumber.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            Customer customer = new Customer();
            jsonObject.put("CustomerID",id);
            jsonObject.put("CustomerName",name);
            jsonObject.put("Address",address);
            jsonObject.put("PhoneNumber",phone);
            customer.setCustomerId(id);
            customer.setCustomerName(name);
            customer.setAdress(address);
            customer.setPhoneNumber(phone);
            them(jsonObject);
            lsCustomer.add(customer);
//            Toast.makeText(getApplicationContext()," Tạo được  Json" ,Toast.LENGTH_LONG).show();
        }
        catch(JSONException e)
        {
            Toast.makeText(getApplicationContext(),"Lỗi Tạo Json" ,Toast.LENGTH_LONG).show();
        }
    }

    private void them(JSONObject JsonDiningTable)
    {
        RequestQueue queue = Volley.newRequestQueue(CustomerMainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlInsert, JsonDiningTable, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(DinningTableMainActivity.this,"Thêm Thành Công" + response.toString(),Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(DinningTableMainActivity.this,"Thêm Thất Bại" + error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }


    private void sunmitFormDelete()
    {
        int id =  Integer.parseInt(edtCustomerID.getText().toString());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CustomerID",id);
            delete(jsonObject);
            removeCustomerByID(lsCustomer,id);
//            Toast.makeText(getApplicationContext()," Tạo được  Json" ,Toast.LENGTH_LONG).show();
        }
        catch(JSONException e)
        {
            Toast.makeText(getApplicationContext(),"Lỗi Tạo Json" ,Toast.LENGTH_LONG).show();
        }
    }

    public static void removeCustomerByID(ArrayList<Customer> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCustomerId() == id) {
                list.remove(i);
                break; // Thoát khỏi vòng lặp sau khi xóa phần tử
            }
        }
    }

    private void delete(JSONObject JsonDiningTable)
    {
        RequestQueue queue = Volley.newRequestQueue(CustomerMainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlDelete, JsonDiningTable, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(DinningTableMainActivity.this,"Thêm Thành Công" + response.toString(),Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(DinningTableMainActivity.this,"Thêm Thất Bại" + error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }


    private void update(JSONObject JsonDiningTable)
    {
        RequestQueue queue = Volley.newRequestQueue(CustomerMainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlUpdate, JsonDiningTable, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(DinningTableMainActivity.this,"Thêm Thành Công" + response.toString(),Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(DinningTableMainActivity.this,"Thêm Thất Bại" + error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void sunmitFormUpdate()
    {
        int id =  Integer.parseInt(edtCustomerID.getText().toString());
        String name = edtCustomerName.getText().toString();
        String address = edtAdress.getText().toString();
        String phone = edtPhoneNumber.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CustomerID",id);
            jsonObject.put("CustomerName",name);
            jsonObject.put("Address",address);
            jsonObject.put("PhoneNumber",phone);
            update(jsonObject);
            int index = FindCustomerByID(lsCustomer,id);
            lsCustomer.get(index).setCustomerName(name);
            lsCustomer.get(index).setAdress(address);
            lsCustomer.get(index).setPhoneNumber(phone);
//            Toast.makeText(getApplicationContext()," Tạo được  Json" ,Toast.LENGTH_LONG).show();
        }
        catch(JSONException e)
        {
            Toast.makeText(getApplicationContext(),"Lỗi Tạo Json" ,Toast.LENGTH_LONG).show();
        }
    }

    public static int FindCustomerByID(ArrayList<Customer> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCustomerId() == id) {
                return i;
            }
        }
        return -1;
    }

}