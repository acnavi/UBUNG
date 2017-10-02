package ubung.co.ubung.Utilidades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by icmontesdumar on 11/07/17.
 */

public class MarceDatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME="databaseMarce.db";
    private final static int DATABASE_VERSION= 4;

    public MarceDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        crearTablaClientes(db);
        crearTablaPaquetes(db);
        crearTablaProfesores(db);
        crearTablaClases(db);
    }

    private void crearTablaClases( SQLiteDatabase db) {

        final String SQL_COMAND= "CREATE TABLE "+DatabaseContractMarce.ClasesDB.CLASES_TABLE_NAME+" ("+
                DatabaseContractMarce.ClasesDB._ID+" INTEGER PRIMARY KEY, "+
                DatabaseContractMarce.ClasesDB.COLUMN_FECHA+" TEXT NOT NULL, "+
                DatabaseContractMarce.ClasesDB.COLUMN_HORA+" INTEGER NOT NULL, "+
                DatabaseContractMarce.ClasesDB.COLUMN_PROFE1+" TEXT NOT NULL, "+
                DatabaseContractMarce.ClasesDB.COLUMN_PROFE1_CLIENTE1 +" TEXT, " +
                DatabaseContractMarce.ClasesDB.COLUMN_PROFE1_CLIENTE_2 +" TEXT, " +
                DatabaseContractMarce.ClasesDB.COLUMN_FISIO_O_PILATES_1 + " TEXT, " +
                DatabaseContractMarce.ClasesDB.COLUMN_PROFE2+" TEXT, "+
                DatabaseContractMarce.ClasesDB.COLUMN_PROFE2_CLIENTE3 +" TEXT, " +
                DatabaseContractMarce.ClasesDB.COLUMN_PROFE2_CLIENTE_4 +" TEXT, "+
        DatabaseContractMarce.ClasesDB.COLUMN_FISIO_O_PILATES_2 + " TEXT, "+
                DatabaseContractMarce.ClasesDB.COLUMN_COMENTARIOS_CLASE + " TEXT, "+
                DatabaseContractMarce.ClasesDB.COLUMN_LISTA_DE_ESPERA + " TEXT);";
        db.execSQL(SQL_COMAND);

    }

    private void crearTablaProfesores(SQLiteDatabase db) {

        final String SQL_COMAND= "CREATE TABLE "+DatabaseContractMarce.ProfesoresDB.PROFESORES_TABLE_NAME+" ("+
                DatabaseContractMarce.ProfesoresDB._ID+" INTEGER AUTO_INCREMENT, " +
                DatabaseContractMarce.ProfesoresDB.COLUMN_UID + " TEXT PRIMARY KEY, " +
                DatabaseContractMarce.ProfesoresDB.COLUMN_NOMBRE + " TEXT NOT NULL, "+
                DatabaseContractMarce.ProfesoresDB.COLUMN_CUMPLEANOS + " TEXT NOT NULL, " +
                DatabaseContractMarce.ProfesoresDB.COLUMN_CORREO + " TEXT, "+
                DatabaseContractMarce.ProfesoresDB.COLUMN_DIRECCION + " TEXT, " +
                DatabaseContractMarce.ProfesoresDB.COLUMN_TELEFONO + " TEXT, "+
                DatabaseContractMarce.ProfesoresDB.COLUMN_GENERO+ " TEXT, "+
                DatabaseContractMarce.ProfesoresDB.COLUMN_PODERSYPREMO+" BOOLEAN NOT NULL, "+
                DatabaseContractMarce.ProfesoresDB.COLUMN_CANTIDAD_DE_CLASES_DICTADAS + " INTEGER NOT NULL);";

        db.execSQL(SQL_COMAND);

    }

    private void crearTablaPaquetes(SQLiteDatabase db) {

        final String SQL_COMAND= "CREATE TABLE "+DatabaseContractMarce.PaquetesDB.PAQUETES_TABLE_NAME+" ("+
                DatabaseContractMarce.PaquetesDB._ID+" INTEGER AUTO_INCREMENT, " +
                DatabaseContractMarce.PaquetesDB.COLUMN_UID_CLIENTE + " TEXT PRIMARY KEY, " +
                DatabaseContractMarce.PaquetesDB.COLUMN_FECHA_DE_VENCIMIENTO + " TEXT NOT NULL, " +
                DatabaseContractMarce.PaquetesDB.COLUMN_CLASES_DEL_PAQUETE + " INTEGER NOT NULL, " +
                DatabaseContractMarce.PaquetesDB.COLUMN_CLASES_RESERVADAS + " INTEGER NOT NULL, " +
                DatabaseContractMarce.PaquetesDB.COLUMN_CLASES_VISTAS_PAQUETE + " INTEGER NOT NULL);"
                ;

        db.execSQL(SQL_COMAND);
    }

    public void crearTablaClientes(SQLiteDatabase db){

        final String SQL_COMAND= "CREATE TABLE "+DatabaseContractMarce.ClientesDB.CLIENTES_TABLE_NAME+" ("+
                DatabaseContractMarce.ClientesDB._ID+" INTEGER AUTO_INCREMENT, " +
                DatabaseContractMarce.ClientesDB.COLUMN_UID + " TEXT PRIMARY KEY, " +
                DatabaseContractMarce.ClientesDB.COLUMN_NOMBRE + " TEXT NOT NULL, "+
                DatabaseContractMarce.ClientesDB.COLUMN_CUMPLEANOS + " TEXT NOT NULL, " +
                DatabaseContractMarce.ClientesDB.COLUMN_CORREO + " TEXT, "+
                DatabaseContractMarce.ClientesDB.COLUMN_DIRECCION + " TEXT, " +
                DatabaseContractMarce.ClientesDB.COLUMN_TELEFONO + " TEXT, "+
                DatabaseContractMarce.ClientesDB.COLUMN_PESO + " INTEGER, "+
                DatabaseContractMarce.ClientesDB.COLUMN_SEGURO_MED + " TEXT, " +
                DatabaseContractMarce.ClientesDB.COLUMN_COMENTARIOS + " TEXT, "+
                DatabaseContractMarce.ClientesDB.COLUMN_GENERO+ " TEXT);";

        db.execSQL(SQL_COMAND);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContractMarce.ClasesDB.CLASES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContractMarce.PaquetesDB.PAQUETES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContractMarce.ClientesDB.CLIENTES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContractMarce.ProfesoresDB.PROFESORES_TABLE_NAME);
        onCreate(db);

    }
}
