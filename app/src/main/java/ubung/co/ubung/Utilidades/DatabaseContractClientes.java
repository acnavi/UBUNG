package ubung.co.ubung.Utilidades;

import android.net.Uri;
import android.provider.BaseColumns;

import ubung.co.ubung.CasillaAdapter;
import ubung.co.ubung.R;

/**
 * Created by icmontesdumar on 11/07/17.
 */

public class DatabaseContractClientes {
    public static final String AUTHORITY= "ubung.co.ubung.Utilidades";

    public static final Uri URI_BASE= Uri.parse("content://"+ AUTHORITY);

    public static final String PATH_PASADAS="clases_pasadas";
    public static final String PATH_FUTURAS="clases_futuras";

    public static final class ClasesFuturasDB implements BaseColumns {


        public enum Estado{
            OCUPADO("ocupado", R.color.clase_clientes_ocupada),RESERVADO_PILATES("reservado_pilates",R.color.clase_clientes_reservada),RESERVADO_FISIO("reservado_fisio",R.color.clase_clientes_reservada),
            DISPONIBLE("disponible",R.color.clase_clientes_disponible), LISTA_DE_ESPERA("lista_de_espera",R.color.clase_clientes_lista_de_espera),NOT_AVAILABLE("not_available",R.color.clase_not_available);

            private String estado;
            private int resColor;

            public String getEstado(){
                return estado;
            }

            public int getResColor() {
                return resColor;
            }

            public static Estado estadoConString(String e){
                for (Estado st:Estado.values()) {
                    if(e.equals(st.estado)) return st;
                }
                return null;
            }
            private Estado(String s, int color){
                estado=s;
                resColor=color;
            }
        }

        /**
         * Nombre de la tabla
         */
        public static final String CLASES_futuras_TABLE_NAME= CasillaAdapter.NOMBRE_GENERAL_BASE_DE_DATOS_CLASE;


        public static final Uri CONTENT_URI= URI_BASE.buildUpon().appendPath(PATH_FUTURAS).build();
        /**
         * Nombre de las columnas de la tabla
         */
        public static final String COLUMN_FECHA=CasillaAdapter.GENERAL_COLUMN_FECHA;
        public static final String COLUMN_HORA=CasillaAdapter.GENERAL_COLUMN_HORA;


        public static final String ESTADO="estado";
    }
    public static final class ClasesPasadasDB implements BaseColumns {



        /**
         * Nombre de la tabla
         */
        public static final String CLASES_pasadas_TABLE_NAME="clases_pasadas_db";


        public static final Uri CONTENT_URI= URI_BASE.buildUpon().appendPath(PATH_PASADAS).build();
        /**
         * Nombre de las columnas de la tabla
         */
        public static final String COLUMN_FECHA=CasillaAdapter.GENERAL_COLUMN_FECHA;
        public static final String COLUMN_HORA=CasillaAdapter.GENERAL_COLUMN_HORA;
        public static final String COLUMN_PROFE="prof";
        public static final String COLUMN_FISIO_O_PILATES="pilofisio";

    }
}
