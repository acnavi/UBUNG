package ubung.co.ubung.Marce;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ubung.co.ubung.R;
import ubung.co.ubung.Utilidades.DatabaseContractMarce;
import ubung.co.ubung.Utilidades.MarceDatabaseHelper;

public class ListaClientesOProfesores extends AppCompatActivity {

    public final static String KEY_ES_CLIENTE="escliente";
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

        RecyclerView solicitudesRView = (RecyclerView) findViewById(R.id.clientes_o_profes_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        solicitudesRView.setLayoutManager(layoutManager);

        Intent i = getIntent();
        boolean esC=i.getBooleanExtra(KEY_ES_CLIENTE,true);


        MarceDatabaseHelper helper= new MarceDatabaseHelper(getApplicationContext());
        SQLiteDatabase database= helper.getReadableDatabase();
        Cursor cursor;
        if(esC){
            String[] columns={DatabaseContractMarce.ClientesDB.COLUMN_UID, DatabaseContractMarce.ClientesDB.COLUMN_NOMBRE};
            cursor=database.query(DatabaseContractMarce.ClientesDB.CLIENTES_TABLE_NAME,columns,null,null,null,null,null);
        }
        else {
            String[] columns={DatabaseContractMarce.ProfesoresDB.COLUMN_UID, DatabaseContractMarce.ProfesoresDB.COLUMN_NOMBRE};
            cursor=database.query(DatabaseContractMarce.ProfesoresDB.PROFESORES_TABLE_NAME,columns,null,null,null,null,null);
        }

        ClienteOProfesorAdapter adapter= new ClienteOProfesorAdapter(cursor,getApplicationContext());
        solicitudesRView.setAdapter(adapter);

    }

}
