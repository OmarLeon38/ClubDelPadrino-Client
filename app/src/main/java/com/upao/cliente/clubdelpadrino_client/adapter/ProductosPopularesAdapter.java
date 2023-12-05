package com.upao.cliente.clubdelpadrino_client.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.upao.cliente.clubdelpadrino_client.R;
import com.upao.cliente.clubdelpadrino_client.activity.DetalleProductoActivity;
import com.upao.cliente.clubdelpadrino_client.api.ConfigApi;
import com.upao.cliente.clubdelpadrino_client.communication.Communication;
import com.upao.cliente.clubdelpadrino_client.entity.service.Producto;
import com.upao.cliente.clubdelpadrino_client.utils.DateSerializer;
import com.upao.cliente.clubdelpadrino_client.utils.TimeSerializer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class ProductosPopularesAdapter extends RecyclerView.Adapter<ProductosPopularesAdapter.ViewHolder> {
    private List<Producto> productos;
    private final Communication communication;

    public ProductosPopularesAdapter(List<Producto> productos, Communication communication) {
        this.productos = productos;
        this.communication = communication;
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
        if (productos != null) {
            this.productos.clear();
            this.productos.addAll(productos);
            this.notifyDataSetChanged();
        } else{
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader("<GenericResponse>...</GenericResponse>"));
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_DOCUMENT) {
                        System.out.println("Start document");
                    } else if(eventType == XmlPullParser.START_TAG) {
                        System.out.println("Start tag "+xpp.getName());
                    } else if(eventType == XmlPullParser.END_TAG) {
                        System.out.println("End tag "+xpp.getName());
                    } else if(eventType == XmlPullParser.TEXT) {
                        System.out.println("Text "+xpp.getText());
                    }
                    eventType = xpp.next();
                }
                System.out.println("End document");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(final Producto p){
            ImageView imgProducto = itemView.findViewById(R.id.imgProducto);
            TextView nameProducto = itemView.findViewById(R.id.nameProducto);
            Button btnComprar = itemView.findViewById(R.id.btnComprar);

            String url = ConfigApi.ipOmarLeon + "/api/foto/download/" + p.getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(itemView.getContext()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.foto_no_encontrada)
                    .into(imgProducto);
            nameProducto.setText(p.getNombre());
            btnComprar.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DetalleProductoActivity.class);
                intent.putExtra("detalleProducto", new Gson().toJson(p));
                itemView.getContext().startActivity(intent);
            });

            //Detalle producto
            itemView.setOnClickListener(view -> {
                final Intent i = new Intent(itemView.getContext(), DetalleProductoActivity.class);
                final Gson g = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateSerializer())
                        .registerTypeAdapter(Time.class, new TimeSerializer())
                        .create();
                i.putExtra("detalleProducto", g.toJson(p));
                communication.showDetails(i);
            });
        }
    }
}
