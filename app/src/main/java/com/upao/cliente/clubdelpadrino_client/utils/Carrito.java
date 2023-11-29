package com.upao.cliente.clubdelpadrino_client.utils;

import com.upao.cliente.clubdelpadrino_client.entity.service.DetallePedido;

import java.util.ArrayList;

public class Carrito {
    private static final ArrayList<DetallePedido> detallePedidos = new ArrayList<>();

    public static String agregarProductos(DetallePedido detallePedido) {
        int index = 0;
        boolean b = false;
        for (DetallePedido dp : detallePedidos) {
            if (dp.getProducto().getId() == detallePedido.getProducto().getId()) {
                detallePedidos.set(index, detallePedido);
                b = true;
                return "Producto agregado al carrito de compras";
            }
            index++;
        }
        if (!b) {
            detallePedidos.add(detallePedido);
            return "Producto agregado al carrito de compras exitosamente";
        }
        return "...";
    }

    public static void eliminar(final int idp) {
        DetallePedido dpE = null;
        for (DetallePedido dp : detallePedidos) {
            if (dp.getProducto().getId() == idp) {
                dpE = dp;
                break;
            }
        }
        if (dpE != null) {
            detallePedidos.remove(dpE);
            System.out.println("Producto eliminado del carrito de compras");
        }
    }

    public static ArrayList<DetallePedido> getDetallePedidos() {
        return detallePedidos;
    }

    public static void limpiar() {
        detallePedidos.clear();
    }
}