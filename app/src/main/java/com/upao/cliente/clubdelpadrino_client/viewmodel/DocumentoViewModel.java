package com.upao.cliente.clubdelpadrino_client.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.Documento;
import com.upao.cliente.clubdelpadrino_client.repository.DocumentoRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DocumentoViewModel extends AndroidViewModel {
    private final DocumentoRepository repository;

    public DocumentoViewModel(@NonNull Application application) {
        super(application);
        this.repository = DocumentoRepository.getInstance();
    }

    public LiveData<GenericResponse<Documento>> save(MultipartBody.Part part, RequestBody requestBody){
        return this.repository.saveFoto(part, requestBody);
    }
}
