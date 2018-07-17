package com.example.bruno.debarrio.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bruno.debarrio.MainActivity;
import com.example.bruno.debarrio.MainActivity2;
import com.example.bruno.debarrio.MapActivity;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Reclamo;
import com.example.bruno.debarrio.entidades.Respuesta;
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
    Button botonPorEstado, botonBuscar, botonPorCategoria;
    TextView textAbierto, textEncurso, textResuelto, textReabierto;
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

        botonBuscar = rootView.findViewById(R.id.boton_buscar);
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner_estado);
        String[] tipos1 = {"Abierto","En curso", "Resuelto","Re-abierto"};
        //spinner.setAdapter(new ArrayAdapter<String>(this, (inflater.inflate(R.layout.fragment_detalle_reclamos, container))), tipos));
        spinner.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, tipos1));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                String posicion = (String) adapterView.getItemAtPosition(pos);
                //guardo los datos del estado
                enviarEstado(posicion);
                //adapterView.getItemIdAtPosition(3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });

        /*
        botonPorEstado = rootView.findViewById(R.id.boton_por_estado);
        botonPorEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llenarlistaEstado();
            }
        });*/

        /*botonPorCategoria = rootView.findViewById(R.id.boton_por_categoria);
        botonPorCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarIntentPorCategoria();
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

    private void enviarEstado(final String posicion) {
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(adapterView.getContext(),(String) adapterView.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                SharedPreferences prefEstado = getContext().getSharedPreferences("estado", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefEstado.edit();
                editor.putString("estadoNombre", posicion); //GUARDA EL ESTADO PARA USARLO EN EL MAIN 2
                editor.commit();
                llamarIntentPorEstado();
            }
        });
    }

    //SPINNER NUEVO
    private void llamarIntentPorEstado() {
        Intent intentVer = new Intent(getActivity(), MainActivity2.class);
        getActivity().startActivity(intentVer);
    }

    //SPINNER VIEJO
    private void llenarlistaEstado() {
        Intent intentVer = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intentVer);
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

    @Override
    public void enviarRespuesta(Respuesta respuesta) {
        detalleReclamoFragment = new DetalleReclamoFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("objeto2", respuesta);
        detalleReclamoFragment.setArguments(bundleEnvio);
        //cargar el fragment en el activity
        //getSupportFragmentManager().beginTransaction().replace(R.id.container, detalleReclamoFragment).addToBackStack(null).commit();
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


    private void llamarIntentPorCategoria() {
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
