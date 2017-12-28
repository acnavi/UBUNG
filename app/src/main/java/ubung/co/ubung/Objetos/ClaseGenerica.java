package ubung.co.ubung.Objetos;

import java.util.ArrayList;

/**
 * Created by icmontesdumar on 27/12/17.
 */

public class ClaseGenerica {

    private ArrayList<String> profesores;
    private ArrayList<String> clientes;
    private ArrayList<String> clientesFisioterapia;
    private int hora;
    private boolean disponible;


    public ClaseGenerica(){}


    public ArrayList<String> getProfesores() {
        return profesores;
    }



    public ArrayList<String> getClientesFisioterapia() {
        return clientesFisioterapia;
    }

    public ArrayList<String> getClientes() {
        return clientes;
    }


    public int getHora() {
        return hora;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setClientes(ArrayList<String> clientes) {
        this.clientes = clientes;
    }

    public void setClientesFisioterapia(ArrayList<String> clientesFisioterapia) {
        this.clientesFisioterapia = clientesFisioterapia;
    }



    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public void setProfesores(ArrayList<String> profesores) {
        this.profesores = profesores;
    }

}
