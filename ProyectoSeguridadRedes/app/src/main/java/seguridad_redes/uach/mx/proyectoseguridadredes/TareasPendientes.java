package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.ArrayList;

import seguridad_redes.uach.mx.proyectoseguridadredes.models.Pendiente;


public class TareasPendientes extends Fragment {
    private static View view;
    private static RecyclerView recyclerView;
    private static ArrayList<Pendiente> item_models;
    private static Pendiente_Adapter adapter;
    private ActionMode mActionMode;
    Socket socket;

    public TareasPendientes() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tareas_pendientes, container, false);
        populateRecyclerView();
        implementRecyclerViewClickListeners();
        return view;
    }

    //Populate ListView with dummy data
    private void populateRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recicladorPendientes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        item_models = new ArrayList<>();

        adapter = new Pendiente_Adapter(getActivity(), item_models);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //Implement item click and long click over recycler view
    private void implementRecyclerViewClickListeners() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerClick_Listener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                if (mActionMode != null)
                onListItemSelect(position);
                view.setSelected(true);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                onListItemSelect(position);
                view.setSelected(true);
            }
        }));
    }

    //List item select method
    private void onListItemSelect(int position) {
        adapter.toggleSelection(position);//Toggle the selection



        boolean hasCheckedItems = adapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null) {
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(), adapter, item_models));
        }else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null) {
            //set action mode title on item selection

            mActionMode.setTitle(String.valueOf(adapter
                    .getSelectedCount()) + " Seleccionadas");
            System.out.println("selectedID=============== = " + adapter.getSelectedIds());

        }


    }
    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    //Delete selected rows
    public void deleteRows() {
        try {
            socket = IO.socket("https://task-master-seguridad.herokuapp.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.connect();
        SparseBooleanArray selected = adapter
                .getSelectedIds();//Get selected ids
        for( String id : adapter.idItemSeleccionado){
            socket.emit("deletePendiente", id);
        }
        adapter.idItemSeleccionado = new ArrayList<>();
        mActionMode.finish();//Finish action mode after use
    }

    public void updateRows() {
        try {
            socket = IO.socket("https://task-master-seguridad.herokuapp.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.connect();
        SparseBooleanArray selected = adapter
                .getSelectedIds();//Get selected ids
        for( String id : adapter.idItemSeleccionado){
            socket.emit("terminarPendiente", id);
        }
        adapter.idItemSeleccionado = new ArrayList<>();
        mActionMode.finish();//Finish action mode after use

    }

    public static Pendiente_Adapter getAdapter() {
        return adapter;
    }

    public static void setAdapter(Pendiente_Adapter adapter) {
        TareasPendientes.adapter = adapter;
    }

    public static RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public static void setRecyclerView(RecyclerView recyclerView) {
        TareasPendientes.recyclerView = recyclerView;
    }
}
