package ubung.co.ubung.Marce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import ubung.co.ubung.PerfilActivity;
import ubung.co.ubung.R;
import ubung.co.ubung.Utilidades.DatabaseManager;

/**
 * Created by icmontesdumar on 25/09/17.
 */

public class ClienteOProfesorAdapter extends RecyclerView.Adapter<ClienteOProfesorAdapter.ClienteOProfesorHolder>{

    private Cursor database;
    private Activity context;
    private StorageReference foticos;
    private boolean esCliente;

    private final static String TAG="ClienteOProfesorAdapter";
    /*
    Debe pasar un cursosr cuya primera fila sean los uids y el segundo el nombre.
     */
    public ClienteOProfesorAdapter(Cursor db, Activity c, boolean esC){

        database=db;

        context=c;

        foticos= FirebaseStorage.getInstance().getReference(context.getString(R.string.nomble_fotos_perfilSR));
        esCliente=esC;
    }
    @Override
    public ClienteOProfesorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.holder_cliente_o_profesor;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ClienteOProfesorHolder holder = new ClienteOProfesorHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ClienteOProfesorHolder holder, int position) {
        if(database.moveToPosition(position)){
            String uid=database.getString(0);
            String nombre=database.getString(1);
            StorageReference sr= foticos.child(uid);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(sr).dontAnimate()
                    .into(holder.foto);
            holder.nombre.setText(nombre);
            holder.uid=uid;
        }

    }

    @Override
    public int getItemCount() {
        return database.getCount();
    }

    public class ClienteOProfesorHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nombre;
        CircleImageView foto;
        String uid;
        public ClienteOProfesorHolder(View itemView) {
            super(itemView);
            nombre=(TextView) itemView.findViewById(R.id.holder_cop_tv);
            foto= (CircleImageView) itemView.findViewById(R.id.holder_cop_iv);
            itemView.setOnClickListener(ClienteOProfesorHolder.this);
        }

        @Override
        public void onClick(View v) {
            if(uid!=null){


                if (context instanceof ListaClientesOProfesores){
                    ListaClientesOProfesores l = (ListaClientesOProfesores) context;
                    l.lanzarPerfil(uid);
                }
                else {
                    Log.e(TAG,"el contexto no esta siendo de tipo ListaClientesOProfesores");
                }

            }
        }
    }
}
