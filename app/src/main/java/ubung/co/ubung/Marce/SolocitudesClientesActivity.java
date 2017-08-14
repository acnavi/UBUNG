package ubung.co.ubung.Marce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Calendar;


import ubung.co.ubung.Calendario;
import ubung.co.ubung.R;

public class SolocitudesClientesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solocitudes_clientes);

        RecyclerView solicitudesRView = (RecyclerView) findViewById(R.id.solicitudes_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        solicitudesRView.setLayoutManager(layoutManager);



        SolicitudesAdapter adapter= new SolicitudesAdapter(getApplicationContext());
        solicitudesRView.setAdapter(adapter);


    }
}
