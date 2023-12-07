package com.upao.cliente.clubdelpadrino_client.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.upao.cliente.clubdelpadrino_client.R;
import com.upao.cliente.clubdelpadrino_client.api.ConfigApi;
import com.upao.cliente.clubdelpadrino_client.entity.service.DetallePedido;

import java.util.List;
import java.util.Locale;

public class DetalleMisComprasAdapter extends RecyclerView.Adapter<DetalleMisComprasAdapter.ViewHolder> {
    private final List<DetallePedido> detalles;

    public DetalleMisComprasAdapter(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_mis_compras, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(this.detalles.get(position));
    }

    public void updateItems(List<DetallePedido> detalles) {
        this.detalles.clear();
        this.detalles.addAll(detalles);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return detalles.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView txtValueCodDetailPurchases, txtValueProducto, txtValueQuantity, txtValuePrecioProducto;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imgProduct = itemView.findViewById(R.id.imgProduct);
            this.txtValueCodDetailPurchases = itemView.findViewById(R.id.txtValueCodDetailPurchases);
            this.txtValueProducto = itemView.findViewById(R.id.txtValueProducto);
            this.txtValueQuantity = itemView.findViewById(R.id.txtValueQuantity);
            this.txtValuePrecioProducto = itemView.findViewById(R.id.txtValuePrecioProducto);
        }

        public void setItem(final DetallePedido detalle) {
            String url = ConfigApi.baseUrlE + "/api/foto/download/" + detalle.getProducto().getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.foto_no_encontrada)
                    .into(imgProduct);
            txtValueCodDetailPurchases.setText("ORD000" + Integer.toString(detalle.getPedido().getId()));
            txtValueProducto.setText(detalle.getProducto().getNombre());
            txtValueQuantity.setText(Integer.toString(detalle.getCantidad()));
            txtValuePrecioProducto.setText(String.format(Locale.ENGLISH, "S/%.2f", detalle.getPrecio()));
        }
    }
}