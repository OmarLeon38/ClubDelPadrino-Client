package com.upao.cliente.clubdelpadrino_client.api;

import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.Pedido;
import com.upao.cliente.clubdelpadrino_client.entity.service.dto.GenerarPedidoDTO;
import com.upao.cliente.clubdelpadrino_client.entity.service.dto.PedidoConDetallesDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface PedidoApi {
    String base = "api/pedido";
    @GET(base + "/misPedidos/{idClient}")
    Call<GenericResponse<List<PedidoConDetallesDTO>>> listarPedidosPorCliente(@Path("idClient") int idClient);

    @POST(base)
    Call<GenericResponse<GenerarPedidoDTO>> guardarPedido(@Body GenerarPedidoDTO dto);

    @DELETE(base + "/{id}")
    Call<GenericResponse<Pedido>> anularPedido(@Path("id") int id);

    @Streaming
    @GET(base + "/exportInvoice")
    Call<ResponseBody> exportInvoicePDF(@Query("idClient") int idClient, @Query("idOrden") int idOrden);
}