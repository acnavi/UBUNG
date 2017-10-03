package ubung.co.ubung;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import ubung.co.ubung.Utilidades.DatabaseContractMarce;
import ubung.co.ubung.Utilidades.DatabaseManager;
import ubung.co.ubung.Utilidades.LaClaseQueHaceTodoConLasFechas;
import ubung.co.ubung.Utilidades.MarceDatabaseHelper;
import ubung.co.ubung.databinding.FragmentDatosContactoBinding;

import static ubung.co.ubung.Calendario.SHARED_PREFERENCES_INFORMACION_PERFIL;

public class PerfilActivity extends AppCompatActivity {

    private final static String TAG="PerfilActivity";
    /*
    Constante usada para poner el los getExtra de SharedPreferences para saber si tiene valores opcionales tales como el peso.
     */
    private final static String VALOR_PARA_SABER_SI_NO_TIENE_VALUE="JAJA NO TIENE";

    public final static String BUNDLE_KEY_TIPO_PERFIL = "tipoperfil";
    public final static String BUNDLE_KEY_ES_EDITABLE = "eseditable";
    public final static String BUNDLE_KEY_BUNDLE_INFO = "SHAREDNOMBRE";
    public final static String BUNDLE_KEY_ID_PARA_FOTO = "lefote";

    private boolean esEditable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        ActionBar ab= getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        TextView tv=  (TextView) findViewById(R.id.perfil_tv_nombre);
        CircleImageView cv = (CircleImageView) findViewById(R.id.perfil_foto);
        LinearLayout ll= (LinearLayout) findViewById(R.id.list_view_perfil);

        Intent i = getIntent();
        String tipoS = i.getStringExtra(BUNDLE_KEY_TIPO_PERFIL);
        Bundle info = i.getBundleExtra(BUNDLE_KEY_BUNDLE_INFO);
        esEditable= i.getBooleanExtra(BUNDLE_KEY_ES_EDITABLE,false);







        String nombre = info.getString(getString(R.string.db_nombre),"");
        tv.setText(nombre);

            StorageReference refFotico= FirebaseStorage.getInstance().getReference(getString(R.string.nomble_fotos_perfilSR))
                    .child(i.getStringExtra(BUNDLE_KEY_ID_PARA_FOTO));

            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(refFotico)
                    .dontAnimate()
                    .into(cv);


        DatabaseManager.TipoAplicacion tipo = DatabaseManager.TipoAplicacion.tipoPorString(tipoS);

        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();
        fragTransaction.add(ll.getId(),datosContacto(info));
        fragTransaction.add(ll.getId(),datosInfo(info));
        fragTransaction.commit();
        //TODO: cuaando tenga info sobre las clase aca se ve si se llena con un historial o con clase dictadas
        switch (tipo){
            case CLIENTE:

                break;
            case MARCE:

                break;
            case PROFE:

                break;
        }

    }

    public FragmentoDatos datosContacto(Bundle sp){
        String keycorreo = getString(R.string.db_correo);
        String keydireccion = getString(R.string.db_direccion);
        String keytelefono= getString(R.string.db_tel);

        String correo= sp.getString(keycorreo,"");
        String direccion = sp.getString(keydireccion,VALOR_PARA_SABER_SI_NO_TIENE_VALUE);
        String telefono = sp.getString(keytelefono,"");

        String contenido = FragmentoDatos.PRE_DOSPUNTOS_CORREO + correo+"\n"
                + FragmentoDatos.PRE_DOSPUNTOS_TELEFONO + telefono;
        if(!direccion.equals(VALOR_PARA_SABER_SI_NO_TIENE_VALUE)){
            contenido+= "\n"+ FragmentoDatos.PRE_DOSPUNTOS_DIRECCION + direccion;
        }

        return FragmentoDatos.newInstance(FragmentoDatos.POSIBLE_TITULO_CONTACTO,contenido);
    }
    public FragmentoDatos datosInfo(Bundle sp){

        String keycumple = getString(R.string.db_cumpleanos);
        String keypeso= getString(R.string.db_peso);
        String keyeps= getString(R.string.db_seguro_medico);
        String keygenero= getString(R.string.db_genero);

        String genero= sp.getString(keygenero,"");

        String cumple= sp.getString(keycumple,"");
        int edad =LaClaseQueHaceTodoConLasFechas.queEdadSiNacioEn(cumple);
;        String eps= sp.getString(keyeps,VALOR_PARA_SABER_SI_NO_TIENE_VALUE);
        String peso= sp.getString(keypeso,VALOR_PARA_SABER_SI_NO_TIENE_VALUE);

        String contenido = FragmentoDatos.PRE_DOSPUNTOS_GENERO+ genero+"\n"
                + FragmentoDatos.PRE_DOSPUNTOS_EDAD + edad+"\n"
                + FragmentoDatos.PRE_DOSPUNTOS_CUMPLE + cumple;
        if(!eps.equals(VALOR_PARA_SABER_SI_NO_TIENE_VALUE)){
            contenido+= "\n"+ FragmentoDatos.PRE_DOSPUNTOS_EPS+eps;
        }
        if(!peso.equals(VALOR_PARA_SABER_SI_NO_TIENE_VALUE)){
            contenido+= "\n"+ FragmentoDatos.PRE_DOSPUNTOS_EPS+peso;
        }

        return FragmentoDatos.newInstance(FragmentoDatos.POSIBLE_TITULO_INFO,contenido);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!esEditable)
        return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.perfil, menu);
        return true;
    }

    public static Bundle deLaBaseDeDatosABundle(String uid,Context context, boolean esCliente){
        Bundle retornar = new Bundle();
        MarceDatabaseHelper helper= new MarceDatabaseHelper(context);
        SQLiteDatabase database= helper.getReadableDatabase();
        Cursor c;
        if(esCliente){
            String[] columns ={
                    DatabaseContractMarce.ClientesDB.COLUMN_CORREO,
                    DatabaseContractMarce.ClientesDB.COLUMN_NOMBRE,
                    DatabaseContractMarce.ClientesDB.COLUMN_PESO,
                    DatabaseContractMarce.ClientesDB.COLUMN_GENERO,
                    DatabaseContractMarce.ClientesDB.COLUMN_DIRECCION,
                    DatabaseContractMarce.ClientesDB.COLUMN_TELEFONO,
                    DatabaseContractMarce.ClientesDB.COLUMN_CUMPLEANOS,
                    DatabaseContractMarce.ClientesDB.COLUMN_SEGURO_MED

            };
            c=database.query(DatabaseContractMarce.ClientesDB.CLIENTES_TABLE_NAME,
                    columns,DatabaseContractMarce.ClientesDB.COLUMN_UID+"=\""+uid+"\"",null,null,null,null);
        }
        else {
            String[] columns={
                    DatabaseContractMarce.Persona.COLUMN_CORREO,
                    DatabaseContractMarce.Persona.COLUMN_NOMBRE,

                    DatabaseContractMarce.Persona.COLUMN_GENERO,
                    DatabaseContractMarce.Persona.COLUMN_DIRECCION,
                    DatabaseContractMarce.Persona.COLUMN_TELEFONO,
                    DatabaseContractMarce.Persona.COLUMN_CUMPLEANOS
            };
            c=database.query(DatabaseContractMarce.ProfesoresDB.PROFESORES_TABLE_NAME,
                    columns,DatabaseContractMarce.ProfesoresDB.COLUMN_UID+"=\""+uid+"\"",null,null,null,null);
        }
        if(c.getCount()>1){ Log.e(TAG,"hubo problemas en el metodo statico delaBase...");
        //TODO:Lanzar execion o algo y quitar return null
            return null;

            }
            c.moveToFirst();
        for(int i =0; i<c.getColumnCount(); i++){

            int tipo=c.getType(i);
            String key= c.getColumnName(i);
            switch (tipo){
                case Cursor.FIELD_TYPE_INTEGER:
                    int cosa=c.getInt(i);
                    retornar.putInt(key,cosa);

                    break;
                case Cursor.FIELD_TYPE_STRING:
                    String cosa2=c.getString(i);
                    retornar.putString(key,cosa2);
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    int cosa3=c.getInt(i);
                    retornar.putInt(key,cosa3);
                    break;
                case Cursor.FIELD_TYPE_NULL:

                    break;
                default:
                    //TODO lanzar un error y quitar la retornada
                    return null;

            }
        }

        return retornar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class FragmentoDatos extends android.app.Fragment {

        private FragmentDatosContactoBinding binding;
        private final static  String datosContenido= "cont";
        private final static String Titulo="titul";
        // EN LOS DATOS APARECE PESO: 66KG LO QUE VA ANTES DE LOS DOSPUNTOS MAS LOS DOS PUNTOS mas un espacio SON LOS SIGUIENTES STRINGS CONSTANTES
        public final static String PRE_DOSPUNTOS_PESO ="Peso: ";
        public final static String PRE_DOSPUNTOS_EDAD ="Edad: ";
        public final static String PRE_DOSPUNTOS_CUMPLE ="Fecha de nacimiento: ";
        public final static String PRE_DOSPUNTOS_EPS ="Eps/ Seguro medico:";
        public final static String PRE_DOSPUNTOS_CORREO ="Correo: ";
        public final static String PRE_DOSPUNTOS_TELEFONO ="Telefono: ";
        public final static String PRE_DOSPUNTOS_GENERO ="Genero: ";
        public final static String PRE_DOSPUNTOS_DIRECCION ="Dirección: ";

        public final static String POSIBLE_TITULO_CONTACTO= "Datos contacto";
        public final static String POSIBLE_TITULO_INFO= "Datos";
        public static FragmentoDatos newInstance(String titulo, String contenido) {
            FragmentoDatos fragment= new FragmentoDatos();

            Bundle args = new Bundle();

            args.putString(datosContenido,contenido);

            args.putString(Titulo,titulo);
            fragment.setArguments(args);
            return fragment;
        }
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            binding = DataBindingUtil.inflate(inflater,R.layout.fragment_datos_contacto, container, false);
            Bundle args= getArguments();
            binding.datosTitulo.setText(args.getString(Titulo));
            binding.datosContenido.setText(args.getString(datosContenido));
            return binding.getRoot();
        }
    }
    //TODO: Llenar esto con las mkadas cuando haya base de datos de clases

    public static class FragmentoHistorial extends Fragment {



        public final static String POSIBLE_TITULO_CLASES_DIC= "Clases dictadas";
        public final static String POSIBLE_TITULO_HISTORIAL= "Historial";
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragmento_historial,container,false);
        }
    }


}
