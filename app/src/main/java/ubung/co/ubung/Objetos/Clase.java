package ubung.co.ubung.Objetos;

import java.util.ArrayList;

/**
 * Created by icmontesdumar on 27/12/17.
 */

public class Clase extends ClaseGenerica {
    private ArrayList<String> comentarios;
    private ArrayList<String> listaDeEspera;
    private ArrayList<String> listaDeEsperaFisioterapia;
    private boolean llenoClientes;
    private boolean llenoPorFisioterapia;
    private boolean llenaListaDeEspera;


    public Clase(){}

    public boolean isLlenaListaDeEspera() {
        return llenaListaDeEspera;
    }

    public boolean isLlenoClientes() {
        return llenoClientes;
    }

    public boolean isLlenoPorFisioterapia() {
        return llenoPorFisioterapia;
    }

    public void setLlenoPorFisioterapia(boolean llenoPorFisioterapia) {
        this.llenoPorFisioterapia = llenoPorFisioterapia;
    }

    public void setLlenoClientes(boolean llenoClientes) {
        this.llenoClientes = llenoClientes;
    }

    public void setLlenaListaDeEspera(boolean llenaListaDeEspera) {
        this.llenaListaDeEspera = llenaListaDeEspera;
    }

    public ArrayList<String> getComentarios() {
        return comentarios;
    }

    public ArrayList<String> getListaDeEspera() {
        return listaDeEspera;
    }

    public ArrayList<String> getListaDeEsperaFisioterapia() {
        return listaDeEsperaFisioterapia;
    }

    public void setListaDeEspera(ArrayList<String> listaDeEspera) {
        this.listaDeEspera = listaDeEspera;
    }

    public void setListaDeEsperaFisioterapia(ArrayList<String> listaDeEsperaFisioterapia) {
        this.listaDeEsperaFisioterapia = listaDeEsperaFisioterapia;
    }


    public void setComentarios(ArrayList<String> comentarios) {
        this.comentarios = comentarios;
    }

    //Metodos Clientes


}
