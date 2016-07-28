package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import seguridad_redes.uach.mx.proyectoseguridadredes.models.Pendiente;


/**
 * Created by SONU on 27/03/16.
 */
public class Realizada_Adapter extends
        RecyclerView.Adapter<DemoViewHolder> {
    private ArrayList<Pendiente> arrayList;
    private Context context;


    public Realizada_Adapter(Context context,
                             ArrayList<Pendiente> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(DemoViewHolder holder,
                                 int position) {

        holder.cvPendientes.setBackgroundColor(Color.parseColor("#CDDC39"));
        //Setting text over text view
        holder.titulo.setText(arrayList.get(position).getDescripcion());
        holder.fecha.setText(arrayList.get(position).getFecha());


    }

    @Override
    public DemoViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.realizadas_card, viewGroup, false);
        return new DemoViewHolder(mainGroup);

    }

}