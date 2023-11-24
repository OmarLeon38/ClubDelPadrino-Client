package com.upao.cliente.clubdelpadrino_client.api;

import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.Foto;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FotoApi {
    String base = "api/foto";
    @Multipart
    @POST(base)
    Call<GenericResponse<Foto>> save(@Part MultipartBody.Part file, @Part("nombre")RequestBody requestBody);
}