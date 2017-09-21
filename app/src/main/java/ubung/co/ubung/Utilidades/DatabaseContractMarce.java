package ubung.co.ubung.Utilidades;

import android.net.Uri;
import android.provider.BaseColumns;

import ubung.co.ubung.CasillaAdapter;

/**
 * Created by icmontesdumar on 11/07/17.
 */

public class DatabaseContractMarce {

    public static final String AUTHORITY= "ubung.co.ubung.Utilidades";

    public static final Uri URI_BASE= Uri.parse("content://"+ AUTHORITY);

    public static final String PATH_CLASES="clases";
    public static final String PATH_CLIENTES="clientes";
    public static final String PATH_PAQUETES="paquetes";
    public static final String PATH_PROFESORES="profesores";



    public static final class ClientesDB implements BaseColumns{

        /**
         * Nombre de la tabla
         */
        public static final String CLIENTES_TABLE_NAME="clientesdb";

        public static final Uri CONTENT_URI= URI_BASE.buildUpon().appendPath(PATH_CLIENTES).build();

        /**
         * Nombre de las columnas de la tabla
         */
        public static final String COLUMN_UID="uid";
        public static final String COLUMN_NOMBRE="nombre";
        public static final String COLUMN_CUMPLEANOS="bday";
        public static final String COLUMN_GENERO="genero";
        public static final String COLUMN_TELEFONO="telefono";
        public static final String COLUMN_CORREO="correo";
        public static final String COLUMN_DIRECCION="dir";
        public static final String COLUMN_PESO="peso";
        public static final String COLUMN_SEGURO_MED="seguro_med";
        public static final String COLUMN_COMENTARIOS="coments";

    }

    public static final class ProfesoresDB implements BaseColumns{

        /**
         * Nombre de la tabla
         */
        public static final String PROFESORES_TABLE_NAME="profesoresdb";


        public static final Uri CONTENT_URI= URI_BASE.buildUpon().appendPath(PATH_PROFESORES).build();
        /**
         * Nombre de las columnas de la tabla
         */
        public static final String COLUMN_UID="uid";
        public static final String COLUMN_NOMBRE="nombre";
        public static final String COLUMN_CUMPLEANOS="bday";
        public static final String COLUMN_GENERO="genero";
        public static final String COLUMN_TELEFONO="telefono";
        public static final String COLUMN_CORREO="correo";
        public static final String COLUMN_DIRECCION="dir";
        public static final String COLUMN_CANTIDAD_DE_CLASES_DICTADAS="CANT_CLAS_DICTADAS";
        public static final String COLUMN_PODERSYPREMO="podersupremo";


    }
    public static final class ClasesDB implements BaseColumns{

        /**
         * Nombre de la tabla
         */
        public static final String CLASES_TABLE_NAME= CasillaAdapter.NOMBRE_GENERAL_BASE_DE_DATOS_CLASE;
        public static final Uri CONTENT_URI= URI_BASE.buildUpon().appendPath(PATH_CLASES).build();


        /**
         * Nombre de las columnas de la tabla
         */
        public static final String COLUMN_FECHA=CasillaAdapter.GENERAL_COLUMN_FECHA;
        public static final String COLUMN_HORA=CasillaAdapter.GENERAL_COLUMN_HORA;
        public static final String COLUMN_PROFE1="prof1";
        public static final String COLUMN_PROFE1_CLIENTE1="p1_cliente1";
        public static final String COLUMN_PROFE1_CLIENTE_2="p1_cliente2";
        public static final String COLUMN_FISIO_O_PILATES_1="p1_pilofisio";

        public static final String COLUMN_PROFE2="prof2";
        public static final String COLUMN_PROFE2_CLIENTE3="p2_cliente1";
        public static final String COLUMN_PROFE2_CLIENTE_4="p2_cliente2";
        public static final String COLUMN_FISIO_O_PILATES_2="p2_pilofisio";
        /**
         * Comentarios exclusivamente de la clase
         */
        public static final String COLUMN_COMENTARIOS_CLASE="comen";
        public static final String COLUMN_LISTA_DE_ESPERA="listaDE";

    }
    public static final class PaquetesDB implements BaseColumns{

        /**
         *
         * Nombre de la tabla
         */
        public static final String PAQUETES_TABLE_NAME="paquetesdb";


        public static final Uri CONTENT_URI= URI_BASE.buildUpon().appendPath(PATH_PAQUETES).build();
        /**
         * Nombre de las columnas de la tabla
         */
        public static final String COLUMN_FECHA_DE_VENCIMIENTO="fecha";
        public static final String COLUMN_UID_CLIENTE="uid";
        public static final String COLUMN_CLASES_VISTAS_PAQUETE="clas_vistas";
        public static final String COLUMN_CLASES_RESERVADAS="clas_res";
        public static final String COLUMN_CLASES_DEL_PAQUETE="clas_paquete";

    }

}
