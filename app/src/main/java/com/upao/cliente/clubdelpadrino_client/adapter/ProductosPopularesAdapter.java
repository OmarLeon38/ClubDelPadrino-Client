package com.upao.cliente.clubdelpadrino_client.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.upao.cliente.clubdelpadrino_client.R;
import com.upao.cliente.clubdelpadrino_client.api.ConfigApi;
import com.upao.cliente.clubdelpadrino_client.entity.service.DetallePedido;
import com.upao.cliente.clubdelpadrino_client.entity.service.Producto;

import java.util.List;

public class ProductosPopularesAdapter extends RecyclerView.Adapter<ProductosPopularesAdapter.ViewHolder> {
    private List<Producto> productos;

    public ProductosPopularesAdapter(List<Producto> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productos, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(this.productos.get(position));
    }

    @Override
    public int getItemCount() {
        return this.productos.size();
    }

    public void updateItems(List<Producto> productos){
        this.productos.clear();
        this.productos.addAll(productos);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(final Producto p){
            ImageView imgProducto = itemView.findViewById(R.id.imgProducto);
            TextView nameProducto = itemView.findViewById(R.id.nameProducto);
            Button btnComprar = itemView.findViewById(R.id.btnComprar);

            String url = ConfigApi.baseUrlE + "/api/foto/download/" + p.getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(itemView.getContext()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.foto_no_encontrada)
                    .into(imgProducto);
            nameProducto.setText(p.getNombre());
            btnComprar.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "a", Toast.LENGTH_SHORT).show();

            });
        }
    }
}
