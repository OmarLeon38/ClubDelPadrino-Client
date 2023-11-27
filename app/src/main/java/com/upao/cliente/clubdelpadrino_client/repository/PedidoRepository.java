package com.upao.cliente.clubdelpadrino_client.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.upao.cliente.clubdelpadrino_client.api.ConfigApi;
import com.upao.cliente.clubdelpadrino_client.api.PedidoApi;
import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.dto.PedidoConDetallesDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoRepository {
    private final PedidoApi api;
    private static PedidoRepository repository;

    public PedidoRepository() {
        this.api = ConfigApi.getPedidoApi();
    }

    public static PedidoRepository getInstance() {
        if (repository == null) {
            repository = new PedidoRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<PedidoConDetallesDTO>>> listarPedidosPorCliente(int idClient) {
        final MutableLiveData<GenericResponse<List<PedidoConDetallesDTO>>> mld = new MutableLiveData<>();
        this.api.listarPedidosPorCliente(idClient).enqueue(new Callback<GenericResponse<List<PedidoConDetallesDTO>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<PedidoConDetallesDTO>>> call, Response<GenericResponse<List<PedidoConDetallesDTO>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<PedidoConDetallesDTO>>> call, Throwable t) {
                mld.setValue(new GenericResponse());
                System.out.println("Error: no se pudieron obtener los pedidos" + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}