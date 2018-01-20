package ubung.co.ubung.Marce;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ubung.co.ubung.Objetos.ClaseGenerica;
import ubung.co.ubung.R;

public class SemanaGenericaActivity extends AppCompatActivity implements EventListener<QuerySnapshot>{

    public static final String KET_DB_DISPONIBLE = "disponible";
    private final static String collectionDiasPath="semanas/semanaGenerica/dias";
    public final static String KEY_HORAMAYOR = "holasoy unakey";
    public final static String KEY_HORAMENOR = "holasoy otrakey";
    public final static String KEY_CANTESTUDIANTESCLASES = "keycantestclas";
    public final static String KEY_CANTCLASESHORA = "keycantclas";
    private static final String TAG = "SemanaGenericaActivity";
    public static final String CLIENTE_KEY = "clientes";
    public static final String PROFESOR_KEY = "profesores";
    public static final String NOMBRE_COM = "nombre.com";
    public static final String UID_ORG = "uid.org";
    private static final int REQUEST_CODE = 245;
    private int dias;
    private final static String collectionHoritasNamePath="horas";
    private HashMap<Integer,ClaseGenerica>[] horas;
    private CollectionReference collectionDiasReference;
    private FirebaseFirestore paraTransacciones;
    private int menorHoraInit;
    private int mayorHoraFin;
    private ListenerRegistration[] paraCerar;
    private AdaptadorDeEstaActividad adaptador;
    private int cantidadDeHoras;
    private int tamanoCuadrito;
    private int columnas;
    private int cantAlumnosCalse;
    private int cantClasesHora;
    private DialogoParaEto onDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semana_generica);
        //TODO: hacer esto mas generico
        dias=6;
        columnas=dias+1;
        Display d= getWindowManager().getDefaultDisplay();
        Point p= new Point();
        d.getSize(p);
        int tamano=p.x;
        tamano/=columnas;
        tamanoCuadrito=tamano;

        paraTransacciones=FirebaseFirestore.getInstance();
        cantClasesHora=getIntent().getIntExtra(KEY_CANTCLASESHORA,2);
        cantAlumnosCalse=getIntent().getIntExtra(KEY_CANTESTUDIANTESCLASES,2);
        mayorHoraFin = getIntent().getIntExtra(KEY_HORAMAYOR,0);
        menorHoraInit = getIntent().getIntExtra(KEY_HORAMENOR,100);
        cantidadDeHoras = mayorHoraFin - (menorHoraInit - 1);
        Log.i(TAG+"I",""+menorHoraInit);
        Log.i(TAG+"F",""+mayorHoraFin);
        int filas= mayorHoraFin-menorHoraInit;
        horas= new HashMap[dias];
        paraCerar= new ListenerRegistration[dias];
        for (int i=0;i<horas.length; i++){
            horas[i]= new HashMap<Integer, ClaseGenerica>(12);
        }

        collectionDiasReference= paraTransacciones.collection(collectionDiasPath);

        for(int i=1; i<=6; i++){
           paraCerar[i-1]= collectionDiasReference.document(""+i).collection(collectionHoritasNamePath).addSnapshotListener(this);
        }

        RecyclerView rv= (RecyclerView) findViewById(R.id.semana_generica_recycler_view);
        adaptador=new AdaptadorDeEstaActividad();
        rv.setAdapter(adaptador);
        rv.setLayoutManager(new GridLayoutManager(this,dias+1));
        hacerLaPrimerFila((LinearLayout) findViewById(R.id.semana_generica_header));
    }


    public void hacerLaPrimerFila(ViewGroup amarrarAEsto){
        String[] diasSemana= {"",getString(R.string.lunesab),getString(R.string.martesab),
                getString(R.string.miercolesab),getString(R.string.juevesab),getString(R.string.viernesab),
                getString(R.string.sabadoab)};
        for (int i=0;i<7;i++){
            FrameLayout v= new FrameLayout(this);
            TextView tv= new TextView(this);
            v.setBackgroundResource(R.drawable.caja_casilla_calendario);
            v.setLayoutParams(new ViewGroup.LayoutParams(tamanoCuadrito,tamanoCuadrito));
            tv.setText(diasSemana[i]);
            tv.setPadding(5,5,0,0);
            v.addView(tv);
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
            ClaseGenerica claseGenerica = doc.toObject(ClaseGenerica.class);
            int hora =  claseGenerica.getHora();
            int dia=Integer.parseInt(doc.getReference().getParent().getParent().getId());
//            HashMap<String, String> profes= new HashMap<>();
//            HashMap<String, String> client= new HashMap<>();

//            for (Map.Entry<String, Object> entry : doc.getData().entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue().toString();
//                if(key.contains(CLIENTE_KEY)){
//                    client.put(key,value);
//                }
//                else if(key.contains(PROFESOR_KEY)){
//                    profes.put(key,value);
//
//                }
//
//            }
//            boolean disponible;
//            try {
//                disponible = doc.getBoolean(KET_DB_DISPONIBLE);
//
//
//            }
//            catch (RuntimeException re){
//                disponible=false;
//            }
            int posicionANotificar=(hora-menorHoraInit)*(dias+1)+dia;
            if (dc.getType().equals(DocumentChange.Type.REMOVED)) {
                horas[dia-1].remove(hora);
                if(hora==mayorHoraFin)
                adaptador.notifyItemRemoved(posicionANotificar);
            }
            else{
                horas[dia-1].put(hora,claseGenerica);
                if(hora>mayorHoraFin){mayorHoraFin=hora;
                adaptador.notifyDataSetChanged();}
                else if(hora<menorHoraInit){menorHoraInit=hora;
                    adaptador.notifyDataSetChanged();
                }
//                if(dc.getType().equals(DocumentChange.Type.ADDED)) ;
////                    adaptador.notifyItemInserted(posicionANotificar);
//                else
                    adaptador.notifyItemChanged(posicionANotificar);


            }

        }
    }

    public class AdaptadorDeEstaActividad extends RecyclerView.Adapter<AdaptadorDeEstaActividad.HolderDeEsto>{


        @Override
        public HolderDeEsto onCreateViewHolder(ViewGroup parent, int viewType) {

            FrameLayout v = new FrameLayout(SemanaGenericaActivity.this);
            v.setLayoutParams(new ViewGroup.LayoutParams(tamanoCuadrito,tamanoCuadrito));
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
            ClaseGenerica b=horas[dIa-1].get(hora);

            if(b!=null)holder.hacerDisponibleEsteticamente(b);



        }

        @Override
        public int getItemCount() {


            return cantidadDeHoras*(dias+1);

        }

        public class HolderDeEsto extends RecyclerView.ViewHolder implements View.OnClickListener{
            int hora;
            int dia;
            TextView tv;

            ClaseGenerica data;
            

            public HolderDeEsto(View itemView) {
                super(itemView);
                tv=new TextView(itemView.getContext());
                ((FrameLayout)itemView).addView(tv);

                itemView.setOnClickListener(this);
                this.itemView.setBackgroundResource(R.drawable.caja_casilla_calendario_no_disp);


            }
            public void anadirTV(){

                itemView.setBackgroundResource(R.drawable.caja_casilla_calendario);
                tv.setVisibility(View.VISIBLE);
                String s;
                if(hora==12) s= "12:00 p.m.";
                else if(hora>12) s=""+hora%12+":00 p.m.";
                else  s=""+hora+":00 a.m.";
                tv.setText(s);
                tv.setPadding(5,5,0,0);

            }

            public void hacerDisponibleEsteticamente(ClaseGenerica d){

                data=d;
                tv.setVisibility(View.GONE);

                if(data.isDisponible()) itemView.setBackgroundResource(R.drawable.caja_casilla_calendario);
                else itemView.setBackgroundResource(R.drawable.caja_casilla_calendario_no_disp);
            }

            @Override
            public void onClick(View v) {

                if(dia==0) return;
                onDisplay = new DialogoParaEto();

                onDisplay.construir(hora,dia);

//                final boolean huboCambios=false;
//                AlertDialog.Builder builder = new AlertDialog.Builder(SemanaGenericaActivity.this);
//                builder.setTitle(nombreDias[dia-1]+" - "+ hora+":00");
//                LinearLayout linearLayout= new LinearLayout(SemanaGenericaActivity.this);
//                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                linearLayout.setOrientation(LinearLayout.VERTICAL);
//
                int profesSize=0;
                int clientesSize=0;
                if(data!=null) {
                    if(data.getClientes()!=null)clientesSize=data.getClientes().size();
                    if(data.getProfesores()!=null)profesSize = data.getProfesores().size();
                }
                if(profesSize<cantClasesHora){
                    onDisplay.anadirBotonAnadirProfesor();
                }
//
//
                for (int i = 0; i < profesSize; i++) {
                    String nombreeid = data.getProfesores().get(i);
                    if(nombreeid==null) break;

                    String nombre= nombreeid.split(",")[1];
                    onDisplay.anadirProfesor(nombre);
                }
                if(clientesSize<cantAlumnosCalse){
                    onDisplay.anadirBotonCliente();
                }
//
                for (int i = 0; i < clientesSize; i++) {
                    final String[] nombreeid = data.getClientes().get(i).split(",");
                    if(nombreeid==null) break;
                    String nombre= nombreeid[0];
                    onDisplay.anadirCliente(nombre);
                }
                onDisplay.anadirBotonEliminar();


            }


        }

        public void cambiarDisponibilidadHora(int hora, int dia, boolean dis){
            Map<String,Object> mapa = new HashMap();
            mapa.put(KET_DB_DISPONIBLE,dis);
            collectionDiasReference.document(""+dia).collection(collectionHoritasNamePath)
                    .document(""+hora).update(mapa);
        }


    }

    private void eliminarClienteDeDiaYHora(int dia, int hora, int j) {
        HashMap<String,Object> map = new HashMap();
        map.put(CLIENTE_KEY+j, FieldValue.delete());
        collectionDiasReference.document(""+dia).collection(collectionHoritasNamePath)
                .document(""+hora).update(map);
    }
    private void eliminarHora(int dia, int hora, int cantidadDeClientes, int canTidadDeProfes) {
        HashMap<String,Object> map = new HashMap();

            map.put(CLIENTE_KEY, FieldValue.delete());


            map.put(PROFESOR_KEY, FieldValue.delete());

        map.put(KET_DB_DISPONIBLE,false);
        collectionDiasReference.document(""+dia).collection(collectionHoritasNamePath)
                .document(""+hora).update(map);
    }


    @Override
    protected void onDestroy() {
        for(int i=0; i<=5;i++){
            paraCerar[i].remove();
        }
        super.onDestroy();
    }
    public class ObjetoHoras {
        boolean disponible;
        // lo que va a guardar ahi es de la forma (key: clientex , value: iddelpisco,nombredelpisco)
        HashMap<String,String> profesores;
        HashMap<String,String> clientes;
        ObjetoHoras(boolean d){
            disponible=d;
            profesores=new HashMap<>();
            clientes= new HashMap<>();
        }
        ObjetoHoras(boolean d, HashMap<String,String> profes,HashMap<String,String> cli){
            disponible=d;
            if(profes==null)
            profesores=new HashMap<>();
            else profesores=profes;
            if(cli==null)
            clientes= new HashMap<>();
            else clientes=cli;
        }


    }

    public void agregarProfesorOCliente(boolean esCli){
        esClienteParaRespuesta=esCli;
        Intent i = new Intent(this,ListaClientesOProfesores.class);
        i.putExtra(ListaClientesOProfesores.KEY_ES_CLIENTE,esCli);
        i.putExtra(ListaClientesOProfesores.KEY_NECESITARESULTADO,true);
        startActivityForResult(i,REQUEST_CODE);

    }
    private boolean esPerandoRespuesta;
    private int diaParaRespuesta;
    private int horaParaRespuesta;

    private boolean esClienteParaRespuesta;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if(!esPerandoRespuesta) return;

            String nombre=data.getStringExtra(NOMBRE_COM);

            String uid = data.getStringExtra(UID_ORG);
            HashMap<String,Object> map = new HashMap();
            String k;
            ArrayList<String> toUpdate;
            if(esClienteParaRespuesta){ k=CLIENTE_KEY;
            onDisplay.anadirCliente(nombre);
                toUpdate=horas[diaParaRespuesta-1].get(horaParaRespuesta).getClientes();}
            else {k=PROFESOR_KEY;
            onDisplay.anadirProfesor(nombre);
            toUpdate=horas[diaParaRespuesta-1].get(horaParaRespuesta).getProfesores();}
            if(toUpdate==null) toUpdate=new ArrayList<>();
            toUpdate.add(nombre+","+uid);
            map.put(k, toUpdate);

            map.put(KET_DB_DISPONIBLE, true);
            collectionDiasReference.document(""+diaParaRespuesta).collection(collectionHoritasNamePath)
                    .document(""+horaParaRespuesta).update(map);

        }
    }

    public class DialogoParaEto {

        private final String[] nombreDias ={"Lunes","Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

        AlertDialog.Builder builder;
        LinearLayout linearLayoutProfes;
        LinearLayout linearLayoutClientes;
        LinearLayout linearLayout;
        View botonAnadirCliente;
        View botonAnadirProfesor;
        
        int cantidadClientes;
        int cantidadProfes;
        int horaDialogo;
        int diaDialogo;

        public AlertDialog construir(int hora, int dia) {

            horaDialogo =hora;
            diaDialogo =dia;
            builder= new AlertDialog.Builder(SemanaGenericaActivity.this);
            builder.setTitle(nombreDias[dia-1]+" - "+ hora+":00");
            linearLayout= new LinearLayout(SemanaGenericaActivity.this);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayoutProfes= new LinearLayout(SemanaGenericaActivity.this);
            linearLayoutProfes.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayoutProfes.setOrientation(LinearLayout.VERTICAL);
            linearLayoutClientes= new LinearLayout(SemanaGenericaActivity.this);
            linearLayoutClientes.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayoutClientes.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(linearLayoutProfes);
            linearLayout.addView(linearLayoutClientes);

            cantidadClientes=0;
            cantidadProfes=0;
            builder.setView(linearLayout);
            return builder.show();

        }

        public void anadirCliente(String nombre){
            if(cantidadClientes<cantClasesHora) {
                TextView tv = new TextView(SemanaGenericaActivity.this);
                tv.setText(nombre);
                linearLayoutClientes.addView(tv);
                cantidadClientes++;
                if(cantidadClientes==cantClasesHora){
                    linearLayoutClientes.removeView(botonAnadirCliente);
                    botonAnadirCliente=null;
                }
            }
            else{
                Toast.makeText(SemanaGenericaActivity.this, "No se puede agreagar otro cliuente, hora llena.", Toast.LENGTH_LONG);
            }

        }
        public void anadirProfesor(String nombre){
            if(cantidadProfes<cantClasesHora) {
                TextView tv = new TextView(SemanaGenericaActivity.this);
                tv.setText(nombre);
                linearLayoutClientes.addView(tv);
                cantidadProfes++;
                if(cantidadProfes==cantClasesHora){
                    linearLayoutClientes.removeView(botonAnadirCliente);
                    botonAnadirCliente=null;
                }
            }
            else{
                Toast.makeText(SemanaGenericaActivity.this, "No se puede agreagar otro cliuente, hora llena.", Toast.LENGTH_LONG);
            }
        }
        public void anadirBotonEliminar(){
            TextView tv = new TextView(SemanaGenericaActivity.this);
            tv.setText("Eliminar Cita");
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminarHora(diaDialogo,horaDialogo,cantidadClientes,cantidadProfes);
                }
            });
            linearLayout.addView(tv);

        }

        public void anadirBotonCliente(){
            View vi1= getLayoutInflater().inflate(R.layout.semana_generica_anadir_cliente,linearLayout,false);
            TextView tvi1 = (TextView) ((ViewGroup)vi1).getChildAt(1);
            int res1 = R.string.anadir_cliente;
            tvi1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    diaParaRespuesta= diaDialogo;
                    horaParaRespuesta= horaDialogo;
                    esPerandoRespuesta=true;
                    agregarProfesorOCliente(true);

                }});
            (tvi1).setText(getString(res1));

            linearLayoutClientes.addView(vi1);
            botonAnadirCliente=vi1;
        }
        public void anadirBotonAnadirProfesor(){
            View vi= getLayoutInflater().inflate(R.layout.semana_generica_anadir_cliente,linearLayout,false);
            TextView tvi = (TextView) ((ViewGroup)vi).getChildAt(1);
            int res;
            res= R.string.anadir_profesor;
            tvi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    diaParaRespuesta= diaDialogo;
                    horaParaRespuesta= horaDialogo;
                    esPerandoRespuesta=true;
                    agregarProfesorOCliente(false);

                }});
            (tvi).setText(getString(res));
            linearLayout.addView(vi);
            
            botonAnadirProfesor=vi;
        }




    }
}
