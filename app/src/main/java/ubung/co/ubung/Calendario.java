package ubung.co.ubung;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import ubung.co.ubung.Marce.SolocitudesClientesActivity;
import ubung.co.ubung.Utilidades.DatabaseManager;

public class Calendario extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Semana.OnFragmentInteractionListener {


    private ViewPager semanas;
    private SemanaFragmentAdapter semanaAdapter;
    private DatabaseManager.TipoAplicacion tipo;
    private String userUid;
    public static final String TIPO_KEY = "tkey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //ESTO lo he hechoyo

        Intent i = getIntent();
//        tipo= DatabaseManager.TipoAplicacion.tipoPorString(i.getStringExtra(TIPO_KEY));
        userUid= i.getStringExtra(getString(R.string.usuario));

//        DatabaseManager databaseManager= new DatabaseManager(this,tipo,userUid);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        semanaAdapter= new SemanaFragmentAdapter(getSupportFragmentManager(),4);
        semanas= (ViewPager) findViewById(R.id.pager_semanas);
        semanas.setAdapter(semanaAdapter);
        semanas.setCurrentItem(0);

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
            super.onBackPressed();
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent i = new Intent(this,PreLoginActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(this, SolocitudesClientesActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_manage) {


        } else if (id == R.id.nav_cerrar_sesion) {
            cerrarSecion();

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void cerrarSecion(){
        FirebaseAuth.getInstance().signOut();
        Intent intent= new Intent(this,PreLoginActivity.class);
        startActivity(intent);
    }
}
