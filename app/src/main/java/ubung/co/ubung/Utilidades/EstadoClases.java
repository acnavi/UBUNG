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
public class EstadoClases{


    public enum EstadoClaseCliente {

        OCUPADO("ocupado", R.color.clase_clientes_ocupada),
        OCUPADO_SOLO_FISIO("ocupadoSoloFisio", R.color.clase_clientes_ocupada),
        RESERVADO_PILATES("reservado_pilates", R.color.clase_clientes_reservada),
        RESERVADO_FISIO("reservado_fisio", R.color.clase_clientes_reservada),
        DISPONIBLE("disponible", R.color.clase_clientes_disponible),
        LISTA_DE_ESPERA("lista_de_espera", R.color.clase_clientes_lista_de_espera),
        LISTA_DE_ESPERA_FISIO("lista_de_espera_fisio", R.color.clase_clientes_lista_de_espera),
        NOT_AVAILABLE("not_available", R.color.clase_not_available);

        private String estado;
        private int resColor;

        public String getEstado() {
            return estado;
        }

        public int getResColor() {
            return resColor;
        }

        public static EstadoClaseCliente estadoConString(String e) {
            for (EstadoClaseCliente st : EstadoClaseCliente.values()) {
                if (e.equals(st.estado)) return st;
            }
            return null;
        }

        EstadoClaseCliente(String s, int color) {
            estado = s;
            resColor = color;
        }

    }
    public enum EstadoClaseMarce {


        NOT_AVAILABLE("not_available", R.color.clase_not_available),
        VACIO("vacio",R.color.clase_marce_abierta_sin_nadie),
        VACIO_Y_VA_PROFESOR("vacio y va prof",R.color.clase_marce_abierta_sin_nadie),
        NECESITA_PROFESOR("necesitaP",R.color.clase_marce_abierta_con_problemas),
        MUCHA_DEMANDA("mucha gente viene",R.color.clase_marce_abierta_con_problemas),
        MUCHA_DEMANDA_FISIO("mucha gente viene a fisio",R.color.clase_marce_abierta_con_problemas),
        CLASE_INCREIBLE("clase increible", R.color.clase_marce_increible),
        CLASE_INCREIBLE_FISIO("clase increible fisio", R.color.clase_marce_increible_fisio);

        private String estado;
        private int resColor;

        public String getEstado() {
            return estado;
        }

        public int getResColor() {
            return resColor;
        }

        public static EstadoClaseCliente estadoConString(String e) {
            for (EstadoClaseCliente st : EstadoClaseCliente.values()) {
                if (e.equals(st.estado)) return st;
            }
            return null;
        }

        EstadoClaseMarce(String s, int color) {
            estado = s;
            resColor = color;
        }

    }

    public static EstadoClaseCliente estadoDeClaseCliente(Clase clase, String uidCliente) {
        if (!clase.isDisponible()) return EstadoClaseCliente.NOT_AVAILABLE;
        for (String s : clase.getClientes()) {
            if (s.contains(uidCliente)) return EstadoClaseCliente.RESERVADO_PILATES;
        }
        for (String s : clase.getClientesFisioterapia()) {
            if (s.contains(uidCliente)) return EstadoClaseCliente.RESERVADO_FISIO;
        }
        for (String s : clase.getListaDeEspera()) {
            if (s.contains(uidCliente)) return EstadoClaseCliente.LISTA_DE_ESPERA;
        }
        for (String s : clase.getListaDeEsperaFisioterapia()) {
            if (s.contains(uidCliente)) return EstadoClaseCliente.LISTA_DE_ESPERA_FISIO;
        }
        if (clase.isLlenaListaDeEspera()) return EstadoClaseCliente.NOT_AVAILABLE;
        if (clase.isLlenoClientes()) return EstadoClaseCliente.OCUPADO;
        if (clase.isLlenoPorFisioterapia()) return EstadoClaseCliente.OCUPADO_SOLO_FISIO;
        throw new IllegalStateException("hubo un error terrible");

    }

    public static EstadoClaseMarce estadoDeClaseMarce(Clase clase) {
        if (!clase.isDisponible()) return EstadoClaseMarce.NOT_AVAILABLE;
        if(clase.getClientes()==null || clase.getClientes().isEmpty()){
            if(clase.getClientesFisioterapia()==null||clase.getClientesFisioterapia().isEmpty()){
            if(clase.getProfesores()==null||clase.getProfesores().isEmpty()){
                return EstadoClaseMarce.VACIO;
            }
            else return EstadoClaseMarce.VACIO_Y_VA_PROFESOR;
            }
            else{
                return EstadoClaseMarce.CLASE_INCREIBLE_FISIO;
            }
        }
        if(clase.isLlenoClientes()){
            if(clase.isLlenoProfesores()){
                if(clase.getListaDeEspera()==null||clase.getListaDeEspera().isEmpty()){
                    return EstadoClaseMarce.CLASE_INCREIBLE;
                }
                else{
                    return EstadoClaseMarce.MUCHA_DEMANDA;
                }
            }
            else return EstadoClaseMarce.NECESITA_PROFESOR;
        }
        else{
            if(clase.getClientesFisioterapia()==null||clase.getClientesFisioterapia().isEmpty()){
            return EstadoClaseMarce.CLASE_INCREIBLE;
            }
            if(clase.isLlenoPorFisioterapia()){
                if(clase.getListaDeEsperaFisioterapia()==null||clase.getListaDeEsperaFisioterapia().isEmpty()){
                    return EstadoClaseMarce.CLASE_INCREIBLE_FISIO;
                }
                return EstadoClaseMarce.MUCHA_DEMANDA_FISIO;
            }
            return EstadoClaseMarce.CLASE_INCREIBLE_FISIO;
        }

    }

}
