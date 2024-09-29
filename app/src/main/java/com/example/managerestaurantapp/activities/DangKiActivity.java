package com.example.managerestaurantapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.managerestaurantapp.services.Api;
import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.services.RetrofitClient;
import com.example.managerestaurantapp.utils.Util;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {

    EditText edtpass, edtrepass, edtacc;
    Button btndki;
    Api api;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);

        addCTRLs();
        addEvents();
    }
    private void addCTRLs(){
        api = RetrofitClient.getInstance(Util.BASE_URL + "/Cong/").create(Api.class);

        edtpass = findViewById(R.id.edtMatKhau_DK);
        edtrepass = findViewById(R.id.edtNhapLaiMK);
        edtacc = findViewById(R.id.edtTaiKhoan_DK);
        btndki = findViewById(R.id.btnDangKi);
    }
    private void addEvents(){
        btndki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKi();
            }
        });
    }

    private void dangKi() {
        String str_pass =  edtpass.getText().toString().trim();
        String str_repass =  edtrepass.getText().toString().trim();
        String str_acc = edtacc.getText().toString().trim();

        if (TextUtils.isEmpty(str_acc)){
            Toast.makeText(getApplicationContext(), "Vui Lòng Nhập Tài Khoản", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(), "Vui Lòng Nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(), "Vui Lòng Nhập Mật Khẩu Xác Nhận", Toast.LENGTH_SHORT).show();
        } else {
            if (str_pass.equals(str_repass)){
                //post data
                compositeDisposable.add(api.dangKi(str_acc, str_pass)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if (userModel.isSuccess()){
                                        Util.user_current.setAccountName(str_acc);
                                        Util.user_current.setPassword(str_pass);

                                        Toast.makeText(getApplicationContext(), "Đăng Kí Thành Công", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                        startActivity(intent);

                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));
            } else {
                Toast.makeText(getApplicationContext(), "Mật khẩu chưa khớp", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
