package com.upao.cliente.clubdelpadrino_client.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.Categoria;
import com.upao.cliente.clubdelpadrino_client.repository.CategoriaRepository;

import java.util.List;

public class CategoriaViewModel extends AndroidViewModel {
    private final CategoriaRepository repository;

    public CategoriaViewModel(@NonNull Application application) {
        super(application);
        this.repository = CategoriaRepository.getInstance();
    }

    public LiveData<GenericResponse<List<Categoria>>> listrCategoriasActivas() {
        return this.repository.listarCategoriasActivas();
    }
}