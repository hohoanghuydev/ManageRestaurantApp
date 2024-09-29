package com.example.managerestaurantapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.models.Order;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class BottomSheetDialogOrder extends BottomSheetDialogFragment {
    private static final String KEY_ORDER_OBJECT = "order";
    private Order order;
    private TextView tvDishName, tvDishPrice;
    private EditText edtQuantity;
    private ImageButton btnPlus, btnSub;
    private Button btnCancel, btnThemMon;
    private OnDataPassListener data;
    public interface OnDataPassListener{
        void onDataPass(Order order);
    }

    public static BottomSheetDialogOrder newInstance(Order order){
        BottomSheetDialogOrder dialogOrder = new BottomSheetDialogOrder();
        Bundle bundleOrder = new Bundle();
        bundleOrder.putSerializable(KEY_ORDER_OBJECT, order);

        dialogOrder.setArguments(bundleOrder);

        return dialogOrder;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundleOrder = getArguments();
        if(bundleOrder != null){
            order = (Order) bundleOrder.get(KEY_ORDER_OBJECT);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        data = (OnDataPassListener) context;
    }

    private void sendOrder(Order order){
        data.onDataPass(order);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet_order, null);

        dialog.setContentView(view);

        initView(view);
        setDataOrder();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                quantity++;
                edtQuantity.setText(String.valueOf(quantity));
            }
        });
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                quantity--;
                if(quantity == 0)
                {
                    quantity = 1;
                }
                edtQuantity.setText(String.valueOf(quantity));
            }
        });
        btnThemMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                order.setQuantity(quantity);
                sendOrder(order);
            }
        });
        return dialog;
    }

    private void setDataOrder() {
        if(order == null){
            return;
        }
        tvDishName.setText(order.getDishName());
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setCurrencySymbol("₫");
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0", symbols);
        tvDishPrice.setText(decimalFormat.format(order.getDishPrice()) + " đ");
        edtQuantity.setText(String.valueOf(order.getQuantity()));
    }

    private void initView(View view) {
        tvDishName = view.findViewById(R.id.tvDishName);
        tvDishPrice = view.findViewById(R.id.tvUnitPrice);
        edtQuantity = view.findViewById(R.id.edtQuantity);
        btnPlus = view.findViewById(R.id.btnPlus);
        btnSub = view.findViewById(R.id.btnSub);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnThemMon = view.findViewById(R.id.btnThemMon);
    }
}
