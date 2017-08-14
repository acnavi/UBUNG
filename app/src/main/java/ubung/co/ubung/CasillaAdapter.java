package ubung.co.ubung;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import ubung.co.ubung.Marce.SolicitudesAdapter;
import ubung.co.ubung.Utilidades.ClientesDatabaseHelper;
import ubung.co.ubung.Utilidades.DatabaseContractClientes;
import ubung.co.ubung.Utilidades.DatabaseContractMarce;
import ubung.co.ubung.Utilidades.DatabaseManager;
import ubung.co.ubung.Utilidades.MarceDatabaseHelper;
import ubung.co.ubung.Utilidades.ProfeDatabaseHelper;

/**
 * Created by icmontesdumar on 15/07/17.
 */

public class CasillaAdapter extends RecyclerView.Adapter<CasillaAdapter.CasillaViewHolder> {

    public final static int HORA_INICIAL=6;
    public final static int HORA_FINA=20;
    public final static int DIAS_SEMANA=5;
    public final static int TIPO_HORA=1098;
    public final static int TIPO_CLASE=9874;
    public final static int NUMERO_DE_COLUMNAS=DIAS_SEMANA+1;
    public final static String NOMBRE_GENERAL_BASE_DE_DATOS_CLASE="clase.db";
    public static final String GENERAL_COLUMN_FECHA="fecha";
    public static final String GENERAL_COLUMN_HORA="hora";


    private DatabaseManager.TipoAplicacion tipo;
    private Context context;
    private SQLiteDatabase database;
    public int[] semana;
    public int ano;
    public int mes;

    public CasillaAdapter(Context c, DatabaseManager.TipoAplicacion t, int[] semana, int ano, int mes){
        tipo=t;
        context=c;
        this.semana=semana;
        this.ano=ano;
        this.mes=mes;
        SQLiteOpenHelper helper;
        switch (tipo){
            case MARCE:
                helper= new MarceDatabaseHelper(context);
                break;
            case PROFE:
                helper= new ProfeDatabaseHelper(context);
                break;
            case CLIENTE:
                helper= new ClientesDatabaseHelper(context);
                break;
            default:
                throw new IllegalArgumentException();
        }

        database= helper.getWritableDatabase();
    }

    @Override
    public CasillaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FrameLayout frameLayout= new FrameLayout(context);
        int tamaño_cuadrado=parent.getWidth();
        FrameLayout.LayoutParams params= new FrameLayout.LayoutParams(tamaño_cuadrado,tamaño_cuadrado);
        frameLayout.setLayoutParams(params);
        if(viewType==TIPO_HORA){
            TextView tv= new TextView(context);
            frameLayout.addView(tv);

        }
        else {
            View v = new View(context);
            frameLayout.addView(v);
        }
        return new CasillaViewHolder(frameLayout);

    }

    @Override
    public void onBindViewHolder(CasillaViewHolder holder, int position) {

        holder.bind(position);



    }

    /**
     *
     * @param position del ViewHolder
     * @return retorna un int con la hora que esel ViewHolder en la posicion que llega por parametro.
     */
    public int getHoraAt(int position){
        int fila= position/5;
        return HORA_INICIAL+fila;
    }

    /**
     *
     * @param position del ViewHolder
     * @return un String con la fecha, en el formato adecuado. null en caso que sea un view Holder de hora
     */
    public String getfechaAt(int position){
        int dia = position%NUMERO_DE_COLUMNAS;
        if(dia==0) return null;
        dia=semana[dia-1];
        String aRet=ano+"/";
        if(mes>=10) aRet+="0";
        aRet+=mes+"/";
        if(dia>=10)aRet+="0";
        return aRet+dia;
    }
    @Override
    public int getItemCount() {
        return (HORA_FINA-HORA_INICIAL)*NUMERO_DE_COLUMNAS;
    }

    @Override
    public int getItemViewType(int position) {
        int aRet;
        if(position%NUMERO_DE_COLUMNAS==0) aRet=TIPO_HORA;
        else aRet=TIPO_CLASE;
        return aRet;
    }

    public class CasillaViewHolder extends RecyclerView.ViewHolder{


        private int horaCas;
        private String fechaCas;


        public CasillaViewHolder(View itemView) {
            super(itemView);

//            final FrameLayout mFrame = (FrameLayout) itemView;
//
//            mFrame.post(new Runnable() {
//
//                @Override
//                public void run() {
//                    RelativeLayout.LayoutParams mParams;
//                    mParams = (RelativeLayout.LayoutParams) mFrame.getLayoutParams();
//                    mParams.height = mFrame.getWidth();
//                    mFrame.setLayoutParams(mParams);
//                    mFrame.postInvalidate();
//                }
//            });
        }

        public void bind(int position) {
            ViewGroup vg = (ViewGroup) this.itemView;
            View hijo = vg.getChildAt(0);
            int hora=getHoraAt(position);
            String fecha= getfechaAt(position);
            if (hijo instanceof TextView) {
                TextView textView= (TextView) hijo;

                String horaS="";
                if(hora<10)
                    horaS+=0;
                horaS+=hora+":00";
                textView.setText(horaS);
            }

            else {
                Cursor c = database.query(NOMBRE_GENERAL_BASE_DE_DATOS_CLASE, null, GENERAL_COLUMN_FECHA + " = " + fecha + " AND "
                        + GENERAL_COLUMN_HORA + " = " + hora, null, null, null, null);
                int respuestas = c.getCount();
                if (respuestas == 0) {
                    switch (tipo) {
                        case PROFE:
                            vg.setBackgroundColor(
                                    context.getResources().getColor(R.color.clase_profesores_NO_hay_que_ir));

                            break;
                        default:
                            itemView.setBackgroundResource(R.color.clase_not_available);


                    }
                    c.close();
                } else if (respuestas > 1) {
                    c.close();
                    //TODO: Manejar Error
                } else {

                    switch (tipo) {
                        case CLIENTE:
                            c.moveToNext();
                            int i = c.getColumnIndex(DatabaseContractClientes.ClasesFuturasDB.ESTADO);
                            String estadoS = c.getString(i);
                            DatabaseContractClientes.ClasesFuturasDB.Estado estado= DatabaseContractClientes.ClasesFuturasDB.Estado.estadoConString(estadoS);
                            hijo.setBackgroundColor(
                                    context.getResources().getColor(estado.getResColor()));
                            break;
                        case PROFE:
                            vg.setBackgroundColor(
                                    context.getResources().getColor(R.color.clase_profesores_hay_que_ir));
                            break;
                        case MARCE:
                            resolvedorColorCursorMarcela(c,vg);
                            break;


                    }
                    c.close();
                }
            }
            horaCas=hora;
            fechaCas=fecha;
        }

        public void resolvedorColorCursorMarcela(Cursor cursor, ViewGroup vg){
            int ihay = cursor.getColumnIndex(DatabaseContractMarce.ClasesDB.COLUMN_LISTA_DE_ESPERA);
            try {
                String hay=cursor.getString(ihay);
                if(hay!=null) {
                    //Hay clientes esperando
                    vg.setBackgroundColor(context.getResources().getColor(R.color.clase_marce_abierta_con_problemas));
                    return;
                }
                ihay= cursor.getColumnIndex(DatabaseContractMarce.ClasesDB.COLUMN_PROFE1_CLIENTE1);
                hay=cursor.getString(ihay);
                if(hay==null){
                    //No hay clientes
                    vg.setBackgroundColor(context.getResources().getColor(R.color.clase_marce_abierta_sin_nadie));
                    return;
                }
                ihay=cursor.getColumnIndex(DatabaseContractMarce.ClasesDB.COLUMN_PROFE2);
                int ihay2=cursor.getColumnIndex(DatabaseContractMarce.ClasesDB.COLUMN_PROFE2_CLIENTE3);
                hay=cursor.getString(ihay);
                if(hay!=null){
                    hay=cursor.getString(ihay2);
                    if(hay==null){
                        //Hay un profesor sin alumnos;
                        vg.setBackgroundColor(context.getResources().getColor(R.color.clase_marce_abierta_con_problemas));
                        return;
                    }
                }
                //all esta en orden.com
                vg.setBackgroundColor(context.getResources().getColor(R.color.clase_marce_increible));

            }
            catch (Exception e){
                //TODO:Manejar Error
        }
    }

    }
}
