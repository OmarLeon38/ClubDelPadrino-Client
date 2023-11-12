package com.upao.cliente.clubdelpadrino_client.entity.service;

public class Plato {

    private int id;

    private String nombre;

    private Documento foto;

    private Double precio;

    private int stock;

    private String descripcionPlato;

    private Categoria categoria;

    private boolean vigencia;

    private boolean recomendado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Documento getFoto() {
        return foto;
    }

    public void setFoto(Documento foto) {
        this.foto = foto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescripcionPlatillo() {
        return descripcionPlato;
    }

    public void setDescripcionPlatillo(String descripcionPlatillo) {
        this.descripcionPlato = descripcionPlatillo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public boolean isVigencia() {
        return vigencia;
    }

    public void setVigencia(boolean vigencia) {
        this.vigencia = vigencia;
    }

    public boolean isRecomendado() {
        return recomendado;
    }

    public void setRecomendado(boolean recomendado) {
        this.recomendado = recomendado;
    }
}
