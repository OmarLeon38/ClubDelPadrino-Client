package com.upao.cliente.clubdelpadrino_client.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.Producto;
import com.upao.cliente.clubdelpadrino_client.repository.ProductoRepository;

import java.util.List;

public class ProductoViewModel extends AndroidViewModel {
    private final ProductoRepository repository;

    public ProductoViewModel(@NonNull Application application) {
        super(application);
        repository = ProductoRepository.getInstance();
    }

    public LiveData<GenericResponse<List<Producto>>> listarProductosPopulares(){
        return repository.listarProductosPopulares();
    }

    public LiveData<GenericResponse<List<Producto>>> listarProductosPorCategoria(int idC){
        return repository.listarProductosPorCategoria(idC);
    }
}