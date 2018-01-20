package ubung.co.ubung;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ubung.co.ubung.Marce.SemanaGenericaActivity;
import ubung.co.ubung.Objetos.Clase;
import ubung.co.ubung.Objetos.ClaseGenerica;
import ubung.co.ubung.Utilidades.DatabaseManager;
import ubung.co.ubung.databinding.FragmentSemanaBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Semana.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Semana#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Semana extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LUNES = "lunes";
    private static final String ARG_DIAS_DEL_MES= "ddm";
    private static final String ARG_TAMANOPANTALLA= "tdepan";
    public static final String ARGS_REFID = "refid";
    public static final String ARGS_TIPOAPP = "tipapp";


    // TODO: Rename and change types of parameters
    private int lunes;
    private int[] todosLosNumeros;
    private int diasDelMes;
    private int tamanoCuadrito;
    private HashMap<Integer,Clase>[] horas;
    private FragmentSemanaBinding binding;
    private DatabaseManager.TipoAplicacion tipoApp;
    private int diasColumnas;
    private CollectionReference collectionReference;


    private OnFragmentInteractionListener mListener;

    public Semana() {
        // Required empty public constructor


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lun dia del mes que es lunes,. si es
     * @param dias cantidad de dias en el mes de esta semana.

     * @return A new instance of fragment Semana.
     */
    public static Semana newInstance(int lun, int dias, int tamanoPantalla, String referencia, String tipoApp) {
        Semana fragment = new Semana();

        Bundle args = new Bundle();

        args.putInt(ARG_LUNES, lun);
        args.putInt(ARG_DIAS_DEL_MES, dias);
        args.putInt(ARG_TAMANOPANTALLA,tamanoPantalla);
        args.putString(ARGS_REFID,referencia);
        args.putString(ARGS_TIPOAPP,tipoApp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            todosLosNumeros= new int[6];
            lunes = getArguments().getInt(ARG_LUNES);
            diasDelMes=getArguments().getInt(ARG_DIAS_DEL_MES);
            diasColumnas=6;
            for(int i=0; i<6;i++){
                int j = lunes+i;
                if(j>diasDelMes) j%=diasDelMes;
                todosLosNumeros[i]=j;
            }
            tamanoCuadrito=getArguments().getInt(ARG_TAMANOPANTALLA)/6;
            tipoApp= DatabaseManager.TipoAplicacion.tipoPorString(getArguments().getString(ARGS_TIPOAPP));
            horas= new HashMap[6];
            for (int i=0;i<horas.length; i++){
                horas[i]= new HashMap<Integer, Clase>(12);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.activity_semana_generica,container);
        LinearLayout ll=(LinearLayout) v.findViewById(R.id.semana_generica_header);


        hacerLaPrimerFila(ll,todosLosNumeros);

        return v;
//        View v=inflater.inflate(R.layout.fragment_semana,container,false);
//        ((TextView)v.findViewById(R.id.semana_tv_dia_lun)).setText(""+todosLosNumeros[0]);
//        ((TextView)v.findViewById(R.id.semana_tv_dia_mar)).setText(""+todosLosNumeros[1]);
//        ((TextView)v.findViewById(R.id.semana_tv_dia_mier)).setText(""+todosLosNumeros[2]);
//        ((TextView)v.findViewById(R.id.semana_tv_dia_jue)).setText(""+todosLosNumeros[3]);
//        ((TextView)v.findViewById(R.id.semana_tv_dia_vie)).setText(""+todosLosNumeros[4]);
//        ((TextView)v.findViewById(R.id.semana_tv_dia_sab)).setText(""+todosLosNumeros[5]);
//        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_semana, container, false);
//        binding.semanaTvDiaLun.setText(""+todosLosNumeros[0]);
//        binding.semanaTvDiaMar.setText(""+todosLosNumeros[1]);
//        binding.semanaTvDiaMier.setText(""+todosLosNumeros[2]);
//        binding.semanaTvDiaJue.setText(""+todosLosNumeros[3]);
//        binding.semanaTvDiaVie.setText(""+todosLosNumeros[4]);
//        binding.semanaTvDiaSab.setText(""+todosLosNumeros[5]);
//        View v=binding.getRoot();
//        return  v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void hacerLaPrimerFila(ViewGroup amarrarAEsto, int[] dias){
        String[] diasSemana= {"",getString(R.string.lunesab),getString(R.string.martesab),
                getString(R.string.miercolesab),getString(R.string.juevesab),getString(R.string.viernesab),
                getString(R.string.sabadoab)};
        for (int i=0;i<7;i++){
            FrameLayout v= new FrameLayout(getContext());
            TextView tv= new TextView(getContext());

            v.setBackgroundResource(R.drawable.caja_casilla_calendario);
            v.setLayoutParams(new ViewGroup.LayoutParams(tamanoCuadrito,tamanoCuadrito));
            tv.setText(diasSemana[i]);
            tv.setPadding(5,5,0,0);
            v.addView(tv);
            if(i>0){
                TextView tv2= new TextView(getContext());
                tv2.setText(""+dias[i-1]);
                tv2.setPadding(5,10,0,0);
                v.addView(tv2);
            }
            amarrarAEsto.addView(v);
        }
}
//    public class AdaptadorDeEstaActividad extends RecyclerView.Adapter<Semana.AdaptadorDeEstaActividad.HolderDeEsto>{
//
//
//        @Override
//        public Semana.AdaptadorDeEstaActividad.HolderDeEsto onCreateViewHolder(ViewGroup parent, int viewType) {
//
//            FrameLayout v = new FrameLayout(getActivity());
//            v.setLayoutParams(new ViewGroup.LayoutParams(tamanoCuadrito,tamanoCuadrito));
//            v.setBackgroundResource(R.drawable.caja_casilla_calendario);
//            return new Semana.AdaptadorDeEstaActividad.HolderDeEsto(v);
//        }
//
//        @Override
//        public void onBindViewHolder(Semana.AdaptadorDeEstaActividad.HolderDeEsto holder, int position) {
//            int dIa= position%(diasColumnas+1);
//            int fila= position/(diasColumnas+1);
//
////            int hora= menorHoraInit+fila;
//
//            holder.hora=hora;
//            holder.dia=dIa;
//            if(dIa==0){
//                holder.anadirTV();
//                return;
//            }
////            ClaseGenerica b=horas[dIa-1].get(hora);
//
////            if(b!=null)holder.hacerDisponibleEsteticamente(b);
//
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//
//
////            return cantidadDeHoras*(dias+1);
//
//        }
//
//        public class HolderDeEsto extends RecyclerView.ViewHolder implements View.OnClickListener{
//            int hora;
//            int dia;
//            TextView tv;
//
//            ClaseGenerica data;
//
//
//            public HolderDeEsto(View itemView) {
//                super(itemView);
//                tv=new TextView(itemView.getContext());
//                ((FrameLayout)itemView).addView(tv);
//
//                itemView.setOnClickListener(this);
//                this.itemView.setBackgroundResource(R.drawable.caja_casilla_calendario_no_disp);
//
//
//            }
//            public void anadirTV(){
//
//                itemView.setBackgroundResource(R.drawable.caja_casilla_calendario);
//                tv.setVisibility(View.VISIBLE);
//                String s;
//                if(hora==12) s= "12:00 p.m.";
//                else if(hora>12) s=""+hora%12+":00 p.m.";
//                else  s=""+hora+":00 a.m.";
//                tv.setText(s);
//                tv.setPadding(5,5,0,0);
//
//            }
//
//            public void hacerDisponibleEsteticamente(ClaseGenerica d){
//
//                data=d;
//                tv.setVisibility(View.GONE);
//
//                if(data.isDisponible()) itemView.setBackgroundResource(R.drawable.caja_casilla_calendario);
//                else itemView.setBackgroundResource(R.drawable.caja_casilla_calendario_no_disp);
//            }
//
//            @Override
//            public void onClick(View v) {
//
//                if(dia==0) return;
//
//
//
//            }
//
//
//        }
//
////        public void cambiarDisponibilidadHora(int hora, int dia, boolean dis){
////            Map<String,Object> mapa = new HashMap();
////            mapa.put(KET_DB_DISPONIBLE,dis);
////            collectionDiasReference.document(""+dia).collection(collectionHoritasNamePath)
////                    .document(""+hora).update(mapa);
////        }


//    }


}