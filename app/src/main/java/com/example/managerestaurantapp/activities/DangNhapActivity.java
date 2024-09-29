package com.example.managerestaurantapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.managerestaurantapp.MainActivity;
import com.example.managerestaurantapp.services.Api;
import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.services.RetrofitClient;
import com.example.managerestaurantapp.utils.Util;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdk, txtquenmk;
    EditText edtacc, edtpass;
    Button btndangnhap;
    Api api;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        addCTRLs();
        addEvents();

        if(!isConnected(this)){
            Toast.makeText(getApplicationContext(), "Không có Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void addCTRLs(){
        Paper.init(this);

        api = RetrofitClient.getInstance(Util.BASE_URL).create(Api.class);

        txtdk = findViewById(R.id.tvTrang_DangKi);
        edtacc = findViewById(R.id.edtTaiKhoan_DN);
        edtpass = findViewById(R.id.edtMatKhau_DN);
        btndangnhap = findViewById(R.id.btnDangNhap);
        txtquenmk = findViewById(R.id.tvQuenMK);

        // Đọc dữ liệu từ Paper
        if (Paper.book().read("acc") != null && Paper.book().read("pass") != null){
            edtacc.setText(Paper.book().read("acc"));
            edtpass.setText(Paper.book().read("pass"));
        }
    }
    private void addEvents(){
        txtdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DangKiActivity.class);
                startActivity(intent);
            }
        });
        txtquenmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuenMKActivity.class);
                startActivity(intent);
            }
        });
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangNhap();
            }
        });
    }

    private void dangNhap() {
        String str_acc = edtacc.getText().toString().trim();
        String str_pass = edtpass.getText().toString().trim();

        if (TextUtils.isEmpty(str_acc)){
            Toast.makeText(getApplicationContext(), "Vui Lòng Nhập Tài Khoản", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(), "Vui Lòng Nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
        } else {
            //save
            Paper.book().write("acc", str_acc);
            Paper.book().write("pass", str_pass);

            compositeDisposable.add(api.dangNhap(str_acc, str_pass)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                       userModel -> {
                            if (userModel.isSuccess()){
                                Toast.makeText(getApplicationContext(), "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();

                                Util.user_current = userModel.getResult().get(0);

                                Intent intent;
                                String role = Util.user_current.getRole();

                                if (role.equals("admin")) {
                                    intent = new Intent(getApplicationContext(), AdminActivity.class);
                                } else if (role.equals("quanly")) {
                                    intent = new Intent(getApplicationContext(), AdminActivity.class);
                                } else {
                                    intent = new Intent(getApplicationContext(), ActivityTable.class);
                                }

                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                       },
                       throwable -> {
                           Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                    ));
        }
    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected()) ){
            return true;
        } else{
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.user_current.getAccountName() != null && Util.user_current.getPassword() != null){
            edtacc.setText(Util.user_current.getAccountName());
            edtpass.setText(Util.user_current.getPassword());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
