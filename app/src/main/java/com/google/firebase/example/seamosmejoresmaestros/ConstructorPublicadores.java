package com.google.firebase.example.seamosmejoresmaestros;

import java.util.Date;

public class ConstructorPublicadores {
    private String NombrePublicador, ApellidoPublicador, telefono, correo, genero,idPublicador, imagen;
    private Date ultAsignacion, ultAyudante, ultSustitucion;

    public ConstructorPublicadores() {}

    public String getNombrePublicador() {
        return NombrePublicador;
    }

    public String getApellidoPublicador() {
        return ApellidoPublicador;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getGenero() {
        return genero;
    }

    public String getIdPublicador() {
        return idPublicador;
    }

    public String getImagen() {
        return imagen;
    }

    public Date getUltAsignacion() {
        return ultAsignacion;
    }

    public Date getUltAyudante() {
        return ultAyudante;
    }

    public Date getUltSustitucion() {
        return ultSustitucion;
    }

    public void setNombrePublicador(String nombrePublicador) {
        NombrePublicador = nombrePublicador;
    }

    public void setApellidoPublicador(String apellidoPublicador) {
        ApellidoPublicador = apellidoPublicador;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setIdPublicador(String idPublicador) {
        this.idPublicador = idPublicador;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setUltAsignacion(Date ultAsignacion) {
        this.ultAsignacion = ultAsignacion;
    }

    public void setUltAyudante(Date ultAyudante) {
        this.ultAyudante = ultAyudante;
    }

    public void setUltSustitucion(Date ultSustitucion) {
        this.ultSustitucion = ultSustitucion;
    }
}
