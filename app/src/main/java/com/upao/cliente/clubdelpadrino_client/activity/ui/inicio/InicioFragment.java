package com.upao.cliente.clubdelpadrino_client.activity.ui.inicio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.upao.cliente.clubdelpadrino_client.R;
import com.upao.cliente.clubdelpadrino_client.adapter.CategoriaAdapter;
import com.upao.cliente.clubdelpadrino_client.adapter.ProductosPopularesAdapter;
import com.upao.cliente.clubdelpadrino_client.communication.BadgeCommunication;
import com.upao.cliente.clubdelpadrino_client.communication.Communication;
import com.upao.cliente.clubdelpadrino_client.entity.service.DetallePedido;
import com.upao.cliente.clubdelpadrino_client.entity.service.Producto;
import com.upao.cliente.clubdelpadrino_client.utils.Carrito;
import com.upao.cliente.clubdelpadrino_client.viewmodel.CategoriaViewModel;
import com.upao.cliente.clubdelpadrino_client.viewmodel.ProductoViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InicioFragment extends Fragment implements Communication, BadgeCommunication {

    private CategoriaViewModel categoriaViewModel;
    private ProductoViewModel productoViewModel;
    private RecyclerView rcvProductosPopulares;
    private ProductosPopularesAdapter adapter;
    private List<Producto> productos = new ArrayList<>();
    private GridView gvCategorias;
    private CategoriaAdapter categoriaAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initAdapter();
        loadData();
    }


    private void init(View v){
        ViewModelProvider vmp = new ViewModelProvider(this);

        categoriaViewModel = vmp.get(CategoriaViewModel.class);
        gvCategorias = v.findViewById(R.id.gvCategorias);

        rcvProductosPopulares = v.findViewById(R.id.rcvPlatillosRecomendados);
        rcvProductosPopulares.setLayoutManager(new GridLayoutManager(getContext(), 1));
        productoViewModel = vmp.get(ProductoViewModel.class);
    }

    private void initAdapter(){
        categoriaAdapter = new CategoriaAdapter(getContext(), R.layout.item_categorias, new ArrayList<>());
        gvCategorias.setAdapter(categoriaAdapter);

        adapter = new ProductosPopularesAdapter(productos, this);
        rcvProductosPopulares.setAdapter(adapter);
    }

    private void loadData(){
        categoriaViewModel.listrCategoriasActivas().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                categoriaAdapter.clear();
                categoriaAdapter.addAll(response.getBody());
                categoriaAdapter.notifyDataSetChanged();
            }else {
                System.out.println("Error al obtener las categorías");
            }
        });
        productoViewModel.listarProductosPopulares().observe(getViewLifecycleOwner(), response -> {
            adapter.updateItems(response.getBody());
        });
    }

    @Override
    public void showDetails(Intent i) {
        getActivity().startActivity(i);
        getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public void add(DetallePedido dp) {
        successMessage(Carrito.agregarProductos(dp));
        BadgeDrawable badgeDrawable = BadgeDrawable.create(this.getContext());
        badgeDrawable.setNumber(Carrito.getDetallePedidos().size());
        BadgeUtils.attachBadgeDrawable(badgeDrawable, getActivity().findViewById(R.id.toolbar), R.id.carritoCompras);
    }

    public void successMessage(String message){
        new SweetAlertDialog(this.getContext(),
                SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("¡Éxito!")
                .setContentText(message)
                .show();
    }
}