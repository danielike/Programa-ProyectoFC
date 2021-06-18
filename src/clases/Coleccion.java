/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author Dani
 */
public class Coleccion {
    private int id;
    private String nombre;
    private String sinopsis;
    

    public Coleccion() {
    }

    public Coleccion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
        public Coleccion(int id, String nombre, String sinopsis) {
        this.id = id;
        this.nombre = nombre;
        this.sinopsis = sinopsis;
    }

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

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    
    @Override
    public String toString() {
        return nombre;
    }
    
    
}
