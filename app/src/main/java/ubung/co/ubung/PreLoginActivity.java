package ubung.co.ubung;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ubung.co.ubung.Utilidades.DatabaseManager;
//TODO: RESOLVER EL PROBLEMADEL BOTON HACIA ATRAS.

public class PreLoginActivity extends AppCompatActivity {
    private final static int INICIAL=0;
    private final static int INICIO_SEC=1;
    private final static int CREAR_CUENTA=2;
    private int estado;
    private FirebaseAuth mAuth;
    private DatabaseReference dataBase;
    private DatabaseReference dataBaseSupreme;
    private FirebaseUser user;
    private StorageReference solicitudesFotosStorage;
    public static final String GENERO_HOMBRE="hombre";
    public static final String GENERO_MUJER="mujer";
    private Uri selectedImagePath;
    private ImageView foto;

    private ViewPager pager;
    public static final String USUARIO_BUNDLE_KEY="user";
    public static final String TIPO_BUNDLE_KEY="tipo";

    private static final String TAG="PreLoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();


        setContentView(R.layout.activity_pre_login);
        findViewById(R.id.loading_thingi).setVisibility(View.VISIBLE);
        mAuth=FirebaseAuth.getInstance();

        solicitudesFotosStorage= FirebaseStorage.getInstance().getReference()
                .child(getString(R.string.nomble_fotos_perfilSR));


        user = mAuth.getCurrentUser();

        dataBaseSupreme= FirebaseDatabase.getInstance().getReference();


        if(user!=null){
            lanzarAlgoDependiendoDelUsuario();
            Log.i(TAG,user.getEmail());
            return;

        }

        findViewById(R.id.loading_thingi).setVisibility(View.INVISIBLE);



        dataBase = dataBaseSupreme.child(getString(R.string.nombre_solicitudedFDB));

        pager=(ViewPager) findViewById(R.id.cambiar_aca_por_lo_necesario);
        pager.setAdapter(new AdapterFragment(getSupportFragmentManager()));
        pager.setCurrentItem(1);

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        findViewById(R.id.loading_thingi).setVisibility(View.INVISIBLE);
    }

    public void lanzarSolicitudEnviada(String uid){


        Intent i = new Intent(PreLoginActivity.this, SolicitudEnviadaActivity.class);
        i.putExtra(USUARIO_BUNDLE_KEY,uid);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        findViewById(R.id.loading_thingi).setVisibility(View.INVISIBLE);
        startActivity(i);
    }

    private void noEstaEnClientesBuscarEnProfesores(final String uid){
        dataBaseSupreme.child(getString(R.string.nomble_profesoresFDB)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot hijo: dataSnapshot.getChildren()){
                    if(hijo.getKey().equals(uid)){
                        boolean esMarce= (boolean) hijo.child(getString(R.string.db_profesores_poderSupremo)).getValue();
                        if(esMarce){
                            lanzarCalendario(DatabaseManager.TipoAplicacion.MARCE);

                        }
                        else {
                            lanzarCalendario(DatabaseManager.TipoAplicacion.PROFE);

                        }
                        return;
                    }
                }
                huboUnProblemaAlIniciarSecion();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                huboUnProblemaAlIniciarSecion();
            }
        });
    }


    /*
    Usar solo si se sabe que el usuario no es null.
     */

    private void noEstaEnsolicitudesBuscarEnClientes(final String uid){
        dataBaseSupreme.child(getString(R.string.nombre_clienteFDB)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot hijo: dataSnapshot.getChildren()){
                    if(hijo.getKey().equals(uid)){
                        lanzarCalendario(DatabaseManager.TipoAplicacion.CLIENTE);
                        return;
                    }

                }
                noEstaEnClientesBuscarEnProfesores(uid);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                huboUnProblemaAlIniciarSecion();
            }
        });
    }
    public void lanzarAlgoDependiendoDelUsuario(){
        final String uid = user.getUid();
        findViewById(R.id.loading_thingi).setVisibility(View.VISIBLE);

                dataBaseSupreme.child(getString(R.string.nombre_solicitudedFDB)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot hijo: dataSnapshot.getChildren()){
                            if(hijo.getKey().equals(uid)){

                                lanzarSolicitudEnviada(uid);
                                return;
                            }
                        }
                        noEstaEnsolicitudesBuscarEnClientes(uid);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if(databaseError.getCode()==DatabaseError.PERMISSION_DENIED) noEstaEnsolicitudesBuscarEnClientes(uid);
                        else huboUnProblemaAlIniciarSecion();
                    }
                });




    }

    public void huboUnProblemaAlIniciarSecion(){
        if(!isFinishing()){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(getString(R.string.problema_inic_secion_title))
                .setMessage(getString(R.string.problema_inic_secion_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
    }}

    public void lanzarCalendario (DatabaseManager.TipoAplicacion t){

        Intent i = new Intent(this, Calendario.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(USUARIO_BUNDLE_KEY,user.getUid());
        i.putExtra(TIPO_BUNDLE_KEY,t.getTipoString());
        startActivity(i);

    }


    private String[] params;
    private  String[] params_keys(){
        String[] ret=
                {"" + getString(R.string.db_nombre),
                "" + getString(R.string.db_cumpleanos),
                "" + getString(R.string.db_genero),
                "" + getString(R.string.db_correo),
                "" + getString(R.string.db_sol_contrasena),
                "" + getString(R.string.db_tel)
        };
        return ret;
    }



    /*
    Hicieron click en cualquiera de los dos botones
     */

    public void hicieronClick(View view){

        int current=pager.getCurrentItem();


        switch (view.getId()){
            case R.id.boton_inic_sec:

                if(current==0)

                    iniciarSecion();

                else{

                    pager.setCurrentItem(0);
                    cambiarBotones(0);


                }
                break;
            case R.id.boton_crear_cue:

                if (params == null)
                    params = new String[AdapterFragment.CANTIDAD_PAGINAS - 4];

                if(current==0) {
                    pager.setCurrentItem(2);
                    cambiarBotones(2);


                }
                else if(current==2||current==6||current==5||current==3||current==7||current==8){

                    FragmentManager fm=getSupportFragmentManager();
                    String tag = "android:switcher:" + R.id.cambiar_aca_por_lo_necesario+ ":" + current;
                    FragmentoTexto f= (FragmentoTexto) fm.findFragmentByTag(tag);
                    int i =f.onClick(params);
                    if(i==FragmentoTexto.CONTRASENA_NO_COINSIDE){
                        Toast.makeText(this,"Las contraseñas no coinciden.",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(i==FragmentoTexto.NO_METIO_NADA){
                    Toast.makeText(this,"Debes introducir este campo.",Toast.LENGTH_LONG).show();
                        return;
                    }
//                    EditText tv= (EditText) findViewById(R.id.registrarse_enter_text);
//
//
//                    String info=tv.getText().toString().trim();
//                    if(info==null || info.equals("")){
//                        Toast.makeText(this,"Debes introducir este campo.",Toast.LENGTH_LONG).show();
//                    return;}
//
//
//                    if(current==7&&!info.equals(params[4])){
//                        Toast.makeText(this,"Las contraseñas no coinciden.",Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    if(current<=6)
//                    params[current-2]=info;
//                    else
//                        params[current-3]=info;
                }

                else if(current==4){

                    if(esHombre==null){ Toast.makeText(this,"Debes seleccionar tu genero.",Toast.LENGTH_LONG).show();
                    return;}
                    if(!esHombre[0]){
                        params[2]=GENERO_MUJER;
                    }
                    else params[2]=GENERO_HOMBRE;
                }
                if(current!=AdapterFragment.CANTIDAD_PAGINAS-1) {
                    if (current != 0)
                        pager.setCurrentItem(getItem(1));
                }
                else {
                    //TODO: cambiar el metodo de abajo
                    enviarSolicitud();
                }

        }




    }



    @Override
    public void onBackPressed() {
        int i = getItem(-1);
        if(i>0)
        pager.setCurrentItem(i);
        else if(i==-1)pager.setCurrentItem(1);
        else
            {
            super.onBackPressed();}
    }



    private void cambiarBotones(int current){
        Button inicSec = (Button) findViewById(R.id.boton_inic_sec);
        Button regis = (Button) findViewById(R.id.boton_crear_cue);
        switch (current){
            case 0:
                inicSec.setText(R.string.registrarse_texto_boton_inic_sec);
                regis.setText(R.string.registrarse_texto_boton_regis);
                break;
            case 1:
                inicSec.setText(R.string.registrarse_texto_boton_inic_sec);
                regis.setText(R.string.registrarse_texto_boton_regis);
                break;
            case 2:
                inicSec.setText(R.string.registrarse_texto_boton_ya_tienes_cuenta);
                regis.setText(R.string.registrarse_texto_boton_siguiente);
                break;
            case 9:
                regis.setText(R.string.registrarse_texto_boton_regis);
                break;

        }
    }


    /*
    Retorna el item del paer en la posicion actual + i, donde i es lo que recibe por parametro.
     */

    private int getItem(int i) {
        int ret=pager.getCurrentItem() + i;
        cambiarBotones(ret);
        return ret;
    }

    public void escribirEnSolicitudes(){
        user=mAuth.getCurrentUser();
        if(user==null){ Log.e(TAG,"No se esta crendo el puto usuario.");
        return;}
        String uid = user.getUid();
        String[] keys = params_keys();
        Map<String,Object> values= new HashMap<>();
        for (int i=0 ; i <keys.length; i++){
            values.put(keys[i],params[i]);
        }
        final DatabaseReference ref = dataBase.child(uid);
        ref.setValue(values);
        if(selectedImagePath!=null){

                //TODO: Hacer esto servir (subir foto)

                UploadTask up=solicitudesFotosStorage.child(uid).putFile(selectedImagePath);
                up.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ref.child(getString(R.string.db_foto)).setValue(false);
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        ref.child(getString(R.string.db_foto)).setValue(true);
                    }
                });




        }
        else ref.child(getString(R.string.db_foto)).setValue(false);
        //Solicituid enviada
        lanzarSolicitudEnviada(uid);

    }



    public void enviarSolicitud(){

        mAuth.createUserWithEmailAndPassword(params[3],params[4]).addOnCompleteListener(PreLoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    escribirEnSolicitudes();

                } else {
                    Toast.makeText(PreLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, task.getException().getMessage());
                }
            }


        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageButton targetImage=(ImageButton) findViewById(R.id.registrarse_image_foto);
        if (resultCode == RESULT_OK){

            Uri targetUri = data.getData();


            selectedImagePath=targetUri;
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
                }
            catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
        }



    public void iniciarSecion(){

        EditText correoET=(EditText) findViewById(R.id.inicio_sec_correo);
        EditText contrasenaET= (EditText) findViewById(R.id.inicio_sec_password);
        String correo=correoET.getText().toString();
        String contrasena=contrasenaET.getText().toString();

        mAuth.signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    user = mAuth.getCurrentUser();
//                    new LoaderInicioSecion().execute();
                    lanzarAlgoDependiendoDelUsuario();

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(PreLoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }








    public class AdapterFragment extends FragmentPagerAdapter{

        public final static int CANTIDAD_PAGINAS=10;

        public final Fragment[] fragmentos = new Fragment[CANTIDAD_PAGINAS];

        public AdapterFragment(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            if(position<0||position>9){
                throw new IllegalArgumentException("La posicon del pager tiene algo mal.");
            }





            switch (position){
                case 0:

                    return new FragmentoInicSesion();


                case 1:

                    return new FragmentoUBUNG();



                case 4:

                    return new FragmentoSexo();



                case 9:

                    return new FragmentoImagen();

                default:
                    return FragmentoTexto.newInstance(position);



            }

        }

        @Override
        public int getCount() {

            return CANTIDAD_PAGINAS;
        }

    }












    public static class FragmentoTexto extends Fragment{
        private static  final String KEY_POSICION="keypos";
        int posicion;
        EditText ed;
        public static FragmentoTexto newInstance(int pocision) {



            FragmentoTexto fragment = new FragmentoTexto();
            Bundle b = new Bundle();

            b.putInt(KEY_POSICION,pocision);
            fragment.setArguments(b);
            return fragment;
        }


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle b = getArguments();
            posicion= b != null ? b.getInt(KEY_POSICION) : -1;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.registro_cuenta_text,container,false);
            TextView tv = (TextView) v.findViewById(R.id.registrarse_label);
            EditText editText =(EditText) v.findViewById(R.id.registrarse_enter_text);
            ed=editText;
            bindOnPosition(editText,tv);
            return v;
        }

        private void bindOnPosition(final EditText editText,TextView tv){
            editText.setSingleLine();
            editText.setText("");
            switch (posicion){

                case 2:
                tv.setText(getString(R.string.registrarse_nombre_label));
                    editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    break;
                case 3:
                    tv.setText(getString(R.string.registrarse_fecha_de_nacimiento_label));
                    editText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Calendar c = Calendar.getInstance();
                            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    String text=year+"/";
                                    int mes=month+1;
                                    if(mes<10){
                                        text+=0;
                                    }
                                    text+=mes+"/";
                                    if(dayOfMonth<10){
                                        text+=0;
                                    }
                                    text+=dayOfMonth;
                                    editText.setText(text);
                                }
                            };
                            DatePickerDialog datePicker = new DatePickerDialog(getContext(), dateSetListener,c.get(Calendar.YEAR),
                                    c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                            datePicker.show();
                        }
                    });
                    break;
                case 5:
                    editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    tv.setText(getString(R.string.registrarse_correo_label));
                    break;
                case 6:

                    tv.setText(getString(R.string.registrarse_contrasena_label));
                    editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    break;

                case 7:

                    tv.setText(getString(R.string.registrarse_contrasena2));
                    editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    break;

                case 8:

                    tv.setText(getString(R.string.registrarse_telefono_label));
                    editText.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
            }
        }

        public final static int NO_METIO_NADA=1243;
        public final static int CONTRASENA_NO_COINSIDE=43508;
        public final static int EXITO=43598;

        public int onClick(String[] params){
            String s =ed.getText().toString();
            if(s==null || s.equals("")){

                return NO_METIO_NADA;}


            if(posicion==7&&!s.equals(params[4])){

                return CONTRASENA_NO_COINSIDE;
            }
            if(posicion<=6)
            params[posicion-2]=s;
            else
                params[posicion-3]=s;
            return EXITO;
        }



    }






    public static class FragmentoImagen extends Fragment{
        public static int RESULT_LOAD_IMAGE = 1;
        @Nullable
        @Override


        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view =inflater.inflate(R.layout.registro_cuenta_foto,container,false);
            ImageButton imageButton= (ImageButton) view.findViewById(R.id.registrarse_image_foto);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            });

            return view;
        }



    }









    private static boolean[] esHombre;
    public static class FragmentoSexo extends Fragment{

        public void seleccionarBoton(View b ){
            b.setBackgroundColor(getResources().getColor(R.color.blanco_tranparentoso));

        }



        public void desSeleccionarBoton(View b ){
            b.setBackgroundResource(R.drawable.caja_transparente);


        }
        FrameLayout mujerB;
        FrameLayout hombreB;

        @Nullable
        @Override

        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v =inflater.inflate(R.layout.registro_cuenta_genero,container,false);
             mujerB= (FrameLayout) v.findViewById(R.id.boton_mujer);
            hombreB= (FrameLayout) v.findViewById(R.id.boton_hombre);
            mujerB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (esHombre==null) esHombre= new boolean[1];
                    seleccionarBoton(mujerB);
                    desSeleccionarBoton(hombreB);
                    esHombre[0]=false;

                }
            });
            hombreB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (esHombre==null) esHombre= new boolean[1];
                    desSeleccionarBoton(mujerB);
                    seleccionarBoton(hombreB);
                    esHombre[0]=true;

                }
            });
            return v;
        }
    }





    public static class FragmentoInicSesion extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.inicio_secion,container,false);
        }
    }




    public static class FragmentoUBUNG extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            return inflater.inflate(R.layout.logo_ubung_view,container,false);
        }
    }
    public static class FragmentoLoading extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            return inflater.inflate(R.layout.loaging_fragment_layout,container,false);
        }
    }

//    public void tipoSegunUsuario(final FirebaseUser tempUser, final DatabaseManager.TipoAplicacion[] tipo){
//
//
//        dataBaseSupreme.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                DataSnapshot profes=dataSnapshot.child(getString(R.string.nomble_profesoresFDB));
//                DataSnapshot child=profes.child(tempUser.getUid());
//                if(child.exists()){
//                    if( (boolean) child.child(getString(R.string.db_profesores_poderSupremo)).getValue()){
//                        tipo[0]= DatabaseManager.TipoAplicacion.MARCE;
//                        lanzarCalendario(tipo[0]);
//
//
//                    }
//                    else
//                        { tipo[0]= DatabaseManager.TipoAplicacion.PROFE;
//                            lanzarCalendario(tipo[0]);
//                        }
//            }
//            else {
//                    DataSnapshot clients = dataSnapshot.child(getString(R.string.nombre_clienteFDB));
//                    if (!clients.exists())return;
//                    DataSnapshot child2=clients.child(tempUser.getUid());
//                    if(child2.exists()){tipo[0]= DatabaseManager.TipoAplicacion.CLIENTE;
//                        lanzarCalendario(tipo[0]);}
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


//        }
//        public class LoaderInicioSecion extends AsyncTask<Void,Void,Void>{
//
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                DatabaseManager.TipoAplicacion[] t = new DatabaseManager.TipoAplicacion[1];
//                tipoSegunUsuario(user, t);
//                return Vo;
//            }
//
//
//        }

    public static void cerrarSecion(Activity context){
        FirebaseAuth.getInstance().signOut();
        Intent intent= new Intent(context,PreLoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(intent);
        context.finish();
    }
    }





