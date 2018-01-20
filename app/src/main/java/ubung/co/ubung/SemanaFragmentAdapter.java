package ubung.co.ubung;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ShareCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ubung.co.ubung.Utilidades.DatabaseManager;

/**
 * Created by icmontesdumar on 15/07/17.
 */

public class SemanaFragmentAdapter extends FragmentStatePagerAdapter implements EventListener<QuerySnapshot>{

    private int cantidadSemanas;
    private LocalDate hoy;
    private int tamanoPantalla;
    private Activity context;
    private String tippoApp;
    private HashMap<Integer,Semana> fragments;

    private class SemanaPAraArray{
        LocalDate lunes;
        String semana;
        SemanaPAraArray(LocalDate lunes, String semana){
            this.lunes=lunes;

            this.semana=semana;
        }
    }
    private ArrayList<SemanaPAraArray> data;


    public SemanaFragmentAdapter(Activity c, FragmentManager fm, int tamanoPantalla, DatabaseManager.TipoAplicacion t){

        super(fm);
        this.tamanoPantalla=tamanoPantalla;
        context=c;
        tippoApp = t.getTipoString();
        fragments= new HashMap<>();
        CollectionReference col = FirebaseFirestore.getInstance().collection(c.getString(R.string.db_firestore_semanasActivasCollection));
        col.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                llenadorData(task.getResult());
            }
        });
//        col.addSnapshotListener(context,this);

    }

    @Override
    public Fragment getItem(int position) {

        SemanaPAraArray s= data.get(position);
        Semana ss=Semana.newInstance(s.lunes.getDayOfMonth(),s.lunes.dayOfMonth().getMaximumValue(),tamanoPantalla,s.semana,tippoApp);
        fragments.put(position,ss);
        return ss;


    }

    @Override
    public int getCount() {
        if(data!=null)
        return data.size();
        return 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
        if(!documentSnapshots.getMetadata().hasPendingWrites())
        llenadorData(documentSnapshots);
    }
    public void llenadorData(QuerySnapshot documentSnapshots){
        ArrayList<SemanaPAraArray> temp= new ArrayList<>();
        for (DocumentSnapshot d: documentSnapshots.getDocuments()) {
            String s=d.getReference().getId();
            String[] ss=s.split("De");
            LocalDate l= new LocalDate(Integer.parseInt(ss[3]),Integer.parseInt(ss[2]),Integer.parseInt(ss[1]));
            SemanaPAraArray sem= new SemanaPAraArray(l,s+"/dias");
            temp.add(sem);
        }
        data=temp;

        notifyDataSetChanged();
    }
}
