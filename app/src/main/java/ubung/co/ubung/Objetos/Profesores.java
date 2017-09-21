package ubung.co.ubung.Objetos;

/**
 * Created by icmontesdumar on 21/09/17.
 */

public class Profesores {
    private String clases_dictadas;
    private String nombre;
    private String correo;
    private String hbd;
    private String masofem;
    private String tel;
    private String direccion;
    private boolean foto;
    private boolean podersupremo;

    public Profesores(){}

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setFoto(boolean foto) {
        this.foto = foto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClases_dictadas(String clases_dictadas) {
        this.clases_dictadas = clases_dictadas;
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

    public void setPodersupremo(boolean podersupremo) {
        this.podersupremo = podersupremo;
    }

    public String getClases_dictadas() {
        return clases_dictadas;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTel() {
        return tel;
    }

    public String getNombre() {
        return nombre;
    }

    public String getHbd() {
        return hbd;
    }

    public String getMasofem() {
        return masofem;
    }
    public boolean getFoto(){
        return foto;
    }
    public boolean getPodersupremo(){
        return podersupremo;
    }

}
