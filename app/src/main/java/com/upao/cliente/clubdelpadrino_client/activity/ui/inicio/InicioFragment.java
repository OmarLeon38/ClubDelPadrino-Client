package com.upao.cliente.clubdelpadrino_client.activity.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.upao.cliente.clubdelpadrino_client.R;
import com.upao.cliente.clubdelpadrino_client.adapter.CategoriaAdapter;
import com.upao.cliente.clubdelpadrino_client.viewmodel.CategoriaViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InicioFragment extends Fragment {

    private CategoriaViewModel categoriaViewModel;
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
    }

    private void initAdapter(){
        categoriaAdapter = new CategoriaAdapter(getContext(), R.layout.item_categorias, new ArrayList<>());
        gvCategorias.setAdapter(categoriaAdapter);
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
    }
}