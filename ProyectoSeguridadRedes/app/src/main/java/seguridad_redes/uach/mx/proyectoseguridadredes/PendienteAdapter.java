package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import seguridad_redes.uach.mx.proyectoseguridadredes.models.Pendiente;

/**
 * Created by dani on 26/07/16.
 */

public class PendienteAdapter extends RecyclerView.Adapter<PendienteAdapter.PendienteViewHolder> {

    private final List<Pendiente> items;
    private SparseBooleanArray mSelectedItemsIds;


    public static class PendienteViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView titulo;
        public TextView prioridad;
        public TextView cvPendientes;


        public PendienteViewHolder(View v) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.titulo);
            cvPendientes = (TextView) v.findViewById(R.id.color);
        }


    }


    public PendienteAdapter(List<Pendiente> items) {
        this.items = items;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public int getItemCount() {
        System.out.println("items.size() = " + items.size());
        return items.size();
    }

    @Override
    public PendienteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pendientes_card, viewGroup, false);
        return new PendienteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PendienteViewHolder viewHolder, int i) {
        System.out.println("items.get(i).getPrioridad().getClass() = " + items.get(i).getPrioridad().getClass());
        if(items.get(i).getPrioridad().equals(1)){
            viewHolder.cvPendientes.setBackgroundColor(Color.parseColor("#d32f2f"));
        }else if(items.get(i).getPrioridad().equals(2)){
            viewHolder.cvPendientes.setBackgroundColor(Color.parseColor("#ffb74d"));
        }else{
            viewHolder.cvPendientes.setBackgroundColor(Color.parseColor("#fff176"));
        }
        System.out.println("items.get(i).getDescripcion() = " + items.get(i).getDescripcion());
        viewHolder.titulo.setText(items.get(i).getDescripcion());
        System.out.println("Integer.toString(items.get(i).getPrioridad()) = " + Integer.toString(items.get(i).getPrioridad()));
        //viewHolder.prioridad.setText(Integer.toString(items.get(i).getPrioridad()));
    }

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
