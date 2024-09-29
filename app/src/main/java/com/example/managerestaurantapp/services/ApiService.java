package com.example.managerestaurantapp.services;

import com.example.managerestaurantapp.models.DiningTable;
import com.example.managerestaurantapp.models.Dish;
import com.example.managerestaurantapp.models.DishCategory;
import com.example.managerestaurantapp.models.Message;
import com.example.managerestaurantapp.models.Payment;
import com.example.managerestaurantapp.models.Revenue;
import com.example.managerestaurantapp.models.TableDish;
import com.example.managerestaurantapp.models.TableService;
import com.example.managerestaurantapp.utils.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl(Util.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("Huy/diningtable/")
    Call<List<DiningTable>> getAllTables();

    @GET("Huy/dishes/")
    Call<List<Dish>> getAllDishes();

    @GET("Huy/dishes/{id}")
    Call<Dish> getDish(@Path("id") int dishId);

    @GET("Huy/categories")
    Call<List<DishCategory>> getAllCategories();

    @GET("Huy/tableservice")
    Call<List<TableService>> getAllServices();

    @GET("Huy/tableservice")
    Call<TableService> getServiceNotPay(@Query("tableid") int tableId);

    @GET("Huy/tabledish/{id}")
    Call<List<TableDish>> getDishesByService(@Path("id") int serviceId);

    @FormUrlEncoded
    @POST("Huy/tableservice/")
    Call<Message> createNewServiceByTable(@Field("CustomerID") int customerId, @Field("TableID") int tableId, @Field("StartTime") String startTime);

    @GET("Huy/payment/{id}")
    Call<Payment> getPaymentByService(@Path("id") int serviceId);
    @FormUrlEncoded
    @POST("Huy/payment/")
    Call<Message> createNewPaymentByService(@Field("ServiceID") int serviceId, @Field("EmployeeID") int employeeId);

    @FormUrlEncoded
    @PATCH("Huy/payment/{id}")
    Call<Message> updatePayment(@Path("id") int serviceId, @Field("PaymentTime") String paymentTime);

    @GET("Huy/tabledish/{id}")
    Call<TableDish> getDishOrder(@Path("id") int serviceId, @Query("dishid") int dishId);

    @FormUrlEncoded
    @POST("Huy/tabledish/")
    Call<Message> addDishToOrders(@Field("ServiceID") int serviceId, @Field("DishID") int dishId, @Field("Quantity") int quantity, @Field("UnitPrice") int unitPrice, @Field("Note") String note);

    @FormUrlEncoded
    @PATCH("Huy/tabledish/{id}")
    Call<Message> updateQuantityDish(@Path("id") int serviceId, @Query("dishid") int dishId, @Field("Quantity") int quantity, @Field("UnitPrice") int unitPrice, @Field("Note") String note);

    @GET("Thien/getDoanhThu.php")
    Call<List<Revenue>> getDoanhThu(@Query("startDate") String startDate,
                                    @Query("endDate") String endDate);

    @GET("Thien/getDoanhThuCaoNhat.php")
    Call<Revenue> getMaxDoanhThu(@Query("startDate") String startDate,
                                 @Query("endDate") String endDate);
    @GET("Thien/getDoanhThuThapNhat.php")
    Call<Revenue> getMinDoanhThu(@Query("startDate") String startDate,
                                 @Query("endDate") String endDate);

}
