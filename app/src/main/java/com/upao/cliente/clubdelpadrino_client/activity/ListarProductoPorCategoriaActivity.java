package com.upao.cliente.clubdelpadrino_client.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.upao.cliente.clubdelpadrino_client.R;
import com.upao.cliente.clubdelpadrino_client.adapter.ProductosPorCategoriaAdapter;
import com.upao.cliente.clubdelpadrino_client.entity.service.Producto;
import com.upao.cliente.clubdelpadrino_client.viewmodel.ProductoViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListarProductoPorCategoriaActivity extends AppCompatActivity {
    private ProductoViewModel productoViewModel;
    private ProductosPorCategoriaAdapter adapter;
    private List<Producto> productos = new ArrayList<>();
    private RecyclerView rcvProductoPorCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_producto_por_categoria);
        init();
        initViewModel();
        initAdapter();
        loadData();
    }

    private void init(){
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_atras);
        toolbar.setNavigationOnClickListener(view -> {
            this.finish();
            this.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        });
    }

    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.productoViewModel = vmp.get(ProductoViewModel.class);
    }

    private void initAdapter() {
        adapter = new ProductosPorCategoriaAdapter(productos);
        rcvProductoPorCategoria = findViewById(R.id.rcvPlatillosPorCategoria);
        rcvProductoPorCategoria.setAdapter(adapter);
        rcvProductoPorCategoria.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        int idC = getIntent().getIntExtra("idC", 0);
        productoViewModel.listarProductosPorCategoria(idC).observe(this, response -> {
            adapter.updateItems(response.getBody());
        });
    }
}