package com.upao.cliente.clubdelpadrino_client.entity.service;

public class Categoria {

    private int id;

    private String nombre;

    private boolean Vigencia;

    private Documento foto;

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

    public boolean isVigencia() {
        return Vigencia;
    }

    public void setVigencia(boolean vigencia) {
        Vigencia = vigencia;
    }

    public Documento getFoto() {
        return foto;
    }

    public void setFoto(Documento foto) {
        this.foto = foto;
    }
}
