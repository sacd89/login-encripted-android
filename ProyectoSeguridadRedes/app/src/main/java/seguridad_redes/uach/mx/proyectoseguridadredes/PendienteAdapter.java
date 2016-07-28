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
    private List<Pendiente> items;
    private int count;

    public static class PendienteViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView titulo;
        public TextView prioridad;

        public PendienteViewHolder(View v) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.titulo);
            prioridad = (TextView) v.findViewById(R.id.prioridad);
        }
    }

    public PendienteAdapter(List<Pendiente> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
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
        viewHolder.titulo.setText(items.get(i).getDescripcion());
        viewHolder.prioridad.setText(Integer.toString(items.get(i).getPrioridad()));
    }
}
