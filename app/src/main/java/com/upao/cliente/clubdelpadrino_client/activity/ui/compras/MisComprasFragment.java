package com.upao.cliente.clubdelpadrino_client.activity.ui.compras;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MisComprasFragment extends Fragment implements Communication, AnularPedidoCommunication {
    private ActivityResultLauncher<String> perReqLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            successMessage("Permiso concedido");
        }else {
            errorMessage("Permiso denegado, no podemos realizar la exportación");
        }
    });

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
    public void exportInvoice(int idClient, int idOrden, String fileName) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            pedidoViewModel.exportInvoice(idClient, idOrden).observe(getViewLifecycleOwner(), response -> {
                if (response.getRpta() == 1) {
                    try {
                        boolean folderCreated = true;
                        File path = requireContext().getExternalFilesDir("/facturas");
                        if (!path.exists()) {
                            folderCreated = false;
                            Toast.makeText(requireContext(), "No se pudo crear la carpeta necesaria", Toast.LENGTH_SHORT);
                        }
                        if (folderCreated){
                            byte[] bytes = response.getBody().bytes();
                            File file = new File(path, fileName);
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(bytes);
                            fileOutputStream.close();
                            successMessage("Factura exportada correctamente" + file.getAbsolutePath());
                        }
                    }catch (Exception e) {
                        errorMessage("No se pudo exportar la factura");
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT);
                }
            });
        }else {
            new SweetAlertDialog(requireContext(),
                    SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Permisos")
                    .setContentText("Se requieren permisos para poder exportar la factura")
                    .setConfirmButton("Conceder", sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                        perReqLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }).setCancelButton("Quizá más tarde", SweetAlertDialog::dismissWithAnimation).show();
        }
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

    public void successMessage(String message) {
        new SweetAlertDialog(requireContext(),
                SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Éxito")
                .setContentText(message)
                .show();
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(requireContext(),
                SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText(message)
                .show();
    }
}