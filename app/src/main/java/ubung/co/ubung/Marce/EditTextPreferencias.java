package ubung.co.ubung.Marce;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import com.google.firebase.database.DatabaseReference;


/**
 * Created by icmontesdumar on 8/11/17.
 */

public class EditTextPreferencias extends android.support.v7.widget.AppCompatEditText {
    private String keyDataBase;



    public EditTextPreferencias(Context context){
        super(context);


    }
//    public EditTextPreferencias (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
//        super(context,attrs,defStyleAttr);
//    }
    public EditTextPreferencias (Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);

    }
    public EditTextPreferencias (Context context, AttributeSet attrs){
        super(context,attrs);

    }

    public void setKeyDataBase(int keyDataBase){



        this.keyDataBase = getContext().getString(keyDataBase);

    }
}







