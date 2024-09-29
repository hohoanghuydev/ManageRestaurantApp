package com.example.managerestaurantapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.models.MonAn;

import java.util.ArrayList;

public class CustomAdapterMonAn extends ArrayAdapter<MonAn> {
    private Context context;
    private int layoutItem;
    private ArrayList<MonAn> dsMonAn;

    public CustomAdapterMonAn(@NonNull Context context, int layoutItem, @NonNull ArrayList<MonAn> dsMonAn) {
        super(context, layoutItem, dsMonAn);
        this.context = context;
        this.layoutItem = layoutItem;
        this.dsMonAn = dsMonAn;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MonAn monAn = dsMonAn.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutItem, parent, false);
        }
        ImageView imgAnh = convertView.findViewById(R.id.imgAnh);
//        Picasso.get().load(monAn.getImageURL()).into(imgAnh);
        TextView tvMaMonAn = convertView.findViewById(R.id.tvMaMonAn);
        tvMaMonAn.setText(String.valueOf(monAn.getMaMon()));
        TextView tvTenMonAn = convertView.findViewById(R.id.tvTenMonAn);
        tvTenMonAn.setText(monAn.getTenMon());
        TextView tvDonGia = convertView.findViewById(R.id.tvDonGia);
        tvDonGia.setText(String.valueOf(monAn.getGiaMon()));
        TextView tvMaLoaiMon = convertView.findViewById(R.id.tvMaLoaiMon);
        tvMaLoaiMon.setText(String.valueOf(monAn.getMaLoaiMon()));
        return convertView;
    }
}
