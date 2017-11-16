package ubung.co.ubung.Marce;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import ubung.co.ubung.R;

public class SemanaGenericaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semana_generica);
        ScrollView sv= (ScrollView) findViewById(R.id.semana_generica_scroll_view);
        GridView gv = new GridView(this);
        Display d= getWindowManager().getDefaultDisplay();
        Point p= new Point();
        d.getSize(p);
        int tamano=p.x;
        tamano/=6;
        int filas=10;
        gv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gv.setColumnWidth(tamano);
        gv.setNumColumns(6);
        hacerLaPrimerFila(tamano,gv);
//        for (int i =0; i<filas; i++){
//
//        }
//        View v = new View(this);
//        v.setBackgroundResource(R.drawable.caja_casilla_calendario);
//        v.setLayoutParams(new ViewGroup.LayoutParams(tamano,tamano));
//        sv.addView(v);
    }

    public void hacerLaPrimerFila(int tamanoCuadrito, ViewGroup amarrarAEsto){
        String[] diasSemana= {"",getString(R.string.lunesab),getString(R.string.martesab),
                getString(R.string.miercolesab),getString(R.string.juevesab),getString(R.string.viernesab),
                getString(R.string.sabadoab)};
        for (int i=0;i<7;i++){
            TextView v= new TextView(this);
            v.setBackgroundResource(R.drawable.caja_casilla_calendario);
            v.setLayoutParams(new ViewGroup.LayoutParams(tamanoCuadrito,tamanoCuadrito));
            v.setText(diasSemana[i]);
            amarrarAEsto.addView(v);
        }
    }
}
