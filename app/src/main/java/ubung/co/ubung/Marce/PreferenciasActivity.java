package ubung.co.ubung.Marce;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ubung.co.ubung.PerfilActivity;
import ubung.co.ubung.PreLoginActivity;
import ubung.co.ubung.R;
import ubung.co.ubung.Semana;
import ubung.co.ubung.databinding.*;

public class PreferenciasActivity extends AppCompatActivity implements ChildEventListener {

    private DatabaseReference preferencias;

    private ActivityPreferenciasBinding binding;

    private boolean huboCambioyNoEstaGuardado;
    private int horaMayor;
    private int horaMenor;
    private int cantEstuciantesClases;
    private int cantClasesHora;

    private final static int KEY_TAG_KEY=R.id.preferenciasActivity_key_tag_key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Lo de siempre y instanciada del binding
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_preferencias);
        horaMayor=0;
        horaMenor=10000;
        huboCambioyNoEstaGuardado=false;
        //ReferenciaBaseDeDatos
        DatabaseReference dr= FirebaseDatabase.getInstance().getReference();
        preferencias = dr.child(getString(R.string.nombre_preferenciasFDB));

        preferencias.addChildEventListener(this);

        binding.preferenciasTvSemanaGenerica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             lanzarActivitySemanaGenerica();
            }
        });
        cuadrarLosTextViewsDeFormaApropiada();




    }

    private void lanzarActivitySemanaGenerica() {

        Intent intent = new Intent(this, SemanaGenericaActivity.class);
        intent.putExtra(SemanaGenericaActivity.KEY_HORAMENOR,horaMenor);
        intent.putExtra(SemanaGenericaActivity.KEY_HORAMAYOR,horaMayor);
        intent.putExtra(SemanaGenericaActivity.KEY_CANTCLASESHORA,Integer.parseInt(((TextView)binding.preferenciasEtCantidadClases.getChildAt(1)).getText().toString()));
        intent.putExtra(SemanaGenericaActivity.KEY_CANTESTUDIANTESCLASES,Integer.parseInt(((TextView)binding.preferenciasEtCantidadPersonas.getChildAt(1)).getText().toString()));
        startActivity(intent);
    }

    public void cuadrarLosTextViewsDeFormaApropiada(){
        ListenerCantidadText lct= new ListenerCantidadText(this);

        binding.preferenciasEtCantidadClases.setTag(KEY_TAG_KEY,getString(R.string.db_preferencias_cant_clases));
        binding.preferenciasEtCantidadClases.setOnClickListener(lct);




        binding.preferenciasEtCantidadPersonas.setTag(KEY_TAG_KEY,getString(R.string.db_preferencias_cantAlmnos_por_clase));
        binding.preferenciasEtCantidadPersonas.setOnClickListener(lct);



        binding.preferenciasEtCantidadHorasCancelar.setTag(KEY_TAG_KEY,getString(R.string.db_preferencias_cant_horas_cancelar));
        binding.preferenciasEtCantidadHorasCancelar.setOnClickListener(lct);



        binding.preferenciasEtCantidadHorasReservar.setTag(KEY_TAG_KEY,getString(R.string.db_preferencias_cant_horas_reserv));
        binding.preferenciasEtCantidadHorasReservar.setOnClickListener(lct);

        binding.preferenciasEtCantidadPersonasFisio.setTag(KEY_TAG_KEY,getString(R.string.db_preferencias_cantAlmnos_por_clase_fisio));
        binding.preferenciasEtCantidadPersonasFisio.setOnClickListener(lct);

        binding.preferenciasEtCantidadSemanas.setTag(KEY_TAG_KEY,getString(R.string.db_preferencias_cant_semanas));
        binding.preferenciasEtCantidadSemanas.setOnClickListener(lct);

        ListenerHoritasEditText horitas = new ListenerHoritasEditText();
        binding.preferenciasEtHoraInicialEntreSemana.setOnClickListener(horitas);
        binding.preferenciasEtHoraInicialEntreSemana.setTag(KEY_TAG_KEY,getString(R.string.db_preferencias_hora_inicio_entre_sem));
        binding.preferenciasEtHoraFinalEntreSemana.setOnClickListener(horitas);
        binding.preferenciasEtHoraFinalEntreSemana.setTag(KEY_TAG_KEY,getString(R.string.db_preferencias_hora_fin_entre_sem));
        binding.preferenciasEtHoraInicialSabado.setOnClickListener(horitas);
        binding.preferenciasEtHoraInicialSabado.setTag(KEY_TAG_KEY,getString(R.string.db_preferencias_hora_inicio_sab));
        binding.preferenciasEtHoraFinalSabado.setOnClickListener(horitas);
        binding.preferenciasEtHoraFinalSabado.setTag(KEY_TAG_KEY,getString(R.string.db_preferencias_hora_fin_sab));
    }



    @Override
    protected void onDestroy() {
        preferencias.removeEventListener(this);
        super.onDestroy();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        onChildChanged(dataSnapshot, s);

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        String key= dataSnapshot.getKey();
        Object val=  dataSnapshot.getValue();


        if(key.equals(getString(R.string.db_preferencias_cantAlmnos_por_clase))){
            ((TextView)binding.preferenciasEtCantidadPersonas.getChildAt(1)).setText(val.toString());
        }
        else if(key.equals(getString(R.string.db_preferencias_cantAlmnos_por_clase_fisio))){
            ((TextView)binding.preferenciasEtCantidadPersonasFisio.getChildAt(1)).setText(val.toString());
        }
        else if(key.equals(getString(R.string.db_preferencias_cant_horas_reserv))){
            ((TextView)binding.preferenciasEtCantidadHorasReservar.getChildAt(1)).setText(val.toString());
        }
        else if(key.equals(getString(R.string.db_preferencias_cant_horas_cancelar))){
            ((TextView)binding.preferenciasEtCantidadHorasCancelar.getChildAt(1)).setText(val.toString());
        }
        else if(key.equals(getString(R.string.db_preferencias_cant_clases))){
            ((TextView)binding.preferenciasEtCantidadClases.getChildAt(1)).setText(val.toString());

        }
        else if(key.equals(getString(R.string.db_preferencias_cant_semanas))){
            ((TextView)binding.preferenciasEtCantidadSemanas.getChildAt(1)).setText(val.toString());
        }
        else if(key.equals(getString(R.string.db_preferencias_hora_fin_entre_sem))){

            ((TextView)binding.preferenciasEtHoraFinalEntreSemana.getChildAt(1)).setText(ponerHorita(val));
        }
        else if(key.equals(getString(R.string.db_preferencias_hora_fin_sab))){
            ((TextView)binding.preferenciasEtHoraFinalSabado.getChildAt(1)).setText(ponerHorita(val));
        }
        else if(key.equals(getString(R.string.db_preferencias_hora_inicio_entre_sem))){
            ((TextView)binding.preferenciasEtHoraInicialEntreSemana.getChildAt(1)).setText(ponerHorita(val));
        }
        else if(key.equals(getString(R.string.db_preferencias_hora_inicio_sab))){
            ((TextView)binding.preferenciasEtHoraInicialSabado.getChildAt(1)).setText(ponerHorita(val));
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public String ponerHorita(Object h){
        long hora= (Long) h;
        if (hora>horaMayor) horaMayor= (int) hora;
        if (hora<horaMenor) horaMenor= (int) hora;
        String AM_PM= "a.m.";


        if(hora>=12){
            if(hora!=12) hora%=12;
            AM_PM="p.m.";
        }
        return ""+hora+":00"+AM_PM;

    }

    public class ListenerHoritasEditText implements View.OnClickListener{
        final int[] AM_PM= new int[1];

        @Override
        public void onClick(View v) {
            final LinearLayout ll=(LinearLayout) v;
//            final int numero[]= new int[1];
//            NumberPicker.OnValueChangeListener listener = new NumberPicker.OnValueChangeListener() {
//
//                @Override
//                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                    numero[0]=newVal;
//
//                }
//
//
//            };
//            NumberPicker.OnValueChangeListener listener2 = new NumberPicker.OnValueChangeListener() {
//
//                @Override
//                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                    AM_PM[0]=newVal;
//
//                }
//
//
//            };
            LinearLayout linearLayout= new LinearLayout(PreferenciasActivity.this);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            final NumberPicker picker = new NumberPicker(PreferenciasActivity.this);
            picker.setMinValue(0);
            picker.setMaxValue(1);
            picker.setDisplayedValues( new String[] { "a.m.", "p.m"} );
//            picker.setOnValueChangedListener(listener2);
            final NumberPicker nm= new NumberPicker(PreferenciasActivity.this);
            nm.setMaxValue(12);
            nm.setMinValue(1);
            nm.setWrapSelectorWheel(false);
//            nm.setOnValueChangedListener(listener);
            TextView tv= new TextView(PreferenciasActivity.this);
            tv.setGravity(Gravity.CENTER);
            tv.setText(":00");

            linearLayout.addView(nm);
            linearLayout.addView(tv);
            linearLayout.addView(picker);

            //malparido sapo
            AlertDialog.Builder b= new AlertDialog.Builder(PreferenciasActivity.this);
            b.setTitle(((TextView)ll.getChildAt(0)).getText().toString()).
                    setView(linearLayout).setPositiveButton(R.string.boton_guardar_preferencias, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {

                        int v=nm.getValue() + picker.getValue()*12;
                        if(v==12) v=0;
                        else if(v==24) v=12;
                        preferencias.child((String) ll.getTag(KEY_TAG_KEY)).setValue(v);
                    }
                    catch (Exception e) {
//                        Toast.makeText(c,"Debes introducir un numero.", Toast.LENGTH_LONG).show();
                    }
                }
            })

                    .setNegativeButton(R.string.boton_descartar_preferencias, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
//            TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        String enLaMitad=":";
//                        boolean seRedondeo= minute!=0 && minute!=30;
//                        if(minute >=30){
//                            minute= 30;
//                        }
//                        else{
//                            minute=0;
//                        }
//
//                        if(minute<10)  enLaMitad+=0;
//                        preferencias.child((String) ll.getTag(KEY_TAG_KEY)).setValue(hourOfDay+enLaMitad+minute);
//                        if(seRedondeo) Toast.makeText(PreferenciasActivity.this,"Se redondeo la hora",Toast.LENGTH_LONG).show();
//                    }
//                };
//                TimePickerDialog dialog= new TimePickerDialog(PreferenciasActivity.this,listener,12,00,false);



        }
    }
    public class ListenerCantidadText implements View.OnClickListener{

        public ListenerCantidadText(Context co){
            c=co;
        }
        Context c ;
        @Override
        public void onClick(View v) {
            LinearLayout ll=(LinearLayout) v;
            final EditText ed = new EditText(c);
            ed.setInputType(InputType.TYPE_CLASS_NUMBER);
            String titulo = ((TextView)ll.getChildAt(0)).getText().toString();
            String currentVal= ((TextView)ll.getChildAt(1)).getText().toString();
            ed.setText(currentVal);
            final String key = (String) v.getTag(KEY_TAG_KEY);
            AlertDialog.Builder b= new AlertDialog.Builder(c);
            b.setView(ed)

                    .setPositiveButton(R.string.boton_guardar_preferencias, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                int s = Integer.parseInt(ed.getText().toString());
                                preferencias.child(key).setValue(s);
                            }
                            catch (Exception e) {
//                        Toast.makeText(c,"Debes introducir un numero.", Toast.LENGTH_LONG).show();
                            }
                        }
                    })

                    .setNegativeButton(R.string.boton_descartar_preferencias, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })

                    .setTitle(titulo)

                    .show();
        }
    }


//    public class BuilderApropiado extends AlertDialog.Builder{
//
//        public BuilderApropiado(@NonNull Context context, View v) {
//            super(context);
//
//        }
//        public BuilderApropiado(@NonNull Context context) {
//            super(context);
//        }
//
//        public BuilderApropiado(@NonNull Context context, @StyleRes int themeResId) {
//            super(context, themeResId);
//        }
//    }



}
