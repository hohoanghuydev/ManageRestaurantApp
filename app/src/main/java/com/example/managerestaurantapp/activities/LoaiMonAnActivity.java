package com.example.managerestaurantapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.managerestaurantapp.adapters.CustomAdapterLoaiMonAn;
import com.example.managerestaurantapp.models.LoaiMonAn;
import com.example.managerestaurantapp.models.MonAn;
import com.example.managerestaurantapp.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoaiMonAnActivity extends AppCompatActivity {
    EditText edtMaLoaiMon,edtTenLoaiMon;
    Button btnLuu, btnHuy,btnXoa,btnSua;
    ListView lvLoaiMon;

    ArrayList<LoaiMonAn> lsLoaiMonAn = new ArrayList<LoaiMonAn>();
    ArrayAdapter<String> adapter;
    CustomAdapterLoaiMonAn adapterLoaiMon;
    ArrayList<MonAn> lsMonAn = new ArrayList<MonAn>();
    String urlInsert = Util.BASE_URL + "Ngoc/Insert_DishCategory.php";
    String urlDelete = Util.BASE_URL + "Ngoc/Delete_DishCategory.php";
    String urlUpdate= Util.BASE_URL + "Ngoc/Update_DishCategory.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loaimon_activity);
        String url = Util.BASE_URL + "Ngoc/Data_Category.php";
        addControls();
        getAllDataDish(url);
        addEvents();
    }

    public void addControls() {
        lvLoaiMon = (ListView) findViewById(R.id.lvLoaiMon);
        btnLuu = (Button) findViewById(R.id.btnLuu);
        btnHuy = (Button) findViewById(R.id.btnHuy);
        edtMaLoaiMon = (EditText) findViewById(R.id.edtMaLoaiMon);
        btnXoa = (Button) findViewById(R.id.btnXoa);
        edtTenLoaiMon = (EditText) findViewById(R.id.edtTenLoaiMon);
        btnSua = (Button) findViewById(R.id.btnSua);
    }

    public void addEvents() {
        lvLoaiMon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               LoaiMonAn loaimonAn = lsLoaiMonAn.get(position);
                Context context = null;
                edtMaLoaiMon.setText(String.valueOf(loaimonAn.getMaLoaiMonAn()));
                edtTenLoaiMon.setText(loaimonAn.getTenLoaiMonAn());
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sunmitFormInsert();
                removeEditText();
                adapterLoaiMon = new CustomAdapterLoaiMonAn(getApplicationContext(),R.layout.activity_item_loaimon_an,lsLoaiMonAn);
                lvLoaiMon.setAdapter(adapterLoaiMon);
                adapterLoaiMon.notifyDataSetChanged();
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sunmitFormDelete();
                removeEditText();
                adapterLoaiMon = new CustomAdapterLoaiMonAn(getApplicationContext(),R.layout.activity_item_loaimon_an, lsLoaiMonAn);
                lvLoaiMon.setAdapter(adapterLoaiMon);
                adapterLoaiMon.notifyDataSetChanged();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEditText();
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFormUpdate();
                removeEditText();
                adapterLoaiMon = new CustomAdapterLoaiMonAn(getApplicationContext(),R.layout.activity_item_loaimon_an, lsLoaiMonAn);
                lvLoaiMon.setAdapter(adapterLoaiMon);
                adapterLoaiMon.notifyDataSetChanged();
            }
        });

    }
    public void getAllDataDish(String url) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(LoaiMonAnActivity.this);
        requestQueue.add(request);
    }
    public void parseJsonData(String response) throws JSONException {
        JSONObject object  = new JSONObject(response);
        JSONArray dsLoaiMonObject = object.getJSONArray("dishcategory");
        for(int i = 0;i < dsLoaiMonObject.length();++i)
        {
            JSONObject LoaimonAn = dsLoaiMonObject.getJSONObject(i);
            LoaiMonAn a  = new LoaiMonAn();
            a.setMaLoaiMonAn(LoaimonAn.getInt("CategoryID"));
            a.setTenLoaiMonAn(LoaimonAn.getString("CategoryName"));
            lsLoaiMonAn.add(a);
        }
        adapterLoaiMon =new CustomAdapterLoaiMonAn(getApplicationContext(),R.layout.activity_item_loaimon_an,lsLoaiMonAn);
        lvLoaiMon.setAdapter(adapterLoaiMon);
    }
    public void removeEditText()
    {
        edtMaLoaiMon.setText("");
        edtMaLoaiMon.setFocusable(true);
        edtMaLoaiMon.setEnabled(true);
        edtTenLoaiMon.setText("");

    }
    private void sunmitFormInsert() {
        String idStr = edtMaLoaiMon.getText().toString().trim();
        String name = edtTenLoaiMon.getText().toString().trim();
        if (idStr.isEmpty() || name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CategoryID", id);
            jsonObject.put("CategoryName", name);
            LoaiMonAn loaiMonAn = new LoaiMonAn();
            loaiMonAn.setMaLoaiMonAn(id);
            loaiMonAn.setTenLoaiMonAn(name);
            them(jsonObject);
            lsLoaiMonAn.add(loaiMonAn);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error creating JSON", Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Invalid number format for Category ID", Toast.LENGTH_LONG).show();
        }
    }


    private void them(JSONObject loaiMonAn)
    {
        RequestQueue queue = Volley.newRequestQueue(LoaiMonAnActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlInsert, loaiMonAn, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(LoaiMonAnActivity.this,"Thêm Thành Công" + response.toString(),Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", error.toString());
            }
        });
        queue.add(jsonObjectRequest);
    }
    private void sunmitFormDelete() {
        int maLoaiMon = Integer.parseInt(edtMaLoaiMon.getText().toString());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CategoryID", maLoaiMon);
            if (checkCategoryInDishes(maLoaiMon)) {
                Toast.makeText(getApplicationContext(), "Loại món ăn đang được sử dụng trong các món ăn, không thể xóa.", Toast.LENGTH_LONG).show();
            } else {
                delete(jsonObject);

            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Lỗi tạo JSON", Toast.LENGTH_LONG).show();
        }
    }
    public static void removeLoaiMonAnByID(ArrayList<LoaiMonAn> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaLoaiMonAn() == id) {
                list.remove(i);
                break;
            }
        }
    }

    private void delete(JSONObject loaiMonAn)
    {
        RequestQueue queue = Volley.newRequestQueue(LoaiMonAnActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlDelete, loaiMonAn, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(LoaiMonAnActivity.this,"Xóa Thành Công" + response.toString(),Toast.LENGTH_LONG).show();
                lsLoaiMonAn.removeIf(r -> {
                    try {
                        return r.getMaLoaiMonAn() == loaiMonAn.getInt("CategoryID");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
                adapterLoaiMon.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", error.toString());
            }
        });
        queue.add(jsonObjectRequest);
    }


    private void update(JSONObject loaiMonAn)
    {
        RequestQueue queue = Volley.newRequestQueue(LoaiMonAnActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlUpdate, loaiMonAn, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(LoaiMonAnActivity.this,"Thêm Thành Công" + response.toString(),Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", error.toString());
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void submitFormUpdate() {
        String idStr = edtMaLoaiMon.getText().toString().trim();
        String name = edtTenLoaiMon.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            int id = Integer.parseInt(idStr);
            jsonObject.put("CategoryID", id);
            jsonObject.put("CategoryName", name); // Ensure 'name' is also added to the JSON
            update(jsonObject);

            int index = FindDinningMaLoaiMon(lsLoaiMonAn, id);
            lsLoaiMonAn.get(index).setTenLoaiMonAn(name);
        } catch (JSONException e) {
            Log.d("Error", e.toString());
        }
    }
    public static int FindDinningMaLoaiMon(ArrayList<LoaiMonAn> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaLoaiMonAn() == id) {
                return i;
            }
        }
        return -1;
    }
    private boolean checkCategoryInDishes(int categoryID) {
        boolean categoryExists = false;
        // Assuming you have a list of dishes and each dish has a categoryID field
        for (MonAn dish : lsMonAn) {
            if (dish.getMaLoaiMon() == categoryID) {
                categoryExists = true;
                break;
            }
        }
        return categoryExists;
    }


}
