package com.upao.cliente.clubdelpadrino_client.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upao.cliente.clubdelpadrino_client.R;
import com.upao.cliente.clubdelpadrino_client.activity.ui.compras.DetalleMisComprasActivity;
import com.upao.cliente.clubdelpadrino_client.communication.AnularPedidoCommunication;
import com.upao.cliente.clubdelpadrino_client.communication.Communication;
import com.upao.cliente.clubdelpadrino_client.entity.service.dto.PedidoConDetallesDTO;
import com.upao.cliente.clubdelpadrino_client.utils.DateSerializer;
import com.upao.cliente.clubdelpadrino_client.utils.TimeSerializer;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MisComprasAdapter extends RecyclerView.Adapter<MisComprasAdapter.ViewHolder> {
    private final List<PedidoConDetallesDTO> pedidos;
    private final Communication communication;
    private final AnularPedidoCommunication anularPedidoCommunication;

    public MisComprasAdapter(List<PedidoConDetallesDTO> pedidos, Communication communication, AnularPedidoCommunication anularPedidoCommunication) {
        this.pedidos = pedidos;
        this.communication = communication;
        this.anularPedidoCommunication = anularPedidoCommunication;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mis_compras, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(this.pedidos.get(position));
    }

    public void updateItems(List<PedidoConDetallesDTO> pedido) {
        this.pedidos.clear();
        this.pedidos.addAll(pedido);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.pedidos.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void setItem(final PedidoConDetallesDTO dto) {
            final TextView txtValueCodPurchases = this.itemView.findViewById(R.id.txtValueCodPurchases),
                    txtValueDatePurchases = this.itemView.findViewById(R.id.txtValueDatePurchases),
                    txtValueAmount = this.itemView.findViewById(R.id.txtValueAmount),
                    txtValueOrder = this.itemView.findViewById(R.id.txtValueOrder);
            txtValueCodPurchases.setText("ORD000" + Integer.toString(dto.getPedido().getId()));
            txtValueDatePurchases.setText((dto.getPedido().getFechaCompra()).toString());
            txtValueAmount.setText(String.format(Locale.ENGLISH, "S/%.2f", dto.getPedido().getMonto()));
            txtValueOrder.setText(dto.getPedido().isAnularPedido() ? "Pedido anulado" : "Enviado");

            itemView.setOnClickListener(view -> {
                final Intent i = new Intent(itemView.getContext(), DetalleMisComprasActivity.class);
                final Gson g = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateSerializer())
                        .registerTypeAdapter(Time.class, new TimeSerializer())
                        .create();
                i.putExtra("detailsPurchases", g.toJson(dto.getDetallePedido()));
                communication.showDetails(i);
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    anularPedido(dto.getPedido().getId());
                    return true;
                }
            });
        }

        private void anularPedido(int id) {
            new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("¿Estás seguro?")
                    .setContentText("¿Deseas anular el pedido?")
                    .setCancelText("No, cancelar")
                    .setConfirmText("Sí, confirmar")
                    .showCancelButton(true)
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("¡Anulado!")
                                .setContentText(anularPedidoCommunication.anularPedido(id))
                                .show();
                    }).setCancelClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("¡Cancelado!")
                                .setContentText("El pedido no se anuló")
                                .show();
                    }).show();
        }
    }
}