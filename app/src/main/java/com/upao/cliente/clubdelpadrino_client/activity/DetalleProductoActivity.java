package com.upao.cliente.clubdelpadrino_client.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.upao.cliente.clubdelpadrino_client.R;
import com.upao.cliente.clubdelpadrino_client.api.ConfigApi;
import com.upao.cliente.clubdelpadrino_client.entity.service.DetallePedido;
import com.upao.cliente.clubdelpadrino_client.entity.service.Producto;
import com.upao.cliente.clubdelpadrino_client.utils.Carrito;
import com.upao.cliente.clubdelpadrino_client.utils.DateSerializer;
import com.upao.cliente.clubdelpadrino_client.utils.TimeSerializer;

import java.sql.Date;
import java.sql.Time;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetalleProductoActivity extends AppCompatActivity {
    private ImageView imgProductoDetalle;
    private Button btnAgregarCarrito, btnComprar;
    private TextView tvNameProductoDetalle, tvPrecioProductoDetalle, tvDescripcionProductoDetalle;

    final Gson g = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();

    Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
        init();
        loadData();
    }

    private void init(){
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_atras);
        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
            this.overridePendingTransition(R.anim.right_in, R.anim.right_out);
        });
        this.imgProductoDetalle = this.findViewById(R.id.imgProductoDetalle);
        this.btnAgregarCarrito = this.findViewById(R.id.btnAgregarCarrito);
        this.btnComprar = this.findViewById(R.id.btnComprar);

        LinearLayout contenedor = findViewById(R.id.ComprarAhora);

        this.tvNameProductoDetalle = this.findViewById(R.id.tvNameProductoDetalle);
        this.tvPrecioProductoDetalle = this.findViewById(R.id.tvPrecioProductoDetalle);
        this.tvDescripcionProductoDetalle = this.findViewById(R.id.tvDescripcionProductoDetalle);
    }

    public void agregarAlCarrito() {
        DetallePedido detallePedido = new DetallePedido();
        detallePedido.setProducto(producto);
        detallePedido.setCantidad(1);
        detallePedido.setPrecio(producto.getPrecio());
        successMessage(Carrito.agregarProductos(detallePedido));
    }

    private void loadData() {
        final String detalleString = this.getIntent().getStringExtra("detalleProducto");
        if (detalleString != null) {
            producto = g.fromJson(detalleString, Producto.class);
            this.tvNameProductoDetalle.setText(producto.getNombre());
            this.tvPrecioProductoDetalle.setText(String.format(Locale.ENGLISH, "S/%.2f", producto.getPrecio()));
            this.tvDescripcionProductoDetalle.setText(producto.getDescripcionProducto());
            String url = ConfigApi.baseUrlE + "/api/foto/download/" + producto.getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(this)
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.foto_no_encontrada)
                    .into(this.imgProductoDetalle);
        } else {
            System.out.println("Error: no se pudieron encontrar detalles del producto");
        }

        //Agregar al Carrito
        this.btnAgregarCarrito.setOnClickListener(v -> agregarAlCarrito());
        //Compra individual
        this.btnComprar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ingrese la cantidad");
            DetallePedido detallePedido = new DetallePedido();
            final EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(editText);

            builder.setPositiveButton("Aceptar", (dialog, which) -> {
                String cantidadString = editText.getText().toString();
                if (!cantidadString.isEmpty()) {
                    int cantidad = Integer.parseInt(cantidadString);
                    if (cantidad >= 1 && cantidad <= producto.getStock()) {
                        detallePedido.setCantidad(cantidad);
                        detallePedido.setProducto(producto);
                        detallePedido.setPrecio(producto.getPrecio());
                        successMessage(Carrito.agregarProductos(detallePedido));
                        // Resto de la lógica para procesar la compra
                    } else if (cantidad > producto.getStock()) {
                        errorMessage("Esa cantidad sobrepasa la cantidad máxima del stock");
                    } else if (cantidad <= 0) {
                        warningMessage("Ese valor no es posible de comprar");
                    } else {
                        errorMessage("ERROR");
                    }
                } else {
                    warningMessage("Ingrese una cantidad válida");
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("¡Operación exitosa!")
                .setContentText(message)
                .show();
    }
    public void warningMessage(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("!Cuidado!")
                .setContentText(message)
                .show();
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("!ERROR!")
                .setContentText(message)
                .show();
    }
}