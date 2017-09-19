package ubung.co.ubung.Utilidades;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by icmontesdumar on 18/09/17.
 */

public class LaClaseQueHaceTodoConLasFechas {
    public static int queEdadSiNacioEn(String fecha){
        Calendar calendar= Calendar.getInstance();
        long mil=calendar.getTimeInMillis();
        String[] numeritos= fecha.split("/");
        int anio = Integer.parseInt(numeritos[0]);
        int mes = Integer.parseInt(numeritos[1]);
        int dia = Integer.parseInt(numeritos[2]);
        Calendar birthday = Calendar.getInstance();
        birthday.set(anio,mes-1,dia);
        long birthinmill=birthday.getTimeInMillis();
        long yearsinmil= mil-birthinmill;
        int anios = (int) yearsinmil/(1000*60*60*24*365);
        return anios;
    }
}
