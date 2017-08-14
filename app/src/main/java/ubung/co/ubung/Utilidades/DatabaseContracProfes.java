package ubung.co.ubung.Utilidades;

import android.net.Uri;
import android.provider.BaseColumns;

import ubung.co.ubung.CasillaAdapter;

/**
 * Created by icmontesdumar on 11/07/17.
 */

public class DatabaseContracProfes {

    public static final String AUTHORITY= "ubung.co.ubung.Utilidades";

    public static final Uri URI_BASE= Uri.parse("content://"+ AUTHORITY);

    public static final String PATH="clases";


    public static final class ClasesDB implements BaseColumns {

        /**
         * Nombre de la tabla
         */
        public static final String CLASES_TABLE_NAME= CasillaAdapter.NOMBRE_GENERAL_BASE_DE_DATOS_CLASE;


        public static final Uri CONTENT_URI= URI_BASE.buildUpon().appendPath(PATH).build();
        /**
         * Nombre de las columnas de la tabla
         */
        public static final String COLUMN_FECHA=CasillaAdapter.GENERAL_COLUMN_FECHA;
        public static final String COLUMN_HORA=CasillaAdapter.GENERAL_COLUMN_HORA;
        public static final String COLUMN_COMENTARIOS="comentarios";
        /**
         * FORMATO (\<UID\>,/<NOMBRE/> )
         */
        public static final String COLUMN_PROFE_CLIENTE1="cliente1";
        /**
         * FORMATO (\<UID\>,\<NOMBRE\>)
         */
        public static final String COLUMN_PROFE_CLIENTE_2="cliente2";
        public static final String COLUMN_FISIO_O_PILATES="p_pilofisio";



    }
}
