package com.example.managerestaurantapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.example.managerestaurantapp.adapters.CustomAdapterDinningTable;
import com.example.managerestaurantapp.models.DiningTable;
import com.example.managerestaurantapp.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DinningTableMainActivity extends AppCompatActivity {

    Button btn_Add,btn_Delete,btn_Update,btn_Reset;
    EditText edt_TableID,edt_SeatCount,edt_Note;
    ListView lv_DiningTables;
    Spinner spinnerTableFloor;
    ArrayList<Integer> data = new ArrayList<>();
    CustomAdapterDinningTable customAdapterDinningTable;
    ArrayList<DiningTable> lsDinningTable = new ArrayList<>();
    String url = Util.BASE_URL + "Phuong/DiningTableShow.php";
    String urlInsert = Util.BASE_URL + "Phuong/insertDiningTable.php";
    String urlDelete = Util.BASE_URL + "Phuong/deleteDiningTable.php";
    String urlUpdate= Util.BASE_URL + "Phuong/updateDiningTable.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinning_table_main);

        addControls();
        ////////////////
        data.add(1);
        data.add(2);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,data);
        spinnerTableFloor.setAdapter(adapter);
        ////////////////
        loadDatabase();
        //////////////////////////////
        addEvents();
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
        RequestQueue requestQueue = Volley.newRequestQueue(DinningTableMainActivity.this);
        requestQueue.add(request);
    }

    //Đọc và hiển thị dữ liệu
    public void  parseJsonData(String response) throws JSONException {
        JSONObject object  = new JSONObject(response);
        JSONArray dssvObject = object.getJSONArray("diningtables");

        for(int i=0;i<dssvObject.length();i++)
        {
            JSONObject table = dssvObject.getJSONObject(i);
            DiningTable a  = new DiningTable();
            a.setTableID(table.getInt("TableID"));
            a.setTableFloor(table.getInt("TableFloor"));
            a.setSeatCount(table.getInt("SeatCount"));
            a.setNote(table.getString("Note"));
            lsDinningTable.add(a);
        }
        customAdapterDinningTable=new CustomAdapterDinningTable(getApplicationContext(),R.layout.layout_item_dinningtable,
                lsDinningTable);
        lv_DiningTables.setAdapter(customAdapterDinningTable);
    }


    private void addControls()
    {
        lv_DiningTables = findViewById(R.id.lv_DiningTables);
        edt_TableID = findViewById(R.id.edt_TableID);
        edt_SeatCount = findViewById(R.id.edt_SeatCount);
        edt_Note = findViewById(R.id.edt_Note);
        btn_Add = findViewById(R.id.btn_Add);
        btn_Delete = findViewById(R.id.btn_Delete);
        btn_Update = findViewById(R.id.btn_Update);
        btn_Reset = findViewById(R.id.btn_Reset);
        spinnerTableFloor =  findViewById(R.id.spinnerTableFloor);
    }

    private void addEvents()
    {
        lv_DiningTables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object item = parent.getItemAtPosition(position);
                edt_TableID.setText(String.valueOf(lsDinningTable.get(position).getTableID()));
                edt_TableID.setEnabled(false);//Không được chỉnh sửa
                edt_SeatCount.setText(String.valueOf(lsDinningTable.get(position).getSeatCount()));
                edt_Note.setText(lsDinningTable.get(position).getNote());
                spinnerTableFloor.setSelection(lsDinningTable.get(position).getTableFloor() - 1);
            }
        });


        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sunmitFormInsert();
                removeEditText();
                customAdapterDinningTable=new CustomAdapterDinningTable(getApplicationContext(),R.layout.layout_item_dinningtable,
                        lsDinningTable);
                lv_DiningTables.setAdapter(customAdapterDinningTable);
                customAdapterDinningTable.notifyDataSetChanged();
            }
        });
        btn_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEditText();
            }
        });


        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sunmitFormDelete();
                removeEditText();
                customAdapterDinningTable=new CustomAdapterDinningTable(getApplicationContext(),R.layout.layout_item_dinningtable,
                        lsDinningTable);
                lv_DiningTables.setAdapter(customAdapterDinningTable);
                customAdapterDinningTable.notifyDataSetChanged();
            }
        });


        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sunmitFormUpdate();
                removeEditText();
                customAdapterDinningTable=new CustomAdapterDinningTable(getApplicationContext(),R.layout.layout_item_dinningtable,
                        lsDinningTable);
                lv_DiningTables.setAdapter(customAdapterDinningTable);
                customAdapterDinningTable.notifyDataSetChanged();
            }
        });
    }


    public void removeEditText()
    {
        edt_TableID.setText("");
        edt_TableID.setFocusable(true);
        edt_TableID.setEnabled(true);
        edt_SeatCount.setText("");
        edt_Note.setText("");
    }
    private void sunmitFormInsert()
    {
        int id =  Integer.parseInt(edt_TableID.getText().toString());
        int seatCount  = Integer.parseInt(edt_SeatCount.getText().toString());
        Integer selectedItem = (Integer) spinnerTableFloor.getSelectedItem();

        String note = edt_Note.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            DiningTable dinningTable = new DiningTable();
            jsonObject.put("TableID",id);
            jsonObject.put("TableFloor",selectedItem);
            jsonObject.put("SeatCount",seatCount);
            jsonObject.put("Note",note);

            dinningTable.setTableID(id);
            dinningTable.setTableFloor(selectedItem);
            dinningTable.setSeatCount(seatCount);
            dinningTable.setNote(note);

            them(jsonObject);
            lsDinningTable.add(dinningTable);
        }
        catch(JSONException e)
        {
            Toast.makeText(getApplicationContext(),"Lỗi Tạo Json" ,Toast.LENGTH_LONG).show();
        }
    }

    private void them(JSONObject JsonDiningTable)
    {
        RequestQueue queue = Volley.newRequestQueue(DinningTableMainActivity.this);
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
        int id =  Integer.parseInt(edt_TableID.getText().toString());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("TableID",id);
            delete(jsonObject);
            removeDinningTableByID(lsDinningTable,id);
//            Toast.makeText(getApplicationContext()," Tạo được  Json" ,Toast.LENGTH_LONG).show();
        }
        catch(JSONException e)
        {
            Toast.makeText(getApplicationContext(),"Lỗi Tạo Json" ,Toast.LENGTH_LONG).show();
        }
    }

    public static void removeDinningTableByID(ArrayList<DiningTable> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTableID() == id) {
                list.remove(i);
                break; // Thoát khỏi vòng lặp sau khi xóa phần tử
            }
        }
    }

    private void delete(JSONObject JsonDiningTable)
    {
        RequestQueue queue = Volley.newRequestQueue(DinningTableMainActivity.this);
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
        RequestQueue queue = Volley.newRequestQueue(DinningTableMainActivity.this);
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
        int id =  Integer.parseInt(edt_TableID.getText().toString());
        int seatCount  = Integer.parseInt(edt_SeatCount.getText().toString());
        Integer selectedItem = (Integer) spinnerTableFloor.getSelectedItem();
        String note = edt_Note.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("TableID",id);
            jsonObject.put("TableFloor",selectedItem);
            jsonObject.put("SeatCount",seatCount);
            jsonObject.put("Note",note);
            update(jsonObject);
            int index = FindDinningTableByID(lsDinningTable,id);
            lsDinningTable.get(index).setSeatCount(seatCount);
            lsDinningTable.get(index).setNote(note);
            lsDinningTable.get(index).setTableFloor(selectedItem);
//            Toast.makeText(getApplicationContext()," Tạo được  Json" ,Toast.LENGTH_LONG).show();
        }
        catch(JSONException e)
        {
            Toast.makeText(getApplicationContext(),"Lỗi Tạo Json" ,Toast.LENGTH_LONG).show();
        }
    }

    public static int FindDinningTableByID(ArrayList<DiningTable> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTableID() == id) {
                return i;
            }
        }
        return -1;
    }


}