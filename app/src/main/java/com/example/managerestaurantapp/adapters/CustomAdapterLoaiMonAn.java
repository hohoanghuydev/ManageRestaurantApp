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
import com.example.managerestaurantapp.models.LoaiMonAn;

import java.util.ArrayList;

public class CustomAdapterLoaiMonAn extends ArrayAdapter<LoaiMonAn>{
    private Context context;
    private int layoutItem;
    private ArrayList<LoaiMonAn> dsLoaiMonAn;

    public CustomAdapterLoaiMonAn(@NonNull Context context, int layoutItem, @NonNull ArrayList<LoaiMonAn> dsLoaiMonAn) {
        super(context, layoutItem, dsLoaiMonAn);
        this.context = context;
        this.layoutItem = layoutItem;
        this.dsLoaiMonAn = dsLoaiMonAn;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LoaiMonAn LoaimonAn = dsLoaiMonAn.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutItem, parent, false);
        }
        TextView tvMaLoaiMon = convertView.findViewById(R.id.tvMaLoaiMonAn);
        tvMaLoaiMon.setText(String.valueOf(LoaimonAn.getMaLoaiMonAn()));
        TextView tvLoaiMonAn = convertView.findViewById(R.id.tvTenLoaiMonAn);
        tvLoaiMonAn.setText(LoaimonAn.getTenLoaiMonAn());
        return convertView;
    }
}
