package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by SONU on 27/03/16.
 */
public class DemoViewHolder extends RecyclerView.ViewHolder {


    public TextView titulo;
    public TextView cvPendientes;
    public TextView fecha;
    public CardView colorCvPendientes;

    public DemoViewHolder(View view) {
        super(view);


        this.titulo = (TextView) view.findViewById(R.id.titulo);
        this.cvPendientes = (TextView) view.findViewById(R.id.color);
        this.fecha = (TextView) view.findViewById(R.id.fecha);
        this.colorCvPendientes = (CardView) view.findViewById(R.id.cvPendientes);

    }
}