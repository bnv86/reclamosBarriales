package com.example.bruno.debarrio.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bruno.debarrio.EmailsActivity;
import com.example.bruno.debarrio.EventosMiosActivity;
import com.example.bruno.debarrio.EventosTodosActivity;
import com.example.bruno.debarrio.MainActivity;
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
public class EventosFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    Button botonVerTodos;
    Button botonVerMios;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventosFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventosFragment newInstance(int columnCount) {
        EventosFragment fragment = new EventosFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_eventos, container, false);

        botonVerTodos = rootView.findViewById(R.id.boton_ver_todos);
        botonVerTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarIntentVerTodos();
            }
        });

        botonVerMios = rootView.findViewById(R.id.boton_ver_mios);
        botonVerMios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarIntentVerMios();
            }
        });
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

    private void llamarIntentVerMios() {
        Intent intentVer = new Intent(getActivity(), EventosMiosActivity.class);
        getActivity().startActivity(intentVer);
    }
}
