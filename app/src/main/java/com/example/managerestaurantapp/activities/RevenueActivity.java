package com.example.managerestaurantapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.models.Revenue;
import com.example.managerestaurantapp.services.ApiService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevenueActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog, endDatePickerDialog;
    private Button buttonStartDate, buttonEndDate, buttonThongKe;

    private TextView textViewMaxDoanhThu, textViewMinDoanhThu, textViewShowInfor;
    private ArrayList arrayList;
    private List<Revenue> lstRV = null;

    LineChart lineChart ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);
        initDatePicker();
        initEndDatePicker();
        addControls();
        addEvents();
    }
    private void addEvents()
    {
        buttonThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = buttonStartDate.getText().toString();
                String   endDate = buttonEndDate.getText().toString();
                ApiService.apiService.getMaxDoanhThu(startDate, endDate).enqueue(new Callback<Revenue>() {
                    @Override
                    public void onResponse(Call<Revenue> call, Response<Revenue> response) {
                        Toast.makeText(RevenueActivity.this, "Success Max", Toast.LENGTH_SHORT).show();
                        Revenue rv = response.body();
                        if (rv!= null)
                        {
                            textViewMaxDoanhThu.setText(rv.getDate());
                        }
                    }

                    @Override
                    public void onFailure(Call<Revenue> call, Throwable t) {
                        Toast.makeText(RevenueActivity.this, "Failed Max", Toast.LENGTH_SHORT).show();

                    }
                });
                ApiService.apiService.getMinDoanhThu(startDate, endDate).enqueue(new Callback<Revenue>() {
                    @Override
                    public void onResponse(Call<Revenue> call, Response<Revenue> response) {
                        Revenue rv = response.body();


                        if (rv!= null)
                        {
                            textViewMinDoanhThu.setText(rv.getDate());
                            Toast.makeText(RevenueActivity.this, "Success Min" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Revenue> call, Throwable t) {
                        Toast.makeText(RevenueActivity.this, "Failed Min", Toast.LENGTH_SHORT).show();
                    }
                });
                ApiService.apiService.getDoanhThu(startDate, endDate).enqueue(new Callback<List<Revenue>>() {
                    @Override
                    public void onResponse(Call<List<Revenue>> call, Response<List<Revenue>> response) {
                        if(response.body() != null) {
                            lstRV = response.body();
                            List<Entry> entries = new ArrayList<>();
                            for (Revenue revenue : lstRV) {
                                entries.add(new Entry(
                                        Float.parseFloat(revenue.getDate().substring(revenue.getDate().length() - 2)),
                                        Float.parseFloat(revenue.getRevenue()) / 1000
                                ));
                            }

                            LineDataSet dataSet = new LineDataSet(entries, "Doanh thu");
                            dataSet.setColors(Color.parseColor("#006769"));
                            dataSet.setLineWidth(3f);
                            dataSet.setValueTextSize(14f);
                            LineData lineData = new LineData(dataSet);
                            lineChart.setData(lineData);
                            lineChart.getDescription().setEnabled(false);
                            lineChart.setDrawGridBackground(false);
                            lineChart.animateX(1000);
                            lineChart.invalidate();
                        }else{
                            Toast.makeText(RevenueActivity.this,"Failed re",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Revenue>> call, Throwable t) {

                    }
                });
            }
        });
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = e.getX();
                float y = e.getY();
                Toast.makeText(RevenueActivity.this, "Ngày: " + x + ", Doanh thu: " + y, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(){

            }

        });
    }
    String getToday()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month+=1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }
    void addControls()
    {
        buttonThongKe = (Button) findViewById(R.id.buttonThongKe);
        buttonStartDate = (Button) findViewById(R.id.buttonStartDate);
        buttonStartDate.setText(getToday());
        buttonEndDate = (Button) findViewById(R.id.buttonEndDate);
        buttonEndDate.setText(getToday());
        textViewMaxDoanhThu = (TextView) findViewById(R.id.textViewDoanhThuMax);
        textViewMinDoanhThu = (TextView) findViewById(R.id.textViewDoanhThuMin);
        lineChart = (LineChart) findViewById(R.id.linechart);
    }
    void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = makeDateString(day, month, year);
                buttonStartDate.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = android.app.AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener,year, month, day);


    }
    void initEndDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            monthOfYear += 1;
            String date = makeDateString(dayOfMonth, monthOfYear, year);
            buttonEndDate.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        endDatePickerDialog = new DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT, dateSetListener, year, month, day);
    }
    String makeDateString(int day, int month, int year)
    {
        return year + "-" + month + "-" + day;
    }


    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
    public void openEndDate(View view)
    {
        endDatePickerDialog.show();
    }

}