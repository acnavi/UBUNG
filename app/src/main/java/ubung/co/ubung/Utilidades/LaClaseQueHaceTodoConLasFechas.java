package ubung.co.ubung.Utilidades;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by icmontesdumar on 18/09/17.
 */

public class LaClaseQueHaceTodoConLasFechas {
    public static int queEdadSiNacioEn(String fecha){

        String[] numeritos= fecha.split("/");
        int anio = Integer.parseInt(numeritos[0]);
        int mes = Integer.parseInt(numeritos[1]);
        int dia = Integer.parseInt(numeritos[2]);
        LocalDate birthdate= new LocalDate(anio,mes,dia);
        Years y = Years.yearsBetween(birthdate,new LocalDate());
        return y.getYears();
    }
}
