package com.example.managerestaurantapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.managerestaurantapp.services.Api;
import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.services.RetrofitClient;
import com.example.managerestaurantapp.utils.Util;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuenMKActivity extends AppCompatActivity {

    EditText edtqmk;
    Button btnqmk;
    Api api;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mk_activity);
        
        addCTRLs();
        addEvents();
    }

    private void addEvents() {
        btnqmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MK();
            }
        });
    }

    private void MK() {
        String str_email = edtqmk.getText().toString().trim();
        if(TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập Email",Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            
            compositeDisposable.add(api.layLaiMK(str_email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userModel -> {
                                if (userModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), userModel.getMessage(),Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                    startActivity(intent);

                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(), userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                    ));
        }
    }

    private void addCTRLs() {
        api = RetrofitClient.getInstance(Util.BASE_URL).create(Api.class);

        edtqmk = findViewById(R.id.edtEmail_QMK);
        btnqmk = findViewById(R.id.btnLayLaiMK);
        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}