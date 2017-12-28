package ubung.co.ubung.Utilidades;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ubung.co.ubung.Objetos.Cliente;
import ubung.co.ubung.Objetos.Profesores;
import ubung.co.ubung.R;



/**
 * Created by icmontesdumar on 8/07/17.
 */

public class DatabaseManager {

    private SQLiteDatabase database;
    private Context context;
    private TipoAplicacion tipo;
    private final static FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    private String userUID;

    private final static String TAG="DatabaseManager";
    public enum TipoAplicacion{

        MARCE("marce"),CLIENTE("cliente"),PROFE("profe"),CLIENTENOACEPTADO("clientena");

        private String tipoString;

        TipoAplicacion(String tipo){
            tipoString=tipo;
        }

        public String getTipoString() {
            return tipoString;
        }
        public static TipoAplicacion tipoPorString(String s){
            if(s.equals(MARCE.getTipoString())) return MARCE;
            else if(s.equals(PROFE.getTipoString())) return PROFE;
            else if(s.equals(CLIENTE.getTipoString())) return CLIENTE;
            else return null;

        }
    }

    public DatabaseManager(Context c, TipoAplicacion t, String uid){

        userUID=uid;
        context=c;
        tipo=t;
        SQLiteOpenHelper helper;
        switch (tipo){
            case MARCE:
                helper = new MarceDatabaseHelper(context);
                break;
            case PROFE:
                helper = new ProfeDatabaseHelper(context);
                break;
            case CLIENTE:
                helper = new ClientesDatabaseHelper(context);
                break;
            default:
                throw new IllegalArgumentException("Tipo de app invalido");

        }

        database=helper.getWritableDatabase();



        switch (tipo){
            case MARCE:

                firebaseDatabase.getReference().child(context.getString(R.string.nombre_clienteFDB))
                        .addChildEventListener(new EscuchadorYLLenadorBaseMarcelaClientes());
                firebaseDatabase.getReference().child(context.getString(R.string.nomble_profesoresFDB))
                        .addChildEventListener(new EscuchadorYLLenadorBaseMarcelaProfesores());
                break;
            case PROFE:
                firebaseDatabase.getReference().child(context.getString(R.string.nomble_clasesFDB))
                        .addChildEventListener(new EscuchadoryLlenadorClasesProfesores());
                break;
            case CLIENTE:
                firebaseDatabase.getReference().child(context.getString(R.string.nomble_clasesFDB))
                        .addChildEventListener(new EscuchadoryLlenadorClasesClientes());
                break;
            default:
                throw new IllegalArgumentException("Tipo de app invalido");

        }
    }


    //////// //////// //       // // ////////     //     ////////
    //    // //        //     //  // //          // //   //    //
    //////// ////////   //   //   // ////////   //////   ////////
    //   //   //          // //    //       //  //    // //   //
    //   //  ////////     //      // //////// //      // //   //
    //Pensar esta mierda bien, no entiendo al gran malparido ivank del pasado
    //Yo tampoco :S soy ivank del mas futuro que la anterior frase.
    public  class EscuchadoryLlenadorClasesClientes implements ChildEventListener{

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            ContentValues cv = new ContentValues();
            String fechayhora=dataSnapshot.getKey();
            String[] fyh=fechayhora.split("-");
            cv.put(DatabaseContractClientes.ClasesFuturasDB.COLUMN_FECHA,fyh[0]);
            try {
                cv.put(DatabaseContractClientes.ClasesFuturasDB.COLUMN_HORA, Integer.parseInt(fyh[1]));

            }
            catch (NumberFormatException m){
                Log.e(TAG,"llenador clientes added 1");
            }
            DatabaseContractClientes.ClasesFuturasDB.Estado estado = queEstadoTiene(dataSnapshot);
            cv.put(DatabaseContractClientes.ClasesFuturasDB.ESTADO,estado.getEstado());

            long i= database.insert(DatabaseContractClientes.ClasesFuturasDB.CLASES_futuras_TABLE_NAME,null,cv);

            if(i==-1) Log.e(TAG,"llenador clientes added 2");
        }

        public DatabaseContractClientes.ClasesFuturasDB.Estado queEstadoTiene(DataSnapshot dataSnapshot){
            int cantidadClientesPorProfe=context.getResources().getInteger(R.integer.cantidad_clientes_por_profe);
            int cantidadDeProfesores= context.getResources().getInteger(R.integer.cantidad_profes);

            //mirar que haya un profesor
            String profe = (String) dataSnapshot.child(context.getString(R.string.db_clases_profe_poner_numero)+"1").getValue();
            if(profe==null){

                return DatabaseContractClientes.ClasesFuturasDB.Estado.NOT_AVAILABLE;
            }
            //Hay al menos un profesor, mirar si ME TIENE
            for (int i=1; i<=cantidadClientesPorProfe; i++){

                String cliente= (String) dataSnapshot.child(context.getString(R.string.db_clases_cliente_poner_numero_profe_y_cliente)+"1"
                        +i).getValue();
                if(cliente==null){
                    //el profesor tiene cupo
                    return DatabaseContractClientes.ClasesFuturasDB.Estado.DISPONIBLE;
                }
                if(cliente.equals(userUID)){
                    String fisioopil=(String) dataSnapshot.child(context.getString(R.string.db_pilates_oFisio_poner_numero)+"1").getValue();
                    if(fisioopil.equals(context.getString(R.string.db_pilates_oFisio_pilates)))
                    return DatabaseContractClientes.ClasesFuturasDB.Estado.RESERVADO_PILATES;
                    else if(fisioopil.equals(context.getString(R.string.db_pilates_oFisio_fisio)))
                        return DatabaseContractClientes.ClasesFuturasDB.Estado.RESERVADO_FISIO;

                }

            }
            //Hay al menos un profesor y no me tiene, mirar si hay lista de espera
            DataSnapshot listaDeESnapS = dataSnapshot.child(context.getString(R.string.db_clases_lista_de_espera));
            if(listaDeESnapS.exists()){
                int cuantosHay= (int)listaDeESnapS.getChildrenCount();
                if(cuantosHay>=context.getResources().getInteger(R.integer.cantidad_clientes_lista_de_espera)){
                    //la lista de espera esta llena
                    return DatabaseContractClientes.ClasesFuturasDB.Estado.NOT_AVAILABLE;
                }
                else {
                int i=0;
                for (DataSnapshot d:listaDeESnapS.getChildren()) {
                   if(((String)d.getValue()).equals(userUID)){
                       //estoy en la lista de espera
                       return DatabaseContractClientes.ClasesFuturasDB.Estado.LISTA_DE_ESPERA;
                   }
                   i++;
                }

                //no estoy en la lista de espera y esta no esta llena
                    return DatabaseContractClientes.ClasesFuturasDB.Estado.OCUPADO;
                }
            }

            //El primer profesor esta lleno, mirar si el resto de profesores existen y estan llenos
            for (int i =2; i<=cantidadDeProfesores;i++){
                profe= (String) dataSnapshot.child(context.getString(R.string.db_clases_profe_poner_numero)+i).getValue();
                if(profe==null){
                    return DatabaseContractClientes.ClasesFuturasDB.Estado.OCUPADO;
                }
                //El profesor existe, mirrar si esta lleno
                for(int j = 1 ; j<=cantidadClientesPorProfe; j++){
                    String ultimoClienteProfe= (String) dataSnapshot.child(context.getString(R.string.db_clases_cliente_poner_numero_profe_y_cliente)+i
                            +cantidadClientesPorProfe).getValue();
                    if(ultimoClienteProfe==null){
                        //no estoy aca pero esta vacio
                        return DatabaseContractClientes.ClasesFuturasDB.Estado.DISPONIBLE;
                    }
                    if(ultimoClienteProfe.equals(userUID)){
                        //estoy con el profesor i
                        String fisioopil=(String) dataSnapshot.child(context.getString(R.string.db_pilates_oFisio_poner_numero)+i).getValue();
                        if(fisioopil.equals(context.getString(R.string.db_pilates_oFisio_pilates)))
                            return DatabaseContractClientes.ClasesFuturasDB.Estado.RESERVADO_PILATES;
                        else if(fisioopil.equals(context.getString(R.string.db_pilates_oFisio_fisio)))
                            return DatabaseContractClientes.ClasesFuturasDB.Estado.RESERVADO_FISIO;

                    }
                }

            }
            //Todo esta lleno pero voy a empezar una lista de espera
            return DatabaseContractClientes.ClasesFuturasDB.Estado.OCUPADO;

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            ContentValues cv = new ContentValues();
            String fechayhora=dataSnapshot.getKey();
            String[] fyh=fechayhora.split("-");

            DatabaseContractClientes.ClasesFuturasDB.Estado estado = queEstadoTiene(dataSnapshot);
            cv.put(DatabaseContractClientes.ClasesFuturasDB.ESTADO,estado.getEstado());

            long i= database.update(DatabaseContractClientes.ClasesFuturasDB.CLASES_futuras_TABLE_NAME,cv,
                    DatabaseContractClientes.ClasesFuturasDB.COLUMN_FECHA+"="+fyh[0]+ " AND "+
                            DatabaseContractClientes.ClasesFuturasDB.COLUMN_HORA+"="+fyh[1],null);

            if(i==-1) Log.e(TAG,"llenador clientes changed");
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            ContentValues cv = new ContentValues();
            String fechayhora=dataSnapshot.getKey();
            String[] fyh=fechayhora.split("-");

            DatabaseContractClientes.ClasesFuturasDB.Estado estado = DatabaseContractClientes.ClasesFuturasDB.Estado.NOT_AVAILABLE;
            cv.put(DatabaseContractClientes.ClasesFuturasDB.ESTADO,estado.getEstado());

            long i= database.update(DatabaseContractClientes.ClasesFuturasDB.CLASES_futuras_TABLE_NAME,
                    cv,
                    DatabaseContractClientes.ClasesFuturasDB.COLUMN_FECHA+"="+fyh[0]+ " AND "+ DatabaseContractClientes.ClasesFuturasDB.COLUMN_HORA+"="+fyh[1],
                    null);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

            //TODO:MANEJAR ERROR
        }
    }

    public class EscuchadoryLlenadorClasesProfesores implements ChildEventListener{

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            int dondeEsta =estaElProfesor(dataSnapshot);
            if(dondeEsta>0){
                ContentValues cv = new ContentValues();
                String fechayhora=dataSnapshot.getKey();
                String[] fyh=fechayhora.split("-");
                cv.put(DatabaseContractClientes.ClasesFuturasDB.COLUMN_FECHA,fyh[0]);
                try {
                    cv.put(DatabaseContractClientes.ClasesFuturasDB.COLUMN_HORA, Integer.parseInt(fyh[1]));

                }
                catch (NumberFormatException m){
                    Log.e(TAG,"hubo un superproblema proferoser added");
                }

                final String[] toAppend = new String[2];
                final String[] toAppend2 = new String[2];
                final String cliente1= (String) dataSnapshot.child(context.getString(R.string.db_clases_cliente_poner_numero_profe_y_cliente)+dondeEsta+"1").getValue();
                final String cliente2= (String) dataSnapshot.child(context.getString(R.string.db_clases_cliente_poner_numero_profe_y_cliente)+dondeEsta+"2").getValue();
                firebaseDatabase.getReference().child(context.getString(R.string.nombre_clienteFDB)).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(cliente1!=null){
                                    toAppend[0]=cliente1;
                                String s=(String) dataSnapshot.child(cliente1).child(context.getString(R.string.db_nombre)).getValue();
                                toAppend[1]=s;
                                }
                                if(cliente2!=null){
                                    toAppend2[0]=cliente1;
                                    String s=(String) dataSnapshot.child(cliente2).child(context.getString(R.string.db_nombre)).getValue();
                                    toAppend2[1]=s;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );

                if(cliente1!=null) cv.put(DatabaseContracProfes.ClasesDB.COLUMN_PROFE_CLIENTE1,appendUIDandName(toAppend));
                if(cliente2!=null) cv.put(DatabaseContracProfes.ClasesDB.COLUMN_PROFE_CLIENTE_2,appendUIDandName(toAppend2));

                String fisioopil= (String) dataSnapshot.child(context.getString(R.string.db_pilates_oFisio_poner_numero)+dondeEsta).getValue();
                String comentarios= (String) dataSnapshot.child(context.getString(R.string.db_comentarios)).getValue();

                cv.put(DatabaseContracProfes.ClasesDB.COLUMN_COMENTARIOS,comentarios);
                cv.put(DatabaseContracProfes.ClasesDB.COLUMN_FISIO_O_PILATES,fisioopil);

                long i=database.insert(DatabaseContracProfes.ClasesDB.CLASES_TABLE_NAME,null,cv);

                if(i<0) Log.e(TAG, "Problemas cuando es profesor added 2");


            }
        }


        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                int dondeEsta =estaElProfesor(dataSnapshot);
                ContentValues cv = new ContentValues();
                String fechayhora=dataSnapshot.getKey();
                String[] fyh=fechayhora.split("-");
                String[] id={DatabaseContracProfes.ClasesDB._ID};
                int existe=database.query(DatabaseContracProfes.ClasesDB.CLASES_TABLE_NAME,
                        id,
                        DatabaseContracProfes.ClasesDB.COLUMN_FECHA+"="+fyh[0]+ " AND "+ DatabaseContracProfes.ClasesDB.COLUMN_HORA+"="+fyh[1],
                        null,null,null,null).getCount();
                if(dondeEsta>0&&existe>0){

                    final String[] toAppend = new String[2];
                    final String[] toAppend2 = new String[2];
                    final String cliente1= (String) dataSnapshot.child(context.getString(R.string.db_clases_cliente_poner_numero_profe_y_cliente)+dondeEsta+"1").getValue();
                    final String cliente2= (String) dataSnapshot.child(context.getString(R.string.db_clases_cliente_poner_numero_profe_y_cliente)+dondeEsta+"2").getValue();
                    firebaseDatabase.getReference().
                            child(context.getString(R.string.nombre_clienteFDB)).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(cliente1!=null){
                                        toAppend[0]=cliente1;
                                        String s=(String) dataSnapshot.child(cliente1).child(context.getString(R.string.db_nombre)).getValue();
                                        toAppend[1]=s;
                                    }
                                    if(cliente2!=null){
                                        toAppend2[0]=cliente1;
                                        String s=(String) dataSnapshot.child(cliente2).child(context.getString(R.string.db_nombre)).getValue();
                                        toAppend2[1]=s;
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            }
                    );

                    if(cliente1!=null) cv.put(DatabaseContracProfes.ClasesDB.COLUMN_PROFE_CLIENTE1,appendUIDandName(toAppend));
                    if(cliente2!=null) cv.put(DatabaseContracProfes.ClasesDB.COLUMN_PROFE_CLIENTE_2,appendUIDandName(toAppend2));

                    String fisioopil= (String) dataSnapshot.child(context.getString(R.string.db_pilates_oFisio_poner_numero)+dondeEsta).getValue();
                    String comentarios= (String) dataSnapshot.child(context.getString(R.string.db_comentarios)).getValue();

                    cv.put(DatabaseContracProfes.ClasesDB.COLUMN_COMENTARIOS,comentarios);
                    cv.put(DatabaseContracProfes.ClasesDB.COLUMN_FISIO_O_PILATES,fisioopil);

                    long i=database.update(DatabaseContracProfes.ClasesDB.CLASES_TABLE_NAME,
                            cv,
                            DatabaseContractClientes.ClasesFuturasDB.COLUMN_FECHA+"="+fyh[0]+ " AND "+ DatabaseContractClientes.ClasesFuturasDB.COLUMN_HORA+"="+fyh[1],
                            null);

                    if(i<0) Log.e(TAG, "Problemas cuando es profesor y se intenta update la base local");
            }
            else if(existe==0 && dondeEsta>0){
                    onChildAdded(dataSnapshot,s);
                }
            else if(existe>0 && dondeEsta==0){
                    onChildRemoved(dataSnapshot);
                }

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            int dondeEsta =estaElProfesor(dataSnapshot);
            if(dondeEsta>0){

                String fechayhora=dataSnapshot.getKey();
                String[] fyh=fechayhora.split("-");

               int i= database.delete(DatabaseContracProfes.ClasesDB.CLASES_TABLE_NAME,DatabaseContractClientes.ClasesFuturasDB.COLUMN_FECHA+"="+fyh[0]+ " AND "+ DatabaseContractClientes.ClasesFuturasDB.COLUMN_HORA+"="+fyh[1],
                        null);
                if(i==0) Log.e(TAG, "Problemas cuando es profesor y se borrar un entry de la base local");
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

            //TODO: hacer  algo con este error
        }


        public String appendUIDandName(String[] uidname) {

            return "("+uidname[0]+","+uidname[1]+")";


        }
        /**
         *
         *
         * @param ds
         * @return Retorna en que puesto esta el profesor, 0 si no esta
         */
        public int estaElProfesor(DataSnapshot ds){
            int cantidadDeProfesores= context.getResources().getInteger(R.integer.cantidad_profes);
            for (int i =1; i<=cantidadDeProfesores; i++){
                String s= (String) ds.child(context.getString(R.string.db_clases_profe_poner_numero)+i).getValue();
                if(s==null){
                    break;
                }
                if(s.equals(userUID)) return i;
            }
            return 0;
        }
    }


    public class EscuchadorYLLenadorBaseMarcelaClientes implements ChildEventListener{

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            String id= dataSnapshot.getKey();
            Cliente cliente = dataSnapshot.getValue(Cliente.class);
            ContentValues cv = databaseToContentValues(cliente, id);
            int bool=dataSnapshot.child(context.getString(R.string.nomble_paqueteFDB)).exists()? 1:0;
            cv.put(DatabaseContractMarce.ClientesDB.COLUMN_PAQUETE,bool);
            long i=database.insert(DatabaseContractMarce.ClientesDB.CLIENTES_TABLE_NAME,null,cv);
            if(i==-1) Log.e(TAG,"hubo un superproblema, may day! added marcela clientes");

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String id= dataSnapshot.getKey();
            Cliente cliente = dataSnapshot.getValue(Cliente.class);
            ContentValues cv = databaseToContentValues(cliente, id);

            int bool=dataSnapshot.child(context.getString(R.string.nomble_paqueteFDB)).exists()? 1:0;
            cv.put(DatabaseContractMarce.ClientesDB.COLUMN_PAQUETE,bool);

            long i=database.update(DatabaseContractMarce.ClientesDB.CLIENTES_TABLE_NAME,cv,
                    DatabaseContractMarce.ClientesDB.COLUMN_UID +"=\""+id+"\"",null);
            if(i==-1) Log.e(TAG,"hubo un superproblema, may day! changed marce clientes");

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String id= dataSnapshot.getKey();
            long i =database.delete(DatabaseContractMarce.ClientesDB.CLIENTES_TABLE_NAME,
                    DatabaseContractMarce.ClientesDB.COLUMN_UID+"=\""+id+"\"",null);
            if(i==0) Log.e(TAG, "problemas removed marce clientes");
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

        public ContentValues databaseToContentValues(Cliente cliente, String id){
            ContentValues cv= new ContentValues();
            cv.put(DatabaseContractMarce.ClientesDB.COLUMN_UID,id);
            cv.put(DatabaseContractMarce.ClientesDB.COLUMN_CORREO,cliente.getCorreo());
            cv.put(DatabaseContractMarce.ClientesDB.COLUMN_CUMPLEANOS,cliente.getHbd());
            cv.put(DatabaseContractMarce.ClientesDB.COLUMN_GENERO,cliente.getMasofem());
            cv.put(DatabaseContractMarce.ClientesDB.COLUMN_NOMBRE,cliente.getNombre());
            cv.put(DatabaseContractMarce.ClientesDB.COLUMN_PESO,cliente.getPeso());
            cv.put(DatabaseContractMarce.ClientesDB.COLUMN_SEGURO_MED,cliente.getSeguromedico());
            cv.put(DatabaseContractMarce.ClientesDB.COLUMN_TELEFONO,cliente.getTel());
            cv.put(DatabaseContractMarce.ClientesDB.COLUMN_DIRECCION,cliente.getDireccion());
            cv.put(DatabaseContractMarce.ClientesDB.COLUMN_COMENTARIOS,cliente.getComentarios());
            return cv;
        }
    }

    public class EscuchadorYLLenadorBaseMarcelaProfesores implements ChildEventListener{

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            String id= dataSnapshot.getKey();
            Profesores profesor = dataSnapshot.getValue(Profesores.class);
            ContentValues cv = databaseToContentValues(profesor, id);
            long i=database.insert(DatabaseContractMarce.ProfesoresDB.PROFESORES_TABLE_NAME,null,cv);
            if(i==-1) Log.e(TAG,"hubo un superproblema, may day! added marcela profes");

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String id= dataSnapshot.getKey();
            Profesores profesor = dataSnapshot.getValue(Profesores.class);
            ContentValues cv = databaseToContentValues(profesor, id);
            long i=database.update(DatabaseContractMarce.ProfesoresDB.PROFESORES_TABLE_NAME,cv,
                    DatabaseContractMarce.ProfesoresDB.COLUMN_UID +"=\""+id+"\"",null);
            if(i==-1) Log.e(TAG,"hubo un superproblema, may day! changed marce profes");

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String id= dataSnapshot.getKey();
            long i =database.delete(DatabaseContractMarce.ProfesoresDB.PROFESORES_TABLE_NAME,
                    DatabaseContractMarce.ProfesoresDB.COLUMN_UID+"=\""+id+"\"",null);
            if(i==0) Log.e(TAG, "problemas removed marce profes");
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

        public ContentValues databaseToContentValues(Profesores profe, String id){
            ContentValues cv= new ContentValues();
            cv.put(DatabaseContractMarce.ProfesoresDB.COLUMN_UID,id);
            cv.put(DatabaseContractMarce.ProfesoresDB.COLUMN_CORREO,profe.getCorreo());
            cv.put(DatabaseContractMarce.ProfesoresDB.COLUMN_CUMPLEANOS,profe.getHbd());
            cv.put(DatabaseContractMarce.ProfesoresDB.COLUMN_GENERO,profe.getMasofem());
            cv.put(DatabaseContractMarce.ProfesoresDB.COLUMN_NOMBRE,profe.getNombre());
            cv.put(DatabaseContractMarce.ProfesoresDB.COLUMN_TELEFONO,profe.getTel());
            cv.put(DatabaseContractMarce.ProfesoresDB.COLUMN_CANTIDAD_DE_CLASES_DICTADAS,profe.getClases_dictadas());
            cv.put(DatabaseContractMarce.ProfesoresDB.COLUMN_DIRECCION,profe.getDireccion());
            cv.put(DatabaseContractMarce.ProfesoresDB.COLUMN_PODERSYPREMO,profe.getPodersupremo());

            return cv;
        }
    }

//
//    private final static UriMatcher uriMatch = buildMatcher();
//
//    private final static int CLIENTES=100;
//    private final static int CLASES=200;
//    private final static int PAQUETES=300;
//    private final static int PROFESORES=400;




//    public static UriMatcher buildMatcher(){
//        UriMatcher matcher= new UriMatcher(UriMatcher.NO_MATCH);
//        matcher.addURI(DatabaseContractMarce.AUTHORITY,DatabaseContractMarce.PATH_CLIENTES,CLIENTES);
//        matcher.addURI(DatabaseContractMarce.AUTHORITY,DatabaseContractMarce.PATH_CLASES,CLASES);
//        matcher.addURI(DatabaseContractMarce.AUTHORITY,DatabaseContractMarce.PATH_PROFESORES,PROFESORES);
//        matcher.addURI(DatabaseContractMarce.AUTHORITY,DatabaseContractMarce.PATH_PAQUETES,PAQUETES);
//        return matcher;
//    }







}
