package ubung.co.ubung;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import ubung.co.ubung.Utilidades.LaClaseQueHaceTodoConLasFechas;

public class CrearClaseActivity extends AppCompatActivity {

    public static final String KEY_ES_MARCE = "esMarce.com";
    public static final String KEY_BUNDLE_MESESITO = "mesesito";
    public static final String KEY_BUNDLE_ANITO = "anito";
    public static final String KEY_BUNDLE_DIITA = "diita";
    public static final String KEY_BUNDLE_HORITA = "horita";
    public static final String KEY_BUNDLE_MINUTITO = "minutito";

    public AdaptadorPaginitas adaptadorPaginitas;

    private int mes,ano,dia,hora,minutos;

    private boolean esMarce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adaptadorPaginitas=new AdaptadorPaginitas();
        setContentView(R.layout.activity_crear_clase);
        esMarce=getIntent().getBooleanExtra(KEY_ES_MARCE, false);
        unidorDeFragmentos();
        Intent intent = getIntent();
        Calendar c = Calendar.getInstance();
        mes=(intent.getIntExtra(KEY_BUNDLE_MESESITO,c.get(Calendar.MONTH)));
        ano=(intent.getIntExtra(KEY_BUNDLE_ANITO,c.get(Calendar.YEAR)));
        dia=(intent.getIntExtra(KEY_BUNDLE_DIITA,c.get(Calendar.DAY_OF_MONTH)));
        hora=(intent.getIntExtra(KEY_BUNDLE_HORITA,c.get(Calendar.HOUR)));
        minutos=(intent.getIntExtra(KEY_BUNDLE_MINUTITO,c.get(Calendar.MINUTE)));

    }

    public void unidorDeFragmentos(){
        LinearLayout ll=(LinearLayout) findViewById(R.id.crear_clase_list_for_fragment);
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.add(ll.getId(),new FragmentoFisioOPilates());
        ft.add(ll.getId(),new FragmentoFecha());
        ft.add(ll.getId(),new FragmentoHora());
        if(esMarce)
        ft.add(ll.getId(),new FragmentoAnadirClientes());

        ft.commit();
    }

public static class FragmentoHora extends Fragment{
    int hora,minutos;
    TextView tv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_clase_hora,container,false);
        tv= (TextView) v.findViewById(R.id.f_clase_hora_tv);
        hora=((CrearClaseActivity)getActivity()).hora;
        minutos=((CrearClaseActivity)getActivity()).minutos;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String enLaMitad=":";
                        if(minute<10)  enLaMitad+=0;
                        tv.setText(hourOfDay+enLaMitad+minute);
                    }
                };
                TimePickerDialog dialog= new TimePickerDialog(getContext(),listener,hora,minutos,true);
                dialog.show();
            }
        });
        return v;
    }

}
    public static class FragmentoFecha extends Fragment{
        TextView diaSemanaTV,diaTV,mesyanoTV;
        int mes,dia,ano;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v= inflater.inflate(R.layout.fragment_clase_fecha,container,false);
            mes=((CrearClaseActivity)getActivity()).mes;
            ano=((CrearClaseActivity)getActivity()).ano;
            dia=((CrearClaseActivity)getActivity()).dia;
            mesyanoTV=(TextView) v.findViewById(R.id.f_clase_fecha_mes_y_ano);
            diaSemanaTV=(TextView) v.findViewById(R.id.f_clase_fecha_dia_de_la_sem);
            diaTV=(TextView) v.findViewById(R.id.f_clase_fecha_numero);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String mesi= LaClaseQueHaceTodoConLasFechas.getMes(month);
                            mesyanoTV.setText(mesi+" "+year);
                            diaTV.setText(""+dayOfMonth);
                            String diaSemanaS= LaClaseQueHaceTodoConLasFechas.getDiaDeLaSemana(year,month,dayOfMonth,getContext());
                            diaSemanaTV.setText(diaSemanaS);
                        }
                    };
                    DatePickerDialog datePicker = new DatePickerDialog(getContext(), dateSetListener,ano,
                          mes  , dia);
                    datePicker.show();
                }
            });
            return v;
        }

    }
    public static class FragmentoFisioOPilates extends Fragment{

        ViewPager vp;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v= inflater.inflate(R.layout.fragment_clase_fisio_o_pilates,container,false);
            vp=(ViewPager) v.findViewById(R.id.f_clase_fisio_o_pialtes_viewpager);
            PagerAdapter pa= ((CrearClaseActivity)getActivity()).adaptadorPaginitas;
            vp.setAdapter(pa);
            TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(vp, true);
            return v;
        }

        public int enQueEstaElPager(){
            return vp.getCurrentItem();
        }

    }
    public class AdaptadorPaginitas extends PagerAdapter{

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==(View) object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int iview;
            switch (position){
                case 0:
                    iview=R.id.f_clase_fisio_o_pialtes_pilates;
                    break;
                case 1:
                    iview=R.id.f_clase_fisio_o_pialtes_fisio;
                    break;
                default: return null;
            }
            return findViewById(iview);
        }
    }
    public static class FragmentoAnadirClientes extends Fragment{


        CircleImageView cv1, cv2;
        TextView tv1,tv2;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v= inflater.inflate(R.layout.fragment_clase_anadir_participantes,container,false);
            cv1=(CircleImageView)v.findViewById(R.id.f_clase_anadir_foto_cliente1);
            cv2=(CircleImageView)v.findViewById(R.id.f_clase_anadir_foto_cliente2);
            tv2=(TextView)v.findViewById(R.id.f_clase_anadir_tv_cliente2);
            tv1=(TextView)v.findViewById(R.id.f_clase_anadir_tv_cliente2);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hicieronClick();
                }
            });
            return v;
        }

        public void hicieronClick(){
            Toast.makeText(getActivity(),"hicieronClickEnAnadir",Toast.LENGTH_LONG).show();
        }



    }
}
