package com.upao.cliente.clubdelpadrino_client.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.Foto;
import com.upao.cliente.clubdelpadrino_client.repository.FotoRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FotoViewModel extends AndroidViewModel {
    private final FotoRepository repository;

    public FotoViewModel(@NonNull Application application) {
        super(application);
        this.repository = FotoRepository.getInstance();
    }

    public LiveData<GenericResponse<Foto>> save(MultipartBody.Part part, RequestBody requestBody){
        return this.repository.saveFoto(part, requestBody);
    }
}