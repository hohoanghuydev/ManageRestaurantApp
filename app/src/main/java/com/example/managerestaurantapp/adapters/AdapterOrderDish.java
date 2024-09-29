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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterOrderDish extends ArrayAdapter<Dish> {
    int idLayout;
    Context context;
    List<Dish> dishes = new ArrayList<>();

    public AdapterOrderDish(@NonNull Context context, int idLayout, List<Dish> dishes) {
        super(context, idLayout, dishes);
        this.idLayout = idLayout;
        this.context = context;
        this.dishes = dishes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(idLayout, null, false);
        }

        Dish dish = dishes.get(position);

//        CircleImageView imgDish = (CircleImageView) convertView.findViewById(R.id.imgDish);
//        imgDish.setImageResource();

        TextView tvNameDish = (TextView) convertView.findViewById(R.id.tvNameDish);
        tvNameDish.setText(dish.getDishName());

        TextView tvInfo = (TextView) convertView.findViewById(R.id.tvInfo);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setCurrencySymbol("₫");
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0", symbols);
//        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvInfo.setText(decimalFormat.format(dish.getUnitPrice()) + " đ");

        return convertView;
    }
}
