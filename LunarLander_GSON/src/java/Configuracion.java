/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ale
 */
class Configuracion {
    private String nombre;
    private String nave;
    private boolean modoDificil;
    
    //default constructor that accepts all the configurations parameters
    public Configuracion(String nombre, String nave, boolean modoDificil){
        this.nombre = nombre;
        this.nave = nave;
        this.modoDificil = modoDificil;
    }

    //returns the name of the configuration
    public String getNombre() {
        return nombre;
    }

    //sets the name of the configuration
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    //returns the color of the ship
    public String getTipoNave() {
        return nave;
    }

    //sets the color of the ship
    public void setTipoNave(String tipoNave) {
        this.nave = tipoNave;
    }

    //returns true if the mode of the configuration is difficult
    public boolean isModoDificil() {
        return modoDificil;
    }

    //sets the difficulty mode of the game.
    //true is difficult, false is easy.
    public void setModoDificil(boolean modoDificil) {
        this.modoDificil = modoDificil;
    }

    @Override
    public String toString() {
        return "{" + "nombre=" + nombre + ", nave=" + nave + ", modoDificil=" + modoDificil + '}';
    }
}

