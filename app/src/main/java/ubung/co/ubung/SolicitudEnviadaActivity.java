package ubung.co.ubung;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ubung.co.ubung.R;
import ubung.co.ubung.Utilidades.DatabaseManager;

public class SolicitudEnviadaActivity extends AppCompatActivity implements ChildEventListener{

    private String uid;
    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_enviada);
        Intent i = getIntent();
        uid=i.getStringExtra(PreLoginActivity.USUARIO_BUNDLE_KEY);
        userDatabaseReference=FirebaseDatabase.getInstance().getReference().child(getString(R.string.nombre_solicitudedFDB));
        userDatabaseReference.addChildEventListener(this);
        vecesPress=0;
    }


    int vecesPress;
    @Override
    public void onBackPressed() {
        if(vecesPress==1){
            cerrarSecion();
            return;
        }
        vecesPress=1;
        Toast.makeText(this,"Preciona otravez para inicia secion",Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                vecesPress=0;
            }
        }, 2000);

    }

    private void cerrarSecion() {
        PreLoginActivity.cerrarSecion(this);
    }



    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

        if(dataSnapshot.getKey().equals(uid)){
            Intent i = new Intent(this, Calendario.class);
            i.putExtra(PreLoginActivity.USUARIO_BUNDLE_KEY,uid);
            i.putExtra(PreLoginActivity.TIPO_BUNDLE_KEY, DatabaseManager.TipoAplicacion.CLIENTE.getTipoString());
            startActivity(i);

        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
