package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import seguridad_redes.uach.mx.proyectoseguridadredes.models.Pendiente;


/**
 * Created by SONU on 22/03/16.
 */
public class Toolbar_ActionMode_Callback implements ActionMode.Callback {

    private Context context;
    private Pendiente_Adapter recyclerView_adapter;
    private ArrayList<Pendiente> message_models;


    public Toolbar_ActionMode_Callback(Context context, Pendiente_Adapter recyclerView_adapter, ArrayList<Pendiente> message_models) {
        this.context = context;
        this.recyclerView_adapter = recyclerView_adapter;
        this.message_models = message_models;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.context, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            menu.findItem(R.id.item_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.item_check).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Fragment recyclerFragment;
        switch (item.getItemId()) {
            case R.id.item_delete:

                //Check if current action mode is from ListView Fragment or RecyclerView Fragment
                    //If current fragment is recycler view fragment
                    recyclerFragment = new TabsActivity().getFragment(0);//Get recycler view fragment
                System.out.println("AQUIII recyclerFragment = " + recyclerFragment);
                    if (recyclerFragment != null)
                        //If recycler fragment not null
                        ((TareasPendientes) recyclerFragment).deleteRows();//delete selected rows
                break;
            case R.id.item_check:
                Toast.makeText(context, "Tarea realizada", Toast.LENGTH_SHORT).show();//Show toast
                recyclerFragment = new TabsActivity().getFragment(0);//Get recycler view fragment
                System.out.println("AQUIII recyclerFragment = " + recyclerFragment);
                if (recyclerFragment != null)
                    //If recycler fragment not null
                    ((TareasPendientes) recyclerFragment).updateRows();
                mode.finish();//Finish action mode
                break;
        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {

        //When action mode destroyed remove selected selections and set action mode to null
        //First check current fragment action mode
            recyclerView_adapter.removeSelection();  // remove selection
            Fragment recyclerFragment = new TabsActivity().getFragment(0);//Get recycler fragment
            if (recyclerFragment != null)
                ((TareasPendientes) recyclerFragment).setNullToActionMode();//Set action mode null

    }
}
