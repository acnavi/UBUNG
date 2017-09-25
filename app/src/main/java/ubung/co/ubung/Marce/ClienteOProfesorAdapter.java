package ubung.co.ubung.Marce;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import ubung.co.ubung.R;

/**
 * Created by icmontesdumar on 25/09/17.
 */

public class ClienteOProfesorAdapter extends RecyclerView.Adapter<ClienteOProfesorAdapter.ClienteOProfesorHolder>{

    private Cursor database;
    private Context context;
    private StorageReference foticos;

    /*
    Debe pasar un cursosr cuya primera fila sean los uids y el segundo el nombre.
     */
    public ClienteOProfesorAdapter(Cursor db, Context c){

        database=db;

        context=c;

        foticos= FirebaseStorage.getInstance().getReference(context.getString(R.string.nomble_fotos_perfilSR));
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
                //HacerAlgo
            }
        }
    }
}
