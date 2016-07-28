package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.support.v7.widget.RecyclerView;
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

    public static class PendienteViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        private TextView titulo;


        public PendienteViewHolder(View v) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.titulo);
        }


    }


    public PendienteAdapter(List<Pendiente> items) {
        this.items = items;
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
        System.out.println("items.get(i).getDescripcion() = " + items.get(i).getDescripcion());
        viewHolder.titulo.setText(items.get(i).getDescripcion());
        System.out.println("Integer.toString(items.get(i).getPrioridad()) = " + Integer.toString(items.get(i).getPrioridad()));
        //viewHolder.prioridad.setText(Integer.toString(items.get(i).getPrioridad()));
    }
}
