package ubung.co.ubung;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by icmontesdumar on 15/07/17.
 */

public class SemanaFragmentAdapter extends FragmentPagerAdapter {

//    private int hoy;
    private int diasDelMes;
    private int cantidadSemanas;
    private Calendar calendar;

    public SemanaFragmentAdapter(FragmentManager fm, int cantidadSemanas){

        super(fm);
//        hoy=diaHoy;
//        this.diasDelMes=diasDelMes;
        this.cantidadSemanas=cantidadSemanas;
        calendar=Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
    }

    @Override
    public Fragment getItem(int position) {
            int lunes;
            int aRestar = calendar.get(Calendar.DAY_OF_WEEK)-2;
            if (aRestar<0) lunes=calendar.get(Calendar.DAY_OF_MONTH)+1;
            else lunes = calendar.get(Calendar.DAY_OF_MONTH)-aRestar;
            if(lunes<=0&&position==0){
                int mesPasado=(calendar.get(Calendar.MONTH)-1)%12;
                Calendar temp = Calendar.getInstance();
                temp.set(Calendar.MONTH,mesPasado);
                int diasMesPasado=temp.getMaximum(Calendar.DAY_OF_MONTH);
                lunes=diasMesPasado+lunes;
                return Semana.newInstance(lunes,diasMesPasado);

            }
            lunes+=(position+1)*7;
            int diasDelMes = calendar.getMaximum(Calendar.DAY_OF_MONTH);
            if(lunes<=diasDelMes)return Semana.newInstance(lunes,diasDelMes);
            lunes-=diasDelMes;
            int mesSig=(calendar.get(Calendar.MONTH)+1)%12;
            Calendar temp = Calendar.getInstance();
            temp.set(Calendar.MONTH,mesSig);
            int diasMesSid=temp.getMaximum(Calendar.DAY_OF_MONTH);
            lunes%=diasMesSid;
            return Semana.newInstance(lunes,diasMesSid);

    }

    @Override
    public int getCount() {
        return cantidadSemanas;
    }
}
