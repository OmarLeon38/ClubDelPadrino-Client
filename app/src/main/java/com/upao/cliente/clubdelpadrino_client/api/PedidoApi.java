package com.upao.cliente.clubdelpadrino_client.api;

import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.dto.PedidoConDetallesDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PedidoApi {
    String base = "api/pedido";
    @GET(base + "/misPedidos/{idClient}")
    Call<GenericResponse<List<PedidoConDetallesDTO>>> listarPedidosPorCliente(@Path("idClient") int idClient);
}
