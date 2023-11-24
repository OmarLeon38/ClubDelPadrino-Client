package com.upao.cliente.clubdelpadrino_client.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.upao.cliente.clubdelpadrino_client.api.ConfigApi;
import com.upao.cliente.clubdelpadrino_client.api.ProductoApi;
import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoRepository {
    private final ProductoApi api;
    private static ProductoRepository repository;

    public ProductoRepository(ProductoApi api) {
        this.api = ConfigApi.getProductoApi();
    }

    public static ProductoRepository getInstance() {
        if (repository == null) {
            repository = new ProductoRepository(ConfigApi.getProductoApi());
        }
        return repository;
    }

    public LiveData<GenericResponse<List<Producto>>> listarProductosPopulares(){
        final MutableLiveData<GenericResponse<List<Producto>>> mld = new MutableLiveData<>();
        this.api.listarProductosPopulares().enqueue(new Callback<GenericResponse<List<Producto>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Producto>>> call, Response<GenericResponse<List<Producto>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Producto>>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return mld;
    }

public LiveData<GenericResponse<List<Producto>>> listarProductosPorCategoria(int idC){
        final MutableLiveData<GenericResponse<List<Producto>>> mld = new MutableLiveData<>();
        this.api.listarProductosPorCategoria(idC).enqueue(new Callback<GenericResponse<List<Producto>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Producto>>> call, Response<GenericResponse<List<Producto>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Producto>>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return mld;
    }
}