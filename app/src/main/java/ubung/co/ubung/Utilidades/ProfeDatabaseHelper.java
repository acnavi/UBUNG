package ubung.co.ubung.Utilidades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by icmontesdumar on 12/07/17.
 */

public class ProfeDatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME="databaseProfe.db";
    private final static int DATABASE_VERSION= 1;

    public ProfeDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    private void crearTablaClases( SQLiteDatabase db) {

        final String SQL_COMAND= "CREATE TABLE "+DatabaseContracProfes.ClasesDB.CLASES_TABLE_NAME+" ("+
                DatabaseContracProfes.ClasesDB._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DatabaseContracProfes.ClasesDB.COLUMN_FECHA+" TEXT NOT NULL, "+
                DatabaseContracProfes.ClasesDB.COLUMN_HORA+" INTEGER NOT NULL, "+
                DatabaseContracProfes.ClasesDB.COLUMN_PROFE_CLIENTE1 +" TEXT, " +
                DatabaseContracProfes.ClasesDB.COLUMN_PROFE_CLIENTE_2 +" TEXT, " +
                DatabaseContracProfes.ClasesDB.COLUMN_COMENTARIOS + " TEXT, "+
                DatabaseContracProfes.ClasesDB.COLUMN_FISIO_O_PILATES + " TEXT);";
        db.execSQL(SQL_COMAND);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        crearTablaClases(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContracProfes.ClasesDB.CLASES_TABLE_NAME);
    }


}
