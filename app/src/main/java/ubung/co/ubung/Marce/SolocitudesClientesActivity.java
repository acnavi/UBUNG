package ubung.co.ubung.Marce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Calendar;


import ubung.co.ubung.Calendario;
import ubung.co.ubung.PerfilActivity;
import ubung.co.ubung.R;

public class SolocitudesClientesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solocitudes_clientes);

        RecyclerView solicitudesRView = (RecyclerView) findViewById(R.id.solicitudes_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        solicitudesRView.setLayoutManager(layoutManager);



        SolicitudesAdapter adapter= new SolicitudesAdapter(this);
        solicitudesRView.setAdapter(adapter);


    }

    public void lanzarPerfil(Bundle bundle, String uid){
        Intent i= new Intent(this, PerfilActivity.class);
        i.putExtra(PerfilActivity.BUNDLE_KEY_ES_SOLICITUD,true);
        i.putExtra(PerfilActivity.BUNDLE_KEY_ID_PARA_FOTO,uid);
        i.putExtra(PerfilActivity.BUNDLE_KEY_ES_EDITABLE,false);
        i.putExtra(PerfilActivity.BUNDLE_KEY_BUNDLE_INFO,bundle);
        startActivity(i);
    }
}
