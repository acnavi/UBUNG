package ubung.co.ubung.Utilidades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by icmontesdumar on 12/07/17.
 */

public class ClientesDatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME="databaseCliente.db";
    private final static int DATABASE_VERSION= 1;

    public ClientesDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        crearTablaClasesFuturas(db);
        crearTablaClasesPasadas(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContractClientes.ClasesPasadasDB.CLASES_pasadas_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContractClientes.ClasesFuturasDB.CLASES_futuras_TABLE_NAME);
        onCreate(db);

    }

    public void crearTablaClasesFuturas(SQLiteDatabase db){
        final String SQL_COMAND= "CREATE TABLE "+DatabaseContractClientes.ClasesFuturasDB.CLASES_futuras_TABLE_NAME+" ("+
                DatabaseContractClientes.ClasesFuturasDB._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DatabaseContractClientes.ClasesFuturasDB.COLUMN_FECHA+" TEXT NOT NULL, "+
                DatabaseContractClientes.ClasesFuturasDB.COLUMN_HORA+" INTEGER NOT NULL, " +
                DatabaseContractClientes.ClasesFuturasDB.ESTADO + " TEXT NOT NULL);";

        db.execSQL(SQL_COMAND);


    }

    public void crearTablaClasesPasadas(SQLiteDatabase db){

        final String SQL_COMAND= "CREATE TABLE "+DatabaseContractClientes.ClasesPasadasDB.CLASES_pasadas_TABLE_NAME+" ("+
                DatabaseContractClientes.ClasesPasadasDB._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DatabaseContractClientes.ClasesPasadasDB.COLUMN_FECHA+" TEXT NOT NULL, "+
                DatabaseContractClientes.ClasesPasadasDB.COLUMN_HORA+" INTEGER NOT NULL, " +
                DatabaseContractClientes.ClasesPasadasDB.COLUMN_PROFE + " TEXT NOT NULL, " +
                DatabaseContractClientes.ClasesPasadasDB.COLUMN_FISIO_O_PILATES +  " TEXT);";

        db.execSQL(SQL_COMAND);

    }
}
