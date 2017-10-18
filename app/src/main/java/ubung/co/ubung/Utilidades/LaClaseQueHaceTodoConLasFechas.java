package ubung.co.ubung.Utilidades;

import android.content.Context;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import ubung.co.ubung.R;

/**
 * Created by icmontesdumar on 18/09/17.
 */

public class LaClaseQueHaceTodoConLasFechas {
    public enum DiasDeLaSemana{
        LUNES(1, R.string.lunes, R.string.lunesab),
        MARTES(2,R.string.martes, R.string.martesab),
        MIERCOLES(3,R.string.miercoles, R.string.miercolesab),
        JUEVES(4,R.string.jueves, R.string.juevesab),
        VIERNES(5,R.string.viernes, R.string.viernesab),
        SABADO(6,R.string.sabado, R.string.sabado),
        DOMINGO(0,R.string.lunes, R.string.lunesab);

        int i;
        int dia;
        int ab;


        DiasDeLaSemana(int i, int dia, int ab){
            this.i=i;
            this.ab=ab;
            this.dia=dia;
        }
        public String getString(Context c){
            return c.getString(dia);
        }
        public static DiasDeLaSemana getDia(int i){
            i%=7;
            for (DiasDeLaSemana d: values()
                 ) {
                if(d.i==i){
                    return d;
                }
            }
            return null;
        }
        public String getAbreviacion(Context c){
            return c.getString(ab);
        }
    }
    public static int queEdadSiNacioEn(String fecha){

        String[] numeritos= fecha.split("/");
        int anio = Integer.parseInt(numeritos[0]);
        int mes = Integer.parseInt(numeritos[1]);
        int dia = Integer.parseInt(numeritos[2]);
        LocalDate birthdate= new LocalDate(anio,mes,dia);
        Years y = Years.yearsBetween(birthdate,new LocalDate());
        return y.getYears();
    }

    public static String seAcabaDeCrearUnPaqueteDemeLaFechaDeVencimiento(){
        LocalDate ld= new LocalDate();
        ld=ld.plusMonths(1);
        return ld.getYear()+"/"+ld.getMonthOfYear()+"/"+ld.getDayOfMonth();
    }
    public static String getHoyDiaDeLaSemana(Context c){
        LocalDate ld= new LocalDate();
        int i=ld.getDayOfWeek();

        DiasDeLaSemana d = DiasDeLaSemana.getDia(i);
        return d.getString(c);
    }
    public static String getDiaDeLaSemana(int ano,int mes, int diaDelMes,Context c){
        LocalDate ld= new LocalDate(ano,mes,diaDelMes);
        int i=ld.getDayOfWeek();

        DiasDeLaSemana d = DiasDeLaSemana.getDia(i);
        return d.getString(c);
    }
    public static String getHoyMes(){
        SimpleDateFormat sp= new SimpleDateFormat("MMMM", new Locale("es","ES"));
        return sp.format(new Date());

    }
    public static String getMes(int mes){
        SimpleDateFormat sp= new SimpleDateFormat("MMMM", new Locale("es","ES"));
        return sp.format(new Date(1997,mes,1));

    }

}
