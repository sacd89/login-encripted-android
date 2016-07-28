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

public class RealizadaAdapter extends RecyclerView.Adapter<RealizadaAdapter.RealizadaViewHolder> {
    private List<Pendiente> items;

    public static class RealizadaViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView titulo;
        public TextView fecha;

        public RealizadaViewHolder(View v) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.titulo);
            fecha = (TextView) v.findViewById(R.id.fecha);
        }
    }

    public RealizadaAdapter(List<Pendiente> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RealizadaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.realizadas_card, viewGroup, false);
        return new RealizadaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RealizadaViewHolder viewHolder, int i) {
        viewHolder.titulo.setText(items.get(i).getDescripcion());
        viewHolder.fecha.setText(items.get(i).getFecha());
    }
}
