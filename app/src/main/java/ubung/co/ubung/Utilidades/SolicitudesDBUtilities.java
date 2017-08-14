package ubung.co.ubung.Utilidades;

import android.content.Context;

import ubung.co.ubung.R;

/**
 * Created by icmontesdumar on 7/07/17.
 */
public class SolicitudesDBUtilities {

    private static final int[] resourses = new int[]{
            R.string.db_correo,
            R.string.db_cumpleanos,
            R.string.db_genero,
            R.string.db_nombre,
            R.string.db_tel

    };


    private static final String[] strings = new String[resourses.length];



    public static String[] getStrings(Context context) {
        if(strings[0]==null){
        for (int i = 0; i < strings.length; i++) {
            strings[i] = context.getString(resourses[i]);

        }}
        return strings;
    }


}