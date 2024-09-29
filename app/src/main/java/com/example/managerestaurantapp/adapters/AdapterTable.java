package com.example.managerestaurantapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.interfaces.OnItemClickListener;
import com.example.managerestaurantapp.models.DiningTable;
import com.example.managerestaurantapp.models.TableService;
import com.example.managerestaurantapp.services.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterTable extends RecyclerView.Adapter<AdapterTable.TableViewHolder> {
    List<DiningTable> lstTable;
    OnItemClickListener event;

    public AdapterTable(List<DiningTable> lstTable, OnItemClickListener event) {
        this.lstTable = lstTable;
        this.event = event;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_table, parent, false);

        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        DiningTable table = lstTable.get(position);

        if(table == null){
            return;
        }

        holder.tvMaBan.setText("Bàn " + table.getTableID());

        ApiService.apiService.getServiceNotPay(table.getTableID()).enqueue(new Callback<TableService>() {
            @Override
            public void onResponse(Call<TableService> call, Response<TableService> response) {
                if(response.isSuccessful()) {
                    TableService checkService = response.body();

                    if(checkService.getServiceID() == 0) {
                        holder.tvTableState.setText("Bàn trống");
                    } else {
                        holder.tvTableState.setText("Đã có người");
                    }
                }
            }

            @Override
            public void onFailure(Call<TableService> call, Throwable t) {

            }
        });

        holder.tvSeatCount.setText(table.getSeatCount() + " người");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(event != null){
                    event.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstTable != null ? lstTable.size() : 0;
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder{
        ImageView imgTable;
        TextView tvMaBan, tvSeatCount, tvTableState;
        public TableViewHolder(@NonNull View itemView) {
            super(itemView);

            imgTable = itemView.findViewById(R.id.imgTable);
            tvMaBan = itemView.findViewById(R.id.tvMaBan);
            tvSeatCount = itemView.findViewById(R.id.tvSeatCount);
            tvTableState = itemView.findViewById(R.id.tvTableState);
        }
    }
}
