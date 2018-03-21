package com.example.bruno.debarrio.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bruno.debarrio.AddContactoActivity;
import com.example.bruno.debarrio.MapsActivity;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.fragments.dummy.DummyContent;
import com.example.bruno.debarrio.fragments.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ContactosFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    Button boton_agregar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactosFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ContactosFragment newInstance(int columnCount) {
        ContactosFragment fragment = new ContactosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        /*
        View rootView = inflater.inflate(R.layout.fragment_contactos, container, false);

        //boton flotante para agregar contacto
        //FloatingActionButton boton_float_agregar = findViewById(R.id.float_agregar;
        //boton_float_agregar.setOnClickListener(new View.OnClickListener() {
        boton_agregar = (Button) rootView.findViewById(R.id.boton_ubicacion);
        boton_agregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                llamarIntentAgregarContacto();
            }
        });
        return rootView;*/

    }

    private void llamarIntentAgregarContacto() { //pasa a un activity o fragment map para obtener un marcador
        /*
        Intent intentMaps = new Intent(SubirFragment.this, MapsActivity.class);
        SubirFragment.this.startActivity(intentMaps);*/
        Intent intentAgregar = new Intent(getActivity(), AddContactoActivity.class);
        getActivity().startActivity(intentAgregar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactos_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyContactosRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }

        View rootView = inflater.inflate(R.layout.fragment_contactos, container, false);
        //boton flotante para agregar contacto
        //FloatingActionButton boton_float_agregar = findViewById(R.id.float_agregar;
        //boton_float_agregar.setOnClickListener(new View.OnClickListener() {
        boton_agregar = rootView.findViewById(R.id.boton_agregar_contacto);
        boton_agregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                llamarIntentAgregarContacto();
            }
        });
        return rootView;
        //return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    public interface OnFragmentInteractionListener {
    }
}
