package com.example.bruno.debarrio.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bruno.debarrio.MainActivity;
import com.example.bruno.debarrio.MapActivity;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Reclamo;
import com.example.bruno.debarrio.fragments.dummy.DummyContent.DummyItem;
import com.example.bruno.debarrio.interfaces.ComunicacionFragments;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ReclamosFragment extends Fragment implements ComunicacionFragments {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    Button botonVerTodos, botonVerMios, botonUbicacion;
    ListaReclamosFragment listaReclamosFragment;
    DetalleReclamoFragment detalleReclamoFragment;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReclamosFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ReclamosFragment newInstance(int columnCount) {
        ReclamosFragment fragment = new ReclamosFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reclamos, container, false);

        botonVerTodos = rootView.findViewById(R.id.boton_ver_todos);
        botonVerTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarIntentVerTodos();
            }
        });

        /*
        botonVerMios = rootView.findViewById(R.id.boton_ver_mios);
        botonVerMios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarIntentVerMios();
            }
        });

        botonUbicacion = rootView.findViewById(R.id.boton_ubicacion_reclamo);
        botonUbicacion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                llamarIntentMapa();
            }

        });*/


        /*
        //View view = inflater.inflate(R.layout.fragment_eventos_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyEventosRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        return view;*/

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

    @Override
    public void enviarReclamo(Reclamo reclamo) {
        detalleReclamoFragment = new DetalleReclamoFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("objeto", reclamo);
        detalleReclamoFragment.setArguments(bundleEnvio);
        //getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, detalleReclamoFragment).addToBackStack(null).commit();
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

    private void llamarIntentVerTodos() {
        Intent intentVer = new Intent(getActivity(), MainActivity.class); //  EventosTodosActivity
        getActivity().startActivity(intentVer);
    }

    /*
    private void llamarIntentVerMios() {
        //Intent intentVer = new Intent(getActivity(), EventosMiosActivity.class);
        Intent intentVer = new Intent(getActivity(), ActivityUser.class);
        getActivity().startActivity(intentVer);
    }*/

    private void llamarIntentMapa() { //pasa a un activity o fragment map
        Intent intentMap = new Intent(getActivity(), MapActivity.class);
        getActivity().startActivity(intentMap);

        /* si no funciona lo anterior...
        private final Context context;
        context = itemView.getContext();
        Intent detail = new Intent(context.getApplicationContext(), ImageDetail.class);
        detail.putExtra("id", imagen.getId());
        context.startActivity(detail);*/
    }
}
