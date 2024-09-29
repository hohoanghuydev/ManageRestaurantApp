package com.example.managerestaurantapp.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.managerestaurantapp.adapters.CustomAdapterMonAn;
import com.example.managerestaurantapp.models.MonAn;
import com.example.managerestaurantapp.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MonAnActivity extends AppCompatActivity {
    EditText edtMaMon, edtTenMon, edtDonGia, edtMaLoaiMon;
    Button btnLuu, btnHuy,btnXoa,btnSua,btnXem;

    //Spinner spMaLoaiMonAn;
    ListView lvMonAn;
    private Uri imageUri;
    private RequestQueue requestQueue;
    ArrayList<MonAn> lsMonAn = new ArrayList<MonAn>();
    ArrayAdapter<String> adapter;
    CustomAdapterMonAn adapterMonAn;
    private String encodedImage;
    Button btnThemAnh;
    ImageView imgAnh;
    Spinner spinnerMaLoai;
    private Bitmap bitmap;
    String urlInsert = Util.BASE_URL + "Ngoc/Insert_Dish.php";
    String urlDelete = Util.BASE_URL + "Ngoc/DeleteDish.php";
    String urlUpdate= Util.BASE_URL + "Ngoc/Update_dish.php";
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monan);
        String url = Util.BASE_URL + "Ngoc/Data.php";
        addControls();
        getAllDataDish(url);
        addEvents();
    }
    public void addControls() {
        edtMaMon = (EditText) findViewById(R.id.edtMaMon);
        edtTenMon = (EditText) findViewById(R.id.edtTenMon);
        edtDonGia = (EditText) findViewById(R.id.edtDonGia);
        lvMonAn = (ListView) findViewById(R.id.lvMonAn);
        btnLuu = (Button) findViewById(R.id.btnLuu);
        btnHuy = (Button) findViewById(R.id.btnHuy);
        edtMaLoaiMon = (EditText) findViewById(R.id.edtMaLoaiMon);
        btnXoa = (Button) findViewById(R.id.btnXoa);
        btnSua = (Button) findViewById(R.id.btnSua);
        imgAnh = (ImageView) findViewById(R.id.imgAnh);
        btnThemAnh = findViewById(R.id.btnThemAnh);
        btnXem = findViewById(R.id.btnXem);
        spinnerMaLoai = findViewById(R.id.spinnerMaLoai);
    }
    public void addEvents() {
        lvMonAn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MonAn monAn = lsMonAn.get(position);
                Context context = null;
                edtMaMon.setText(String.valueOf(monAn.getMaMon()));
                edtTenMon.setText(monAn.getTenMon());
                edtDonGia.setText(String.valueOf(monAn.getGiaMon()));
//                edtMaLoaiMon.setText(String.valueOf(monAn.getMaLoaiMon()));
//                Picasso.get().load(monAn.getImageURL()).into(imgAnh);
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sunmitFormInsert();
                removeEditText();
                adapterMonAn =new CustomAdapterMonAn(getApplicationContext(),R.layout.activity_item_mon_an,
                        lsMonAn);
                lvMonAn.setAdapter(adapterMonAn);
                adapterMonAn.notifyDataSetChanged();

            }
        });
         btnXoa.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sunmitFormDelete();
                 removeEditText();
                 adapterMonAn =new CustomAdapterMonAn(getApplicationContext(),R.layout.activity_item_mon_an,
                         lsMonAn);
                 lvMonAn.setAdapter(adapterMonAn);
                 adapterMonAn.notifyDataSetChanged();
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

                  sunmitFormUpdate();
                  removeEditText();
                  adapterMonAn =new CustomAdapterMonAn(getApplicationContext(),R.layout.activity_item_mon_an,
                          lsMonAn);
                  lvMonAn.setAdapter(adapterMonAn);

                  adapterMonAn.notifyDataSetChanged();
              }
          });
          btnThemAnh.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  openImagePicker();
              }
          });
          btnXem.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(MonAnActivity.this, LoaiMonAnActivity.class);
                  startActivity(intent);
              }
          });

    }
    private int findDish(List<MonAn> dishList, int dishID) {
        for (int i = 0; i < dishList.size(); i++) {
            MonAn monAn = dishList.get(i);
            if (monAn.getMaMon() == dishID) {
                return i;
            }
        }
        return -1;
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
        RequestQueue requestQueue = Volley.newRequestQueue(MonAnActivity.this);
        requestQueue.add(request);
    }
    public void parseJsonData(String response) throws JSONException {
        JSONObject object  = new JSONObject(response);
        JSONArray dsmonanObject = object.getJSONArray("dishes");
        List<String> maLoaiMonList = new ArrayList<>();
        for(int i = 0;i < dsmonanObject.length();++i)
        {
            JSONObject monAn = dsmonanObject.getJSONObject(i);
            MonAn a  = new MonAn();
            a.setMaMon(monAn.getInt("DishID"));
            a.setTenMon(monAn.getString("DishName"));
            a.setGiaMon(monAn.getInt("UnitPrice"));
            a.setMaLoaiMon(monAn.getInt("CategoryID"));
            int maLoaiMon = a.getMaLoaiMon();
            if (!maLoaiMonList.contains(String.valueOf(maLoaiMon))) {
                maLoaiMonList.add(String.valueOf(maLoaiMon));
            }
//            a.setImageURL("http://"+ip+"/Ngoc/Image/" + monAn.getString("Image"));
            lsMonAn.add(a);
        }
        adapterMonAn =new CustomAdapterMonAn(getApplicationContext(),R.layout.activity_item_mon_an,lsMonAn);
        lvMonAn.setAdapter(adapterMonAn);

        ArrayAdapter<String> maLoaiMonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, maLoaiMonList);
        maLoaiMonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaLoai.setAdapter(maLoaiMonAdapter);
    }
    public void removeEditText()
    {
        edtMaMon.setText("");
        edtMaMon.setFocusable(true);
        edtMaMon.setEnabled(true);
        edtTenMon.setText("");
        edtDonGia.setText("");
        imgAnh.setImageResource(0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgAnh.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private boolean isMaMonExist(int maMon) {
        for (MonAn monAn : lsMonAn) {
            if (monAn.getMaMon() == maMon) {
                return true;
            }
        }
        return false;
    }

    private void sunmitFormInsert() {
        try {
            int maMon = Integer.parseInt(edtMaMon.getText().toString().trim());
            if (isMaMonExist(maMon)) {
                Toast.makeText(getApplicationContext(), "Mã món đã tồn tại trong danh sách", Toast.LENGTH_LONG).show();
                return;
            }
            String tenMon = edtTenMon.getText().toString().trim();
            int donGia = Integer.parseInt(edtDonGia.getText().toString().trim());
            // Lấy mã loại món từ Spinner
            int maLoaiMon = Integer.parseInt(spinnerMaLoai.getSelectedItem().toString());
            if (TextUtils.isEmpty(edtMaMon.getText()) ||
                    TextUtils.isEmpty(edtTenMon.getText()) ||
                    TextUtils.isEmpty(edtDonGia.getText())) {
                Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                return;
            }
            if (imageUri == null) {
                Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_LONG).show();
                return;
            }
            String imagePath = getPathFromUri(imageUri);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("DishID", maMon);
            jsonObject.put("DishName", tenMon);
            jsonObject.put("UnitPrice", donGia);
            jsonObject.put("CategoryID", maLoaiMon);
            jsonObject.put("Image", imagePath);

            // Thêm món vào danh sách
            MonAn monAn = new MonAn();
            monAn.setMaMon(maMon);
            monAn.setTenMon(tenMon);
            monAn.setGiaMon(donGia);
            monAn.setMaLoaiMon(maLoaiMon);
            lsMonAn.add(monAn);

            refreshListView();
            them(jsonObject);
        } catch (JSONException e) {
            Log.d("API", e.toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Please enter valid number", Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            Log.d("API", e.toString());
        }
    }


    private String getPathFromUri(Uri uri) {
        if (uri == null) {
            return null; // Trả về null nếu Uri là null
        }

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                String imagePath = cursor.getString(column_index);
                cursor.close();
                return imagePath;
            }
            cursor.close();
        }

        return null; // Trả về null nếu không có dữ liệu hoặc không thể truy cập Cursor
    }


    private void them(JSONObject JsonMonAn)
    {
        RequestQueue queue = Volley.newRequestQueue(MonAnActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlInsert, JsonMonAn, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(MonAnActivity.this,"Thêm Thành Công" + response.toString(),Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", error.toString());
            }

        });
        queue.add(jsonObjectRequest);
    }
    private void sunmitFormDelete()
    {
        int maMon =  Integer.parseInt(edtMaMon.getText().toString());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DishID",maMon);
            delete(jsonObject);
            removeMonAnByID(lsMonAn,maMon);
        }
        catch(JSONException e)
        {
            Toast.makeText(getApplicationContext(),"Lỗi Tạo Json" ,Toast.LENGTH_LONG).show();
        }
    }

    public static void removeMonAnByID(ArrayList<MonAn> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaMon() == id) {
                list.remove(i);
                break; // Thoát khỏi vòng lặp sau khi xóa phần tử
            }
        }
    }

    private void delete(JSONObject JsonMonAn)
    {
        RequestQueue queue = Volley.newRequestQueue(MonAnActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlDelete, JsonMonAn, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", error.toString());
            }
        });
        queue.add(jsonObjectRequest);
    }


    private void update(JSONObject JsonMonAn)
    {
        RequestQueue queue = Volley.newRequestQueue(MonAnActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlUpdate, JsonMonAn, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", error.toString());
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void sunmitFormUpdate() {
        int maMon = Integer.parseInt(edtMaMon.getText().toString());
        String tenMon = edtTenMon.getText().toString();
        int giaMon = Integer.parseInt(edtDonGia.getText().toString());
        // Lấy mã loại món từ Spinner
        int maLoaiMon = Integer.parseInt(spinnerMaLoai.getSelectedItem().toString());
        String img = null;
        if (imageUri != null) {
            img = imageUri.toString();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DishID", maMon);
            jsonObject.put("DishName", tenMon);
            jsonObject.put("UnitPrice", giaMon);
            jsonObject.put("CategoryID", maLoaiMon);
            jsonObject.put("Image", img);
            update(jsonObject);
            int index = FindDinningMaMon(lsMonAn, maMon);
            lsMonAn.get(index).setGiaMon(giaMon);
            lsMonAn.get(index).setTenMon(tenMon);
            lsMonAn.get(index).setMaLoaiMon(maLoaiMon);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Lỗi Tạo Json", Toast.LENGTH_LONG).show();
        }
    }
    public static int FindDinningMaMon(ArrayList<MonAn> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaMon() == id) {
                return i;
            }
        }
        return -1;
    }
    private void refreshListView() {
        adapterMonAn = new CustomAdapterMonAn(getApplicationContext(), R.layout.activity_item_mon_an, lsMonAn);
        lvMonAn.setAdapter(adapterMonAn);
        adapterMonAn.notifyDataSetChanged();
    }
    //Them hinh anh
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }
}


