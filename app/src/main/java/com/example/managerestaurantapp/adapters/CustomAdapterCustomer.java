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
import com.example.managerestaurantapp.models.Customer;

import java.util.ArrayList;

public class CustomAdapterCustomer extends ArrayAdapter {

    Context context;
    int layoutItem;

    ArrayList<Customer> listCustomer = new ArrayList<>();

    public CustomAdapterCustomer(@NonNull Context context, int resource, @NonNull ArrayList<Customer> listCustomer) {
        super(context, resource, listCustomer);
        this.context =  context;
        this.layoutItem = resource;
        this.listCustomer = listCustomer;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Customer customer = listCustomer.get(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(layoutItem,null);
        }

        TextView tvID = (TextView) convertView.findViewById(R.id.tv_CustomerID);
        String id = String.valueOf(customer.getCustomerId());
        tvID.setText(id);
        TextView tvname = (TextView) convertView.findViewById(R.id.tv_CustomerName);
        tvname.setText(customer.getCustomerName());
        TextView tvAdress = (TextView) convertView.findViewById(R.id.tv_Adress);
        tvAdress.setText(customer.getAdress());
        TextView tvPhone= (TextView) convertView.findViewById(R.id.tv_PhoneNumber);
        tvPhone.setText(customer.getPhoneNumber());

        return convertView;
    }
}
