package com.upao.cliente.clubdelpadrino_client.api;

import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.Documento;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DocumentoApi {
    String base = "api/documento";
    @Multipart
    @POST(base)
    Call<GenericResponse<Documento>> save(@Part MultipartBody.Part file, @Part("nombre")RequestBody requestBody);
}
