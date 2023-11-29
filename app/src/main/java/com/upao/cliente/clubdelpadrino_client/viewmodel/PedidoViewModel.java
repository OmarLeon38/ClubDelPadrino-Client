package com.upao.cliente.clubdelpadrino_client.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.upao.cliente.clubdelpadrino_client.entity.GenericResponse;
import com.upao.cliente.clubdelpadrino_client.entity.service.Pedido;
import com.upao.cliente.clubdelpadrino_client.entity.service.dto.GenerarPedidoDTO;
import com.upao.cliente.clubdelpadrino_client.entity.service.dto.PedidoConDetallesDTO;
import com.upao.cliente.clubdelpadrino_client.repository.PedidoRepository;

import java.util.List;

public class PedidoViewModel extends AndroidViewModel {
    private final PedidoRepository repository;

    public PedidoViewModel(@NonNull Application application) {
        super(application);
        this.repository = PedidoRepository.getInstance();
    }

    public LiveData<GenericResponse<List<PedidoConDetallesDTO>>> listarPedidosPorCliente(int idClient) {
        return repository.listarPedidosPorCliente(idClient);
    }

    public LiveData<GenericResponse<GenerarPedidoDTO>> guardarPedido(GenerarPedidoDTO dto) {
        return repository.save(dto);
    }

    public LiveData<GenericResponse<Pedido>> anularPedido(int id) {
        return repository.anularPedido(id);
    }
}