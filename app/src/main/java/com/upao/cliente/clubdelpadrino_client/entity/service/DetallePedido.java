package com.upao.cliente.clubdelpadrino_client.entity.service;

public class DetallePedido {

    private int id;

    private int cantidad;

    private Double precio;

    private Plato platillo;

    private Pedido pedido;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Plato getPlatillo() {
        return platillo;
    }

    public void setPlatillo(Plato platillo) {
        this.platillo = platillo;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getNombre(){
        return this.platillo!= null ? this.platillo.getNombre() : "----";
    }

    public Double getSubTotal(){
        return this.cantidad * this.precio;
    }

}
