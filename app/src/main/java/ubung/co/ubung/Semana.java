package ubung.co.ubung;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import ubung.co.ubung.databinding.FragmentSemanaBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Semana.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Semana#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Semana extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LUNES = "lunes";
    private static final String ARG_DIAS_DEL_MES= "ddm";


    // TODO: Rename and change types of parameters
    private int lunes;
    private int[] todosLosNumeros;
    private int diasDelMes;

    private FragmentSemanaBinding binding;



    private OnFragmentInteractionListener mListener;

    public Semana() {
        // Required empty public constructor


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lun dia del mes que es lunes,. si es
     * @param dias cantidad de dias en el mes de esta semana.

     * @return A new instance of fragment Semana.
     */
    // TODO: Rename and change types and number of parameters
    public static Semana newInstance(int lun, int dias) {
        Semana fragment = new Semana();

        Bundle args = new Bundle();

        args.putInt(ARG_LUNES, lun);
        args.putInt(ARG_DIAS_DEL_MES, dias);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            todosLosNumeros= new int[6];
            lunes = getArguments().getInt(ARG_LUNES);
            diasDelMes=getArguments().getInt(ARG_DIAS_DEL_MES);
            for(int i=0; i<6;i++){
                int j = lunes+i;
                if(j>diasDelMes) j%=diasDelMes;
                todosLosNumeros[i]=j;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_semana, container, false);
        binding.semanaTvDiaLun.setText(""+todosLosNumeros[0]);
        binding.semanaTvDiaMar.setText(""+todosLosNumeros[1]);
        binding.semanaTvDiaMier.setText(""+todosLosNumeros[2]);
        binding.semanaTvDiaJue.setText(""+todosLosNumeros[3]);
        binding.semanaTvDiaVie.setText(""+todosLosNumeros[4]);
        binding.semanaTvDiaSab.setText(""+todosLosNumeros[5]);
        View v=binding.getRoot();
        return  v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
