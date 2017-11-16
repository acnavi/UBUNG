package ubung.co.ubung.Marce;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ubung.co.ubung.PerfilActivity;
import ubung.co.ubung.R;
import ubung.co.ubung.Utilidades.DatabaseContractMarce;
import ubung.co.ubung.Utilidades.DatabaseManager;
import ubung.co.ubung.Utilidades.MarceDatabaseHelper;

public class ListaClientesOProfesores extends AppCompatActivity {

    public final static String KEY_ES_CLIENTE="escliente";
    public final static String KEY_ES_CON_PAQUETE="esconPaq";
    private boolean esCliente;
    private boolean esConPaquete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes_oprofesores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView solicitudesRView = (RecyclerView) findViewById(R.id.clientes_o_profes_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        solicitudesRView.setLayoutManager(layoutManager);

        Intent i = getIntent();
        esCliente =i.getBooleanExtra(KEY_ES_CLIENTE,true);
        esConPaquete=i.getBooleanExtra(KEY_ES_CON_PAQUETE,false);

        AsyncTask<Boolean, Void, Cursor> at=new AsyncTask<Boolean, Void, Cursor>() {
            boolean esCli;
            boolean eCP;
            @Override
            protected Cursor doInBackground(Boolean... params) {
                esCli=params[0];
                eCP=params[1];
                MarceDatabaseHelper helper= new MarceDatabaseHelper(getApplicationContext());
                SQLiteDatabase database= helper.getReadableDatabase();
                Cursor cursor;
                if(esCli){
                    String[] columns={DatabaseContractMarce.ClientesDB.COLUMN_UID, DatabaseContractMarce.ClientesDB.COLUMN_NOMBRE};
                    if(eCP)
                        cursor=database.query(DatabaseContractMarce.ClientesDB.CLIENTES_TABLE_NAME,columns,
                                DatabaseContractMarce.ClientesDB.COLUMN_PAQUETE + " = "+1,null,null,null,null);
                    else
                        cursor=database.query(DatabaseContractMarce.ClientesDB.CLIENTES_TABLE_NAME,columns,null,null,null,null,null);
                }
                else {
                    String[] columns={DatabaseContractMarce.ProfesoresDB.COLUMN_UID, DatabaseContractMarce.ProfesoresDB.COLUMN_NOMBRE};
                    cursor=database.query(DatabaseContractMarce.ProfesoresDB.PROFESORES_TABLE_NAME,columns,null,null,null,null,null);
                }

                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);
                ClienteOProfesorAdapter adapter= new ClienteOProfesorAdapter(cursor,ListaClientesOProfesores.this,esCliente);
                solicitudesRView.setAdapter(adapter);
            }
        };

        at.execute(esCliente,esConPaquete);



    }

    public void lanzarPerfil(String uid){
        Bundle b = PerfilActivity.deLaBaseDeDatosABundle(uid,this,esCliente);
        Intent i = new Intent(this,PerfilActivity.class);
        i.putExtra(PerfilActivity.BUNDLE_KEY_BUNDLE_INFO,b);
        i.putExtra(PerfilActivity.BUNDLE_KEY_ES_EDITABLE,true);
        String tipo;
        if(esCliente) tipo= DatabaseManager.TipoAplicacion.CLIENTE.getTipoString();
        else tipo= DatabaseManager.TipoAplicacion.PROFE.getTipoString();
        i.putExtra(PerfilActivity.BUNDLE_KEY_TIPO_PERFIL, tipo);
        i.putExtra(PerfilActivity.BUNDLE_KEY_ID_PARA_FOTO,uid);
        startActivity(i);
    }

}
