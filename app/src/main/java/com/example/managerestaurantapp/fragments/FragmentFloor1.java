package com.example.managerestaurantapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managerestaurantapp.R;
import com.example.managerestaurantapp.models.DiningTable;
import com.example.managerestaurantapp.services.ApiService;
import com.example.managerestaurantapp.interfaces.OnItemClickListener;
import com.example.managerestaurantapp.activities.ActivityOrder;
import com.example.managerestaurantapp.adapters.AdapterTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFloor1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFloor1 extends Fragment implements OnItemClickListener {
    RecyclerView rcvTable;
    AdapterTable adapterTable;
    List<DiningTable> lstTable = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentFloor1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFloor1.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFloor1 newInstance(String param1, String param2) {
        FragmentFloor1 fragment = new FragmentFloor1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_floor1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvTable = (RecyclerView) view.findViewById(R.id.rcvTable);

        GridLayoutManager layout = new GridLayoutManager(getContext(), 2);
        rcvTable.setLayoutManager(layout);

        loadListTable();
    }

    void loadListTable() {
        ApiService.apiService.getAllTables().enqueue(new Callback<List<DiningTable>>() {
            @Override
            public void onResponse(Call<List<DiningTable>> call, Response<List<DiningTable>> response) {
                if(response.isSuccessful()){
                    lstTable = response.body();

                    loadDataAdapterTable();
                }
            }

            @Override
            public void onFailure(Call<List<DiningTable>> call, Throwable t) {

            }
        });
    }

    void loadDataAdapterTable(){
        lstTable = lstTable.stream().filter(r -> r.getTableFloor() == 1).collect(Collectors.toList());
        adapterTable = new AdapterTable(lstTable, this);
        rcvTable.setAdapter(adapterTable);
    }

    @Override
    public void onItemClick(int i) {
        DiningTable table = lstTable.get(i);
        Intent intentTable = new Intent(getActivity(), ActivityOrder.class);

        Bundle bundleDataTable = new Bundle();
        bundleDataTable.putInt("tableId", table.getTableID());
        intentTable.putExtras(bundleDataTable);
        startActivity(intentTable);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataAdapterTable();
    }
}