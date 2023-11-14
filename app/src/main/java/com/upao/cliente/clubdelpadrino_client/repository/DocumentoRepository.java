package com.upao.cliente.clubdelpadrino_client.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.upao.cliente.clubdelpadrino_client.api.ConfigApi;
import com.upao.cliente.clubdelpadrino_client.api.DocumentoApi;
import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.Documento;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentoRepository {
    private final DocumentoApi api;
    private static DocumentoRepository repository;

    public DocumentoRepository() {
        this.api = ConfigApi.getDocumentoApi();
    }

    public static DocumentoRepository getInstance(){
        if (repository == null){
            repository = new DocumentoRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<Documento>> saveFoto(MultipartBody.Part part, RequestBody requestBody){
        final MutableLiveData<GenericResponse<Documento>> mld = new MutableLiveData<>();
        this.api.save(part, requestBody).enqueue(new Callback<GenericResponse<Documento>>() {
            @Override
            public void onResponse(Call<GenericResponse<Documento>> call, Response<GenericResponse<Documento>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Documento>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
