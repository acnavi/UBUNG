package ubung.co.ubung.Utilidades;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ubung.co.ubung.Objetos.Clase;
import ubung.co.ubung.R;

/**
 * Created by icmontesdumar on 27/12/17.
 */

public enum EstadoClaseCliente {

    OCUPADO("ocupado", R.color.clase_clientes_ocupada),
    OCUPADO_SOLO_FISIO("ocupadoSoloFisio", R.color.clase_clientes_ocupada),
    RESERVADO_PILATES("reservado_pilates",R.color.clase_clientes_reservada),
    RESERVADO_FISIO("reservado_fisio",R.color.clase_clientes_reservada),
    DISPONIBLE("disponible",R.color.clase_clientes_disponible),
    LISTA_DE_ESPERA("lista_de_espera",R.color.clase_clientes_lista_de_espera),
    LISTA_DE_ESPERA_FISIO("lista_de_espera_fisio",R.color.clase_clientes_lista_de_espera),
    NOT_AVAILABLE("not_available",R.color.clase_not_available);

    private String estado;
    private int resColor;

    public String getEstado(){
        return estado;
    }

    public int getResColor() {
        return resColor;
    }

    public static EstadoClaseCliente estadoConString(String e){
        for (EstadoClaseCliente st: EstadoClaseCliente.values()) {
            if(e.equals(st.estado)) return st;
        }
        return null;
    }
    EstadoClaseCliente(String s, int color){
        estado=s;
        resColor=color;
    }

    public static EstadoClaseCliente estadoDeClase(Clase clase, String uidCliente){
        if(!clase.isDisponible()) return NOT_AVAILABLE;
        for(String s: clase.getClientes()){
            if(s.contains(uidCliente)) return RESERVADO_PILATES;
        }
        for (String s: clase.getClientesFisioterapia()){
            if(s.contains(uidCliente)) return RESERVADO_FISIO;
        }
        for (String s: clase.getListaDeEspera()){
            if(s.contains(uidCliente)) return  LISTA_DE_ESPERA;
        }
        for (String s: clase.getListaDeEsperaFisioterapia()){
            if(s.contains(uidCliente)) return  LISTA_DE_ESPERA_FISIO;
        }
        if(clase.isLlenaListaDeEspera()) return NOT_AVAILABLE;
        if(clase.isLlenoClientes()) return OCUPADO;
        if(clase.isLlenoPorFisioterapia()) return OCUPADO_SOLO_FISIO;
        throw new IllegalStateException("hubo un error terrible");

    }

}
