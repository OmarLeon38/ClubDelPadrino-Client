package com.upao.cliente.clubdelpadrino_client.activity.ui.compras;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upao.cliente.clubdelpadrino_client.R;
import com.upao.cliente.clubdelpadrino_client.adapter.MisComprasAdapter;
import com.upao.cliente.clubdelpadrino_client.communication.AnularPedidoCommunication;
import com.upao.cliente.clubdelpadrino_client.communication.Communication;
import com.upao.cliente.clubdelpadrino_client.entity.service.Usuario;
import com.upao.cliente.clubdelpadrino_client.utils.DateSerializer;
import com.upao.cliente.clubdelpadrino_client.utils.TimeSerializer;
import com.upao.cliente.clubdelpadrino_client.viewmodel.PedidoViewModel;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class MisComprasFragment extends Fragment implements Communication, AnularPedidoCommunication {
    private PedidoViewModel pedidoViewModel;
    private RecyclerView rcvPedidos;
    private MisComprasAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mis_compras, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initViewModel();
        initAdapter();
        loadData();
    }

    private void init(View v) {
        rcvPedidos = v.findViewById(R.id.rcvMisCompras);
    }

    private void initViewModel() {
        pedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);
    }

    private void initAdapter() {
        adapter = new MisComprasAdapter(new ArrayList<>(), this, this);
        rcvPedidos.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rcvPedidos.setAdapter(adapter);
    }

    private void loadData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        final Gson g = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        String usuarioJson = sp.getString("UsuarioJson", null);
        if (usuarioJson != null) {
            final Usuario u = g.fromJson(usuarioJson, Usuario.class);
            this.pedidoViewModel.listarPedidosPorCliente(u.getCliente().getId()).observe(getViewLifecycleOwner(), response -> {
                adapter.updateItems(response.getBody());
            });
        }
    }

    @Override
    public void showDetails(Intent i) {
        getActivity().startActivity(i);
        getActivity().overridePendingTransition(R.anim.above_in, R.anim.above_out);
    }

    @Override
    public String anularPedido(int id) {
        this.pedidoViewModel.anularPedido(id).observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                this.loadData();
            }
        });
        return "Pedido anulado";
    }
}