package ubung.co.ubung.Marce;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import ubung.co.ubung.R;

public class SemanaGenericaActivity extends AppCompatActivity implements EventListener<QuerySnapshot>{

    private final static String collectionDiasPath="semanas/semanaGenerica/dias";
    public final static String KEY_HORAMAYOR = "holasoy unakey";
    public final static String KEY_HORAMENOR = "holasoy otrakey";
    private static final String TAG = "SemanaGenericaActivity";
    private int dias;
    private final static String collectionHoritasNamePath="horas";
    private HashMap<Integer,Boolean>[] horas;
    private CollectionReference collectionDiasReference;
    private FirebaseFirestore paraTransacciones;
    private int menorHoraInit;
    private int mayorHoraFin;
    private ListenerRegistration[] paraCerar;
    private AdaptadorDeEstaActividad adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semana_generica);
        paraTransacciones=FirebaseFirestore.getInstance();
        //TODO: hacer esto mas generico
        dias=6;
        mayorHoraFin = getIntent().getIntExtra(KEY_HORAMAYOR,0);
        menorHoraInit = getIntent().getIntExtra(KEY_HORAMENOR,100);
        int filas= mayorHoraFin-menorHoraInit;
        horas= new HashMap[dias];
        paraCerar= new ListenerRegistration[dias];
        for (int i=0;i<horas.length; i++){
            horas[i]= new HashMap<Integer, Boolean>(12);
        }

        collectionDiasReference=paraTransacciones.collection(collectionDiasPath);

        for(int i=1; i<=6; i++){
           paraCerar[i-1]= collectionDiasReference.document(""+i).collection(collectionHoritasNamePath).addSnapshotListener(this);
        }

        RecyclerView rv= (RecyclerView) findViewById(R.id.semana_generica_recycler_view);
        adaptador=new AdaptadorDeEstaActividad();
        rv.setAdapter(adaptador);
        rv.setLayoutManager(new GridLayoutManager(this,dias+1));
//            DocumentReference dr= collectionDiasReference.document(""+i);
//            paraTransacciones.runTransaction(new Transaction.Function<>)
//        }
//        collectionDiasReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot documentSnapshots) {
//                int columnas= documentSnapshots.size();
//                ArrayList<Boolean>[] temp= new ArrayList[columnas+1];
//
//
//
//                for(DocumentSnapshot ds: documentSnapshots){
//                    int columna= Integer.parseInt(ds.getId());
//                    ArrayList<Boolean> arrayDeColumna = new ArrayList<Boolean>();
//                    ds.getDocumentReference()
//
//
//                }
//                llenarCuadrito();
//            }
//        });
//        ScrollView sv= (ScrollView) findViewById(R.id.semana_generica_scroll_view);
//        GridView gv = new GridView(this);
//
//        int filas=10;
//        gv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        gv.setColumnWidth(tamano);
//        gv.setNumColumns(6);
//        hacerLaPrimerFila(tamano,gv);
//        for (int i =0; i<filas; i++){
//
//        }
//        View v = new View(this);
//        v.setBackgroundResource(R.drawable.caja_casilla_calendario);
//        v.setLayoutParams(new ViewGroup.LayoutParams(tamano,tamano));
//        sv.addView(v);
    }


    public void hacerLaPrimerFila(int tamanoCuadrito, ViewGroup amarrarAEsto){
        String[] diasSemana= {"",getString(R.string.lunesab),getString(R.string.martesab),
                getString(R.string.miercolesab),getString(R.string.juevesab),getString(R.string.viernesab),
                getString(R.string.sabadoab)};
        for (int i=0;i<7;i++){
            TextView v= new TextView(this);
            v.setBackgroundResource(R.drawable.caja_casilla_calendario);
            v.setLayoutParams(new ViewGroup.LayoutParams(tamanoCuadrito,tamanoCuadrito));
            v.setText(diasSemana[i]);
            amarrarAEsto.addView(v);
        }
    }

    @Override
    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

        if (e != null) {
            Log.w(TAG, "listen:error", e);
            return;
        }

        if(documentSnapshots.getMetadata().hasPendingWrites()) return;

        for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
            DocumentSnapshot doc= dc.getDocument();
            int hora =  doc.getLong("hora").intValue();
            int dia=Integer.parseInt(doc.getReference().getParent().getParent().getId());
            boolean disponible;
            try {
                disponible = doc.getBoolean("disponible");
            }
            catch (RuntimeException re){
                disponible=false;
            }
            int posicionANotificar=hora*(dias+1)+dia;
            if (dc.getType().equals(DocumentChange.Type.REMOVED)) {
                horas[dia].remove(hora);
                adaptador.notifyItemRemoved(posicionANotificar);
            }
            else{
                horas[dia-1].put(hora,disponible);
                if(hora>mayorHoraFin)mayorHoraFin=hora;
                else if(hora<menorHoraInit)menorHoraInit=hora;
                if(dc.getType().equals(DocumentChange.Type.ADDED)) adaptador.notifyItemInserted(posicionANotificar);
                else adaptador.notifyItemChanged(posicionANotificar);


            }

        }
    }

    public class AdaptadorDeEstaActividad extends RecyclerView.Adapter<AdaptadorDeEstaActividad.HolderDeEsto>{
        int columnas;
        @Override
        public HolderDeEsto onCreateViewHolder(ViewGroup parent, int viewType) {
            columnas=dias+1;
            Display d= getWindowManager().getDefaultDisplay();
            Point p= new Point();
            d.getSize(p);
            int tamano=p.x;
            tamano/=columnas;
            FrameLayout v = new FrameLayout(SemanaGenericaActivity.this);
            v.setLayoutParams(new ViewGroup.LayoutParams(tamano,tamano));
            v.setBackgroundResource(R.drawable.caja_casilla_calendario);
            return new HolderDeEsto(v);
        }

        @Override
        public void onBindViewHolder(HolderDeEsto holder, int position) {
            int dIa= position%(columnas);
            int fila= position/(columnas);
            int hora= menorHoraInit+fila;

                holder.hora=hora;
                holder.dia=dIa;
            if(dIa==0){
                holder.anadirTV();
                return;
        }
        Boolean b=horas[dIa-1].get(hora);

            if(b==null) b=false;
        holder.hacerDisponibleEsteticamente(b);

        }

        @Override
        public int getItemCount() {

            return (mayorHoraFin-menorHoraInit)*(dias+1);

        }

        public class HolderDeEsto extends RecyclerView.ViewHolder{
            int hora;
            int dia;
            TextView tv;
            public HolderDeEsto(View itemView) {
                super(itemView);
                tv=new TextView(itemView.getContext());
                ((FrameLayout)itemView).addView(tv);

            }
            public void anadirTV(){

                tv.setVisibility(View.VISIBLE);
                String s;
                if(hora==12) s= "12:00 p.m.";
                else if(hora>12) s=""+hora%12+":00 p.m.";
                else  s=""+hora+":00 a.m.";
                tv.setText(s);

            }
            public void hacerDisponibleEsteticamente(boolean disponible){
                tv.setVisibility(View.GONE);
                if(disponible) itemView.setBackgroundResource(R.drawable.caja_casilla_calendario);
                else itemView.setBackgroundResource(R.drawable.caja_casilla_calendario_no_disp);
            }
        }


//    public class HoraDisponible implements Comparable<HoraDisponible>{
//        private int hora;
//        private boolean disponible;
//
//        public HoraDisponible(int hora, boolean dis){
//            this.hora=hora;
//            disponible=dis;
//        }
//
//        public int getHora() {
//            return hora;
//        }
//
//        public boolean isDisponible() {
//            return disponible;
//        }
//
//        @Override
//        public int compareTo(@NonNull HoraDisponible o) {
//            if(this.hora<o.hora) return 1;
//            if(this.hora==o.hora) return 0;
//            return -1;
//        }

    }

    @Override
    protected void onDestroy() {
        for(int i=0; i<5;i++){
            paraCerar[i].remove();
        }
        super.onDestroy();
    }
}
