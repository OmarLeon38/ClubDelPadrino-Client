package com.upao.cliente.clubdelpadrino_client.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.upao.cliente.clubdelpadrino_client.api.ConfigApi;
import com.upao.cliente.clubdelpadrino_client.api.FotoApi;
import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.Foto;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FotoRepository {
    private final FotoApi api;
    private static FotoRepository repository;

    public FotoRepository() {
        this.api = ConfigApi.getFotoApi();
    }

    public static FotoRepository getInstance(){
        if (repository == null){
            repository = new FotoRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<Foto>> saveFoto(MultipartBody.Part part, RequestBody requestBody){
        final MutableLiveData<GenericResponse<Foto>> mld = new MutableLiveData<>();
        this.api.save(part, requestBody).enqueue(new Callback<GenericResponse<Foto>>() {
            @Override
            public void onResponse(Call<GenericResponse<Foto>> call, Response<GenericResponse<Foto>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Foto>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}