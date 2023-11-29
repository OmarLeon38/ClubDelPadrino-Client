package com.upao.cliente.clubdelpadrino_client.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upao.cliente.clubdelpadrino_client.R;
import com.upao.cliente.clubdelpadrino_client.adapter.ProductoCarritoAdapter;
import com.upao.cliente.clubdelpadrino_client.communication.CarritoCommunication;
import com.upao.cliente.clubdelpadrino_client.entity.service.DetallePedido;
import com.upao.cliente.clubdelpadrino_client.entity.service.Usuario;
import com.upao.cliente.clubdelpadrino_client.entity.service.dto.GenerarPedidoDTO;
import com.upao.cliente.clubdelpadrino_client.utils.Carrito;
import com.upao.cliente.clubdelpadrino_client.utils.DateSerializer;
import com.upao.cliente.clubdelpadrino_client.utils.TimeSerializer;
import com.upao.cliente.clubdelpadrino_client.viewmodel.PedidoViewModel;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ProductosCarritoActivity extends AppCompatActivity implements CarritoCommunication {
    private PedidoViewModel pedidoViewModel;
    private ProductoCarritoAdapter adapter;
    private RecyclerView rcvCarritoCompras;
    private Button btnRealizarCompra;

    final Gson g = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_carrito);
        init();
        initViewModel();
        initAdapter();
    }

    private void init() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_atras);
        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
            this.overridePendingTransition(R.anim.right_in, R.anim.right_out);
        });
        rcvCarritoCompras = findViewById(R.id.rcvCarritoCompras);
        btnRealizarCompra = findViewById(R.id.btnRealizarCompra);
        btnRealizarCompra.setOnClickListener(v -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String pref = preferences.getString("UsuarioJson", "");
            Usuario u = g.fromJson(pref, Usuario.class);
            int idC = u.getCliente().getId();
            if (idC != 0) {
                if (Carrito.getDetallePedidos().isEmpty()) {
                    toastIncorrecto("¡El carrito de compras está vacio!");
                } else {
                    toastCorrecto("Procesando pedido...");
                    registrarPedido(idC);
                }
            } else {
                toastIncorrecto("Inicie sesión para realizar un pedido");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
    }

    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.pedidoViewModel = vmp.get(PedidoViewModel.class);
    }

    private void initAdapter() {
        adapter = new ProductoCarritoAdapter(Carrito.getDetallePedidos(), this);
        rcvCarritoCompras.setLayoutManager(new LinearLayoutManager(this));
        rcvCarritoCompras.setAdapter(adapter);
    }

    private void registrarPedido(int idC) {
        ArrayList<DetallePedido> detallePedidos = Carrito.getDetallePedidos();
        GenerarPedidoDTO dto = new GenerarPedidoDTO();
        java.util.Date date = new java.util.Date();
        dto.getPedido().setFechaCompra(new Date(date.getTime()));
        dto.getPedido().setMonto(getTotalV(detallePedidos));
        dto.getCliente().setId(idC);
        dto.setDetallePedido(detallePedidos);
        this.pedidoViewModel.guardarPedido(dto).observe(this, response -> {
            if (response.getRpta() == 1){
                toastCorrecto("¡Pedido registrado correctamente!");
                Carrito.limpiar();
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            } else {
                toastIncorrecto("¡Ocurrió un error al registrar el pedido!");
            }
        });
    }

    private double getTotalV(List<DetallePedido> detalles) {
        float total = 0;
        for (DetallePedido dp : detalles) {
            total += dp.getTotal();
        }
        return total;
    }

    public void toastCorrecto(String texto) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView textView = layouView.findViewById(R.id.txtMensajeToastOk);
        textView.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
        toast.show();
    }

    public void toastIncorrecto(String texto) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.custom_toast_no_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_no_ok));
        TextView textView = layouView.findViewById(R.id.txtMensajeToastNoOk);
        textView.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
        toast.show();

    }

    @Override
    public void eliminarDetalle(int idP) {
        Carrito.eliminar(idP);
        adapter.notifyDataSetChanged();
    }
}