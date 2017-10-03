package ubung.co.ubung;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ubung.co.ubung.Marce.ListaClientesOProfesores;
import ubung.co.ubung.Marce.SolocitudesClientesActivity;
import ubung.co.ubung.Utilidades.DatabaseManager;
import ubung.co.ubung.Utilidades.LaClaseQueHaceTodoConLasFechas;

import static ubung.co.ubung.PreLoginActivity.TIPO_BUNDLE_KEY;
import static ubung.co.ubung.PreLoginActivity.USUARIO_BUNDLE_KEY;

public class Calendario extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Semana.OnFragmentInteractionListener {


    private ViewPager semanas;
    private SemanaFragmentAdapter semanaAdapter;
    private DatabaseManager.TipoAplicacion tipo;
    private String userUid;
    private DatabaseReference refUsuario;
    private StorageReference refFotico;
    private SharedPreferences sp;
    private boolean tienefoto;

    private final static String TAG= "Calendario";
    public final static String SHARED_PREFERENCES_INFORMACION_PERFIL="esteeselnombredespip";

//    public final static String SHARED_PREFERENCES_KEY_UID="ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        tipo= DatabaseManager.TipoAplicacion.tipoPorString(i.getStringExtra(TIPO_BUNDLE_KEY));
        userUid= i.getStringExtra(USUARIO_BUNDLE_KEY);

        setContentView(R.layout.activity_calendario);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        View header = nv.getHeaderView(0);

        refUsuario= FirebaseDatabase.getInstance().getReference();
        refFotico= FirebaseStorage.getInstance().getReference(getString(R.string.nomble_fotos_perfilSR)).child(userUid);


        final CircleImageView iv= (CircleImageView) header.findViewById(R.id.nav_header_image_view);
        final TextView tvnombre= (TextView) header.findViewById(R.id.nav_header_nombre);
        final TextView tvcorreo= (TextView) header.findViewById(R.id.nav_header_correo);
        View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"si hizo click");
                lanzarPerfilPropio();
            }
        };

        iv.setOnClickListener(clickListener);
        tvnombre.setOnClickListener(clickListener);
        tvcorreo.setOnClickListener(clickListener);

        Menu m= nv.getMenu();
        switch (tipo){
            case MARCE:
                m.setGroupVisible(R.id.menu_marcela,true);
                m.setGroupVisible(R.id.menu_clientesyprofes,false);
                refUsuario=refUsuario.child(getString(R.string.nomble_profesoresFDB)).child(userUid);
                break;
            case CLIENTE:
                m.setGroupVisible(R.id.menu_marcela,false);
                m.setGroupVisible(R.id.menu_clientesyprofes,true);
                refUsuario=refUsuario.child(getString(R.string.nombre_clienteFDB)).child(userUid);
                break;
            case PROFE:m.setGroupVisible(R.id.menu_marcela,false);
                m.setGroupVisible(R.id.menu_clientesyprofes,true);
                refUsuario=refUsuario.child(getString(R.string.nomble_profesoresFDB)).child(userUid);
                break;
        }
        ponerFotoNombreyCorreoyLlenarSharedPreferences(tvnombre,tvcorreo,iv);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //ESTO lo he hechoyo


        DatabaseManager databaseManager= new DatabaseManager(this,tipo,userUid);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        semanaAdapter= new SemanaFragmentAdapter(getSupportFragmentManager(),4);
        semanas= (ViewPager) findViewById(R.id.pager_semanas);
        semanas.setAdapter(semanaAdapter);
        semanas.setCurrentItem(0);

    }

    public void ponerFotoNombreyCorreoyLlenarSharedPreferences(final TextView nom, final TextView corr, final CircleImageView iv){
        sp= getSharedPreferences(SHARED_PREFERENCES_INFORMACION_PERFIL,MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        refUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Shared preferences stuff
                String keynombre=getString(R.string.db_nombre);
                String keycorreo=getString(R.string.db_correo);
                String keytelefono=getString(R.string.db_tel);
                String keycumple=getString(R.string.db_cumpleanos);
                String keygenero=getString(R.string.db_genero);
                String keyfoto = getString(R.string.db_foto);



                String nombre = dataSnapshot.child(keynombre).getValue(String.class);
                String correo = dataSnapshot.child(keycorreo).getValue(String.class);
                String telefono;
                try{
                telefono= dataSnapshot.child(keytelefono).getValue(String.class);}
                catch (DatabaseException e){
                    long l = dataSnapshot.child(keytelefono).getValue(Long.class);
                    telefono= ""+l;
                }
                String fecha= dataSnapshot.child(keycumple).getValue(String.class);
                int edad = LaClaseQueHaceTodoConLasFechas.queEdadSiNacioEn(fecha);
                String genero= dataSnapshot.child(keygenero).getValue(String.class);
                tienefoto= dataSnapshot.child(keyfoto).getValue(Boolean.class);

                editor.putString(keycorreo,correo);
                editor.putString(keycumple,fecha);

//                editor.putString(SHARED_PREFERENCES_KEY_UID,userUid);
                editor.putString(keynombre,nombre);
                editor.putString(keygenero,genero);
                editor.putString(keytelefono,telefono);
                editor.commit();

                // poner foto y info
                nom.setText(nombre);
                corr.setText(correo);
                if(refFotico!=null){
                    Glide.with(Calendario.this)
                            .using(new FirebaseImageLoader())
                            .load(refFotico)
                            .dontAnimate()
                            .into(iv);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                //Todo: algo aca
                Log.e(TAG, databaseError.getMessage());
                Toast.makeText(Calendario.this,"Fallo algo vea TODOs"+ TAG,Toast.LENGTH_LONG).show();
            }
        });
    }

    public ViewPager getSemanas() {
        return semanas;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_marce_clientes) {
            Intent i = new Intent(this, ListaClientesOProfesores.class);
            i.putExtra(ListaClientesOProfesores.KEY_ES_CLIENTE,true);
            startActivity(i);
        } else if (id == R.id.menu_marce_profesores) {
            Intent i = new Intent(this, ListaClientesOProfesores.class);
            i.putExtra(ListaClientesOProfesores.KEY_ES_CLIENTE,false);
            startActivity(i);

        } else if (id == R.id.menu_marce_solicitudes) {
            Intent i = new Intent(this, SolocitudesClientesActivity.class);
            startActivity(i);

        } else if (id == R.id.menu_marce_paquetes) {


        } else if (id == R.id.nav_cerrar_sesion) {
            cerrarSecion();

        } else if (id == R.id.menu_marce_generar_reporte) {

        }else if (id == R.id.nav_contactar){

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void cerrarSecion(){
        PreLoginActivity.cerrarSecion(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void lanzarPerfilPropio(){
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra(PerfilActivity.BUNDLE_KEY_BUNDLE_INFO,sharedPreferencesToBundle(sp));
        intent.putExtra(PerfilActivity.BUNDLE_KEY_TIPO_PERFIL, DatabaseManager.TipoAplicacion.MARCE.getTipoString());
        intent.putExtra(PerfilActivity.BUNDLE_KEY_ES_EDITABLE,true);
        intent.putExtra(PerfilActivity.BUNDLE_KEY_ID_PARA_FOTO,userUid);
        startActivity(intent);
    }

    public Bundle sharedPreferencesToBundle(SharedPreferences sharedPreferences){
        Map<String,Object> map= (Map<String,Object>)sharedPreferences.getAll();
        Bundle retornar = new Bundle();
        for(Map.Entry<String,Object> entry: map.entrySet()){
            Object o= entry.getValue();
            String key = entry.getKey();
            if (o instanceof Integer){
                int i = (Integer) o;
                retornar.putInt(key,i);
            }
            else if(o instanceof String){
                String s = (String) o ;
                retornar.putString(key, s);
            }
            else if (o instanceof Boolean){
                boolean b = (Boolean) o;
                retornar.putBoolean(key,b);
            }
        }
        return retornar;
    }
}
