package com.example.administrador.gestionusuarios;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BlankFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageButton clearBtn;
    Button btnFragment;
    Activity activity;
    EditText phone;
    Spinner nombreproducto;


    public BlankFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank,container,false);
        final Producto producto= new Producto(String.valueOf(ReciboformActivity.posicioproductos),
                (getArguments()==null?"nom":getArguments().getString("nombreprod", "nom")),"des","marca","cat","ref","prec","prec",
                (getArguments()==null?"1":getArguments().getString("unidades", "1")),"min","max","pro","pa","pa","","","","","","");


        ReciboformActivity.productolist.add(producto);

        phone=(EditText)view.findViewById(R.id.opt_contedit_phone_value);
        nombreproducto=(Spinner)view.findViewById(R.id.opt_contedit_phone_type);

        phone.setText(producto.getExistenciasproducto());

        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.opt_contedit_phonetypes, android.R.layout.simple_spinner_item);*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.opt_contedit_phonetypes, android.R.layout.simple_spinner_dropdown_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nombreproducto.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(producto.getNombreproducto());
        nombreproducto.setSelection(spinnerPosition);



        // btnFragment= (Button)view.findViewById(R.id.buttonfragment);
        clearBtn=(ImageButton) view.findViewById(R.id.clearBtn);
        /*btnFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity=getActivity();
                Toast.makeText(activity,"asd", Toast.LENGTH_LONG).show();

            }
        })*/;
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                //producto.setExistenciasproducto("b"+getFragmentManager().findFragmentById(R.id.productoidrecibo_recibosl).getTag());
                activity=getActivity();
                Toast.makeText(activity,""+producto.getId(), Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(producto.getId())).commit();
              // ReciboformActivity.productolist.remove(Integer.parseInt(producto.getId()));
                producto.setPa1Producto("inactivo");


                //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.productoidrecibo_recibosl)).commit();
               // getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(((ViewGroup) getView().getParent()).getId())).commit();
                //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.productoidrecibo_recibosl).getChildFragmentManager().findFragmentById(R.id.productoidrecibo_recibosl)).commit();

                /*Fragment swipe = getChildFragmentManager().findFragmentByTag("swipe");
                if (swipe == null){
                    throw new RuntimeException("Nope");
                }

                getChildFragmentManager().beginTransaction().remove(swipe).commit();*/
               // Fragment savedFragment = (Fragment) getFragmentManager().findFragmentById(R.id.productoidrecibo_recibosl);
                //int index = getFragmentManager().beginTransaction(). .indexOf(oldFragment);







                //mListener.onFragmentInteraction("sdsd");


            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                producto.setExistenciasproducto(phone.getText().toString());
            }
        });
        nombreproducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                producto.setNombreproducto(nombreproducto.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return /*inflater.inflate(R.layout.fragment_blank, container, false);*/view;
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
