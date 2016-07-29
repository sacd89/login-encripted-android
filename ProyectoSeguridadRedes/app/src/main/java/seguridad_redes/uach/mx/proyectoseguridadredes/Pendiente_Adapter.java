package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import seguridad_redes.uach.mx.proyectoseguridadredes.models.Pendiente;


/**
 * Created by SONU on 27/03/16.
 */
public class Pendiente_Adapter extends
        RecyclerView.Adapter<DemoViewHolder> {
    private ArrayList<Pendiente> arrayList;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;
    private static int selectedPos = 0;
    public List<String> idItemSeleccionado;


    public Pendiente_Adapter(Context context,
                             ArrayList<Pendiente> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        mSelectedItemsIds = new SparseBooleanArray();

    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(DemoViewHolder holder,
                                 int position) {

        if(arrayList.get(position).getPrioridad().equals(1)){
            holder.cvPendientes.setBackgroundColor(Color.parseColor("#d32f2f"));
        }else if(arrayList.get(position).getPrioridad().equals(2)){
            holder.cvPendientes.setBackgroundColor(Color.parseColor("#ffb74d"));
        }else{
            holder.cvPendientes.setBackgroundColor(Color.parseColor("#fff176"));
        }
        //Setting text over text view
        holder.titulo.setText(arrayList.get(position).getDescripcion());

        holder.itemView
                .setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
                        : Color.TRANSPARENT);


    }

    @Override
    public DemoViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.pendientes_card, viewGroup, false);
        return new DemoViewHolder(mainGroup);

    }


    /***
     * Methods required for do selections, remove selections, etc.
     */

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
        //Igualacion de id del item seleccionado
        idItemSeleccionado.add(arrayList.get(position).get_id());
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