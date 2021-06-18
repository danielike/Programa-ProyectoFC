/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Dani
 */
public class Comic {
    private int id;
    private String titulo;
    private Date fecha_adquisicion;
    private int num_coleccion;
    private float precio;
    private Blob portada;
    private String estado;
    private int id_coleccion;
    private String url_imagen;

    public Comic() {
    }

    public Comic(int id, String titulo, Date fecha_adquisicion, int num_coleccion, float precio, Blob portada, String estado, int id_coleccion,
            String url_imagen) {
        this.id = id;
        this.titulo = titulo;
        this.fecha_adquisicion = fecha_adquisicion;
        this.num_coleccion = num_coleccion;
        this.precio = precio;
        this.portada = portada;
        this.estado = estado;
        this.id_coleccion = id_coleccion;
        this.url_imagen = url_imagen;
    }
    public Comic(int id, String titulo, Date fecha_adquisicion, int num_coleccion
            ,float precio, Blob portada, String estado, int id_coleccion
            ) {
        this.id = id;
        this.titulo = titulo;
        this.fecha_adquisicion = fecha_adquisicion;
        this.num_coleccion = num_coleccion;
        this.precio = precio;
        this.portada = portada;
        this.estado = estado;
        this.id_coleccion = id_coleccion;
    }

    public Comic(int id, String titulo, Date fecha_adquisicion, int num_coleccion) {
        this.id = id;
        this.titulo = titulo;
        this.fecha_adquisicion = fecha_adquisicion;
        this.num_coleccion = num_coleccion;
    }

    public Comic(int id, String titulo, Date fecha_adquisicion) {
        this.id = id;
        this.titulo = titulo;
        this.fecha_adquisicion = fecha_adquisicion;
    }

    public Comic(int id, String titulo, Date fecha_adquisicion, 
            int numero_coleccion, Blob portada, String estado, int id_coleccion) {
        this.id = id;
        this.titulo = titulo;
        this.fecha_adquisicion = fecha_adquisicion;
        this.num_coleccion = numero_coleccion;
        this.estado = estado;
        this.id_coleccion = id_coleccion;
    }

  

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFecha_adquisicion() {
        return fecha_adquisicion;
    }

    public void setFecha_adquisicion(Date fecha_adquisicion) {
        this.fecha_adquisicion = fecha_adquisicion;
    }

    public int getNum_coleccion() {
        return num_coleccion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Blob getPortada() {
        return portada;
    }

    public void setPortada(Blob portada) {
        this.portada = portada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_coleccion() {
        return id_coleccion;
    }

    public void setId_coleccion(int id_coleccion) {
        this.id_coleccion = id_coleccion;
    }

    public void setNum_coleccion(int num_coleccion) {
        this.num_coleccion = num_coleccion;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }
    
    

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return titulo + " - " + sdf.format(fecha_adquisicion);
    }
    
    
}
