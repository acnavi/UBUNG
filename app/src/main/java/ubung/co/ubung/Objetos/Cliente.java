package ubung.co.ubung.Objetos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by icmontesdumar on 21/09/17.
 */

public class Cliente {
    private String nombre;
    private String correo;
    private String peso;
    private String hbd;
    private String masofem;
    private String seguromedico;
    private boolean foto;
    private String tel;
    private String comentarios;
    private String direccion;


    public Cliente(){}

    public String getCorreo() {
        return correo;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTel() {
        return tel;
    }

    public void setFoto(boolean foto) {
        this.foto = foto;
    }
    public boolean getFoto(){
        return foto;
    }

    public String getHbd() {
        return hbd;
    }

    public String getMasofem() {
        return masofem;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPeso() {
        return peso;
    }

    public String getSeguromedico() {
        return seguromedico;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setHbd(String hbd) {
        this.hbd = hbd;
    }

    public void setMasofem(String masofem) {
        this.masofem = masofem;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public void setSeguromedico(String seguromedico) {
        this.seguromedico = seguromedico;
    }


}
