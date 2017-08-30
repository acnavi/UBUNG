package ubung.co.ubung.Marce;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

import ubung.co.ubung.Utilidades.SolicitudesDBUtilities;
import ubung.co.ubung.R;

/**
 * Created by icmontesdumar on 29/06/17.
 */

public class SolicitudesAdapter extends RecyclerView.Adapter<SolicitudesAdapter.SolicitudesViewHolder> {

    private  DatabaseReference solicitudesDB;
    private  StorageReference solicitudesSR;
    private int cantidadSolicitudes;
    private final static String APP_ID ="1:539271845073:android:82756593d6c782a7" ;
    private final static String PROYECT_ID= "ubung-4f2c1";
    private final static String APIKEY =
            "AIzaSyCFWESIjexEC-I6AkapwbHtoMMXa3hgZPU";
    private final static String TAG="SolicitudesAdapter";
   // private final ValueEventListener valueEventListener;

    private static Context context;



    private String[] mapaPosicionViewHolderNombre;
    private boolean[] mapaPosicionViewHolderFoto;
    private String[] mapaPosicionViewHolderUids;



    public SolicitudesAdapter(Context c){

        context=c;


        cantidadSolicitudes =0;

        mapaPosicionViewHolderFoto= new boolean[cantidadSolicitudes];
        mapaPosicionViewHolderNombre= new String[cantidadSolicitudes];
        mapaPosicionViewHolderUids= new String[cantidadSolicitudes];

        solicitudesDB = FirebaseDatabase.getInstance().getReference(context.getString(R.string.nombre_solicitudedFDB));
        solicitudesSR= FirebaseStorage.getInstance().getReference(context.getString(R.string.nomble_fotos_perfilSR));

        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String uid = dataSnapshot.getKey();
                final String nom = (String) dataSnapshot.child(context.getString(R.string.db_nombre)).getValue();
                boolean fot = (boolean) dataSnapshot.child(context.getString(R.string.db_foto)).getValue();

                anadirSolicitud(nom,fot,uid);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String nom = (String) dataSnapshot.child(context.getString(R.string.db_nombre)).getValue();
                quitarSolicitud(nom);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        solicitudesDB.addChildEventListener(childEventListener);



    }
    @Override
    public SolicitudesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.holder_solicitud;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        SolicitudesViewHolder holder = new SolicitudesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SolicitudesViewHolder holder, int position) {

        holder.nombre.setText(mapaPosicionViewHolderNombre[position]);
        holder.uid = mapaPosicionViewHolderUids[position];
        holder.tieneFoto=mapaPosicionViewHolderFoto[position];
        if(mapaPosicionViewHolderFoto[position]){
            StorageReference sr= solicitudesSR.child(mapaPosicionViewHolderUids[position]);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(sr)
                    .into(holder.foto);
        }
        //TODO: Descargar la libreria glide y usarla para poner la foto en el ImageView

    }

    @Override
    public int getItemCount() {
        return cantidadSolicitudes;
    }

    public class SolicitudesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView foto;
        boolean tieneFoto;
        TextView nombre;
        Button aceptar;
        Button rechaButton;
        String uid;
        public SolicitudesViewHolder(View itemView) {
            super(itemView);
            foto = (ImageView) itemView.findViewById(R.id.holder_solicitud_foto);
            nombre = (TextView) itemView.findViewById(R.id.holder_solicitud_tv_nombre);
            rechaButton= (Button) itemView.findViewById(R.id.holder_solicitud_boton_rechazar);
            aceptar= (Button) itemView.findViewById(R.id.holder_solicitud_boton_aceptar);
            aceptar.setOnClickListener(SolicitudesViewHolder.this);
            rechaButton.setOnClickListener(SolicitudesViewHolder.this);
            tieneFoto=false;
        }

        public void onClick(View v) {
            final String stringNombre= nombre.getText().toString();

            final View view = v;
//            switch (v.getId()){
//
//                case R.id.holder_solicitud_boton_aceptar:

                    //VOY ACA
                    //Tenga en cuenta
                    // no se esta completando to do lo que hace este listen

                    solicitudesDB.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DatabaseReference usuariosDB=solicitudesDB.getParent().child(context.getString(R.string.nombre_clienteFDB));
                            DataSnapshot soliData=dataSnapshot.child(uid);

                            if(soliData==null) return;
                            String dsCorreo= (String)soliData.child(context.getString(R.string.db_correo)).getValue();
                            String dsContresena = (String)soliData.child(context.getString(R.string.db_sol_contrasena)).getValue();

                            String[] strings=SolicitudesDBUtilities.getStrings(context);



//                            if(uid==null){
//                                Toast.makeText(context,"Se notifcara al usuario que metio mal la informacion.", Toast.LENGTH_LONG).show();
//                                return;
//                            }
                            switch (view.getId()) {

                                case R.id.holder_solicitud_boton_aceptar:
                                    DatabaseReference usuario = usuariosDB.child(uid);


                                    for (String espacio : strings) {
                                        DataSnapshot dataSnapAPegar = soliData.child(espacio);
                                        Object dataAPegar =  dataSnapAPegar.getValue();
                                        usuario.child(espacio).setValue(dataAPegar);
                                    }

                                    solicitudResuelta(uid);
                                    break;
                                case R.id.holder_solicitud_boton_rechazar:
                                    eliminarUsuario(dsCorreo,dsContresena,tieneFoto);

                                    if (FirebaseAuth.getInstance().getCurrentUser()!=null) Log.e(TAG,FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    solicitudResuelta(uid);
                                    break;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            //TODO:Notificar error

                        }

                    });
//                    break;
//                case R.id.holder_solicitud_boton_rechazar:
//
//                    //TODO:ENVIAR Notificacion al correo que no fue aceptado
//
//
//                    break;
//
//            }


        }

        public void solicitudResuelta(String uid){
            solicitudesDB.child(uid).removeValue();

        }

    }



    /**
     * Agrega un nuevo usuario sin cerrar secion de Marce o cualquiera con poder supremo.
     *
     * @param correo
     * @param contraseña
     * @return el uid del correo del Usuario si fue agregado correctamente, null de lo contrario;
     */
//    public static String crearUsuario(String correo, String contraseña){
//        FirebaseOptions.Builder builder= new FirebaseOptions.Builder();
//        FirebaseOptions options= builder.setApiKey(APIKEY).setApplicationId(APP_ID).setProjectId(PROYECT_ID).build();
//        FirebaseApp segundaApp = FirebaseApp.initializeApp(context,FirebaseApp.getInstance().getOptions(),"segundaapp");
//        final FirebaseAuth secondAuth = FirebaseAuth.getInstance(segundaApp);
//        String uid=null;
//        Task t = secondAuth.createUserWithEmailAndPassword(correo, contraseña);
//
//        t.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(!task.isSuccessful())
//                        //TODO: Notificar ERROR
//                        Log.e("Hubo","un problemacom")
//                                ;
//                    else {
//                        Log.i("Si sirvio","punto com");
//                    }
//                }
//
//            });
//        FirebaseUser user = secondAuth.getCurrentUser();
//        if(user!=null)
//        uid=user.getUid();
//        secondAuth.signOut();
//        return uid;
//    }

    public void eliminarUsuario(String correo, String contraseña, boolean tieneFoto){
        FirebaseApp segundaApp = FirebaseApp.initializeApp(context,FirebaseApp.getInstance().getOptions(),"segundaapp");
        final FirebaseAuth secondAuth = FirebaseAuth.getInstance(segundaApp);
        secondAuth.signInWithEmailAndPassword(correo,contraseña);
        FirebaseUser u = secondAuth.getCurrentUser();
        if(tieneFoto)
        solicitudesSR.child(u.getUid()).delete();
        u.delete();    }
    /**
     * Anade una solicitud a ambos mapas y aumenta en 1 la cantidad de solicitudes
     * @param nombre
     * @param foto
     */
    public void anadirSolicitud(String nombre, boolean foto, String uid){

        cantidadSolicitudes++;


        String[] tempNom= new String[cantidadSolicitudes];
        boolean[] tempFot= new boolean[cantidadSolicitudes];
        String[] tempUs= new String[cantidadSolicitudes];
        int i=0;
        for(; i<cantidadSolicitudes-1; i++){
            tempNom[i]=mapaPosicionViewHolderNombre[i];
            tempFot[i]= mapaPosicionViewHolderFoto[i];
            tempUs [i]= mapaPosicionViewHolderUids[i];
        }
        tempNom[cantidadSolicitudes-1]=nombre;
        tempUs[cantidadSolicitudes-1]=uid;

        tempFot[cantidadSolicitudes-1]=foto;
        mapaPosicionViewHolderNombre=tempNom;
        mapaPosicionViewHolderFoto=tempFot;
        mapaPosicionViewHolderUids=tempUs;
        notifyItemInserted(cantidadSolicitudes-1);
    }

    /**
     *
     * @param nombre
     * @return la posicion de la que fue eliminada la solicitud, -1 si no se elimino.
     */
    public void quitarSolicitud(String nombre){

        cantidadSolicitudes--;
        String[] tempNom= new String[cantidadSolicitudes];
        boolean[] tempFot= new boolean[cantidadSolicitudes];
        String[] tempUs= new String[cantidadSolicitudes];
        int i=0;
        int ret=-1;
        // esto cambia a uno cuando encuentra la coincidencia y se le suma al indice para sacarlo de la antigua lista
        int uno=0;
        for(; i<cantidadSolicitudes+1; i++){
            if(!mapaPosicionViewHolderNombre[i].equals(nombre)){
                tempNom[i-uno]=mapaPosicionViewHolderNombre[i];
                tempFot[i-uno]= mapaPosicionViewHolderFoto[i];
                tempUs [i-uno]= mapaPosicionViewHolderUids[i];

            }
            else {
                ret = i;
                uno=1;
            }
        }
        mapaPosicionViewHolderNombre=tempNom;
        mapaPosicionViewHolderFoto=tempFot;
        mapaPosicionViewHolderUids=tempUs;


        notifyItemRemoved(ret);
    }




}
