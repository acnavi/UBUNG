package ubung.co.ubung;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by icmontesdumar on 15/07/17.
 */

public class SemanaFragmentAdapter extends FragmentPagerAdapter {

    private int cantidadSemanas;
    private LocalDate hoy;


    public SemanaFragmentAdapter(FragmentManager fm, int cantidadSemanas){

        super(fm);
        this.cantidadSemanas=cantidadSemanas;
        hoy = new LocalDate();

    }

    @Override
    public Fragment getItem(int position) {
        LocalDate hoytemp= hoy.plusDays(position*7);
        LocalDate lunes= hoytemp.withDayOfWeek(DateTimeConstants.MONDAY);
        return Semana.newInstance(lunes.getDayOfMonth(),lunes.dayOfMonth().getMaximumValue());


    }

    @Override
    public int getCount() {
        return cantidadSemanas;
    }
}
