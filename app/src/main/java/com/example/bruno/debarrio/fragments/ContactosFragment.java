package com.example.bruno.debarrio.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.bruno.debarrio.AddContactoActivity;
import com.example.bruno.debarrio.MapsActivity;
import com.example.bruno.debarrio.ObtenerContacto;
import com.example.bruno.debarrio.PedidoDeContacto;
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
    TextView textview_regresar;
    Button boton_agregar;
    ListView listView;
    String lista;
    SimpleCursorAdapter cursorAdapter;
    Cursor cursor;
    String[] from;
    int[] to;
    int telefono;
    String email, direccion, detalle;


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
        //boton flotante para agregar contacto
        //FloatingActionButton boton_float_agregar = findViewById(R.id.float_agregar;
        //boton_float_agregar.setOnClickListener(new View.OnClickListener() {
        View rootView = inflater.inflate(R.layout.fragment_contactos, container, false);
        listView = rootView.findViewById(R.id.list_contactos);

        /*
        //forma de traer los contactos?? como meterlos en la listview???
        ObtenerContacto obtener = new ObtenerContacto(email, direccion, detalle);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(obtener);*/

        //reemplazar por datos de la BD
        String[] menuItems = {"Uno", "Dos", "Tres", "Cuatro", "Cinco", "Seis", "Siete", "Ocho", "Nueve", "Diez", "Once", "Doce"}; //scrollea pero no llega al doce
        //String[] menuItems = {email, direccion, detalle}; //algo asi no funciona

        ArrayAdapter<String> listviewAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, menuItems);
        listView.setAdapter(listviewAdapter);

        /* prueba
        from = new String[]{"_id", "item"};
        to = new int[] {R.id.id, R.id.title_item};
        cursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),R.layout.fragment_contactos_list, cursor, from, to);
        listView.setAdapter(cursorAdapter);*/


        /*
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
        }*/


        boton_agregar = rootView.findViewById(R.id.boton_agregar_contacto);
        boton_agregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                llamarIntentAgregarContacto();
            }
        });
        /*
        //boton flotante para agregar contacto
        FloatingActionButton boton_float_agregar = view.findViewById(R.id.float_agregar);
        //boton_float_agregar.setOnClickListener(new View.OnClickListener() {
        //boton_agregar = (Button) view.findViewById(R.id.boton_agregar_contacto);
        boton_float_agregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View rootView) {
                llamarIntentAgregarContacto();
            }
        });*/
        return rootView;
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
