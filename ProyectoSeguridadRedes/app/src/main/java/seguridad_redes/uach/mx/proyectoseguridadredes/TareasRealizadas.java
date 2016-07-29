package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import seguridad_redes.uach.mx.proyectoseguridadredes.models.Pendiente;


public class TareasRealizadas extends Fragment {

    private static View view;
    private static RecyclerView recyclerView;
    private static ArrayList<Pendiente> item_models;
    private static Realizada_Adapter adapter;

    public TareasRealizadas() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tareas_realizadas, container, false);
        populateRecyclerView();
        return view;
    }

    //Populate ListView with dummy data
    private void populateRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recicladorRealizadas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        item_models = new ArrayList<>();

        adapter = new Realizada_Adapter(getActivity(), item_models);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public static void setRecyclerView(RecyclerView recyclerView) {
        TareasRealizadas.recyclerView = recyclerView;
    }

    public static Realizada_Adapter getAdapter() {
        return adapter;
    }

    public static void setAdapter(Realizada_Adapter adapter) {
        TareasRealizadas.adapter = adapter;
    }
}
