package com.upao.cliente.clubdelpadrino_client.api;

import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductoApi {
    String base = "api/producto";

    @GET(base)
    Call<GenericResponse<List<Producto>>> listarProductosPopulares();

    @GET(base + "/{idC}")
    Call<GenericResponse<List<Producto>>> listarProductosPorCategoria(@Path("idC") int idC);
}