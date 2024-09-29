package com.example.managerestaurantapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.models.Dish;
import com.example.managerestaurantapp.models.TableDish;
import com.example.managerestaurantapp.services.ApiService;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterTableDish extends ArrayAdapter<TableDish> {
    int idLayout;
    Context context;
    List<TableDish> lstTableDish = new ArrayList<>();

    public AdapterTableDish(@NonNull Context context, int resource, @NonNull List<TableDish> objects) {
        super(context, resource, objects);
        this.idLayout = resource;
        this.context = context;
        this.lstTableDish = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(idLayout, null, false);
        }

        TableDish tblDish = lstTableDish.get(position);

//        ImageView imgDish = convertView.findViewById(R.id.imgDish);

        TextView tvName = convertView.findViewById(R.id.tvName);

        TextView tvUnitPrice = convertView.findViewById(R.id.tvUnitPrice);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setCurrencySymbol("₫");
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0", symbols);
        tvUnitPrice.setText("Thành tiền: " + decimalFormat.format(tblDish.getUnitPrice()) + " đ");

        TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);
        tvQuantity.setText("Số lượng: " + tblDish.getQuantity());

        ApiService.apiService.getDish(tblDish.getDishID()).enqueue(new Callback<Dish>() {
            @Override
            public void onResponse(Call<Dish> call, Response<Dish> response) {
                if(response.isSuccessful()) {
                    Dish dish = response.body();

                    tvName.setText(dish.getDishName());
                }
            }

            @Override
            public void onFailure(Call<Dish> call, Throwable t) {

            }
        });

        return convertView;
    }
}
