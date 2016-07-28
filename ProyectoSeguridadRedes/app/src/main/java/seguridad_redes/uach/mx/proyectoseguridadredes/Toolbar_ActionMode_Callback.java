package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.util.SparseBooleanArray;
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
    private PendienteAdapter recyclerView_adapter;
    private ArrayList<Pendiente> message_models;
    private boolean isListViewFragment;


    public Toolbar_ActionMode_Callback(Context context, PendienteAdapter recyclerView_adapter, ArrayList<Pendiente> message_models) {
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

        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu according to SDK Levels
        /*if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_delete), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_copy), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_forward), MenuItemCompat.SHOW_AS_ACTION_NEVER);
        } else {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_forward).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }*/

        menu.findItem(R.id.item_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.item_check).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                    Fragment recyclerFragment = new TabsActivity.PlaceholderFragment().getFragment(1);//Get recycler view fragment
                    if (recyclerFragment != null)
                        //If recycler fragment not null
                        ((RecyclerView_Fragment) recyclerFragment).deleteRows();//delete selected rows

                break;
            case R.id.item_check:

                //Get selected ids on basis of current fragment action mode
                SparseBooleanArray selected;

                    selected = PendienteAdapter
                            .getSelectedIds();

                int selectedMessageSize = selected.size();

                //Loop to all selected items
                for (int i = (selectedMessageSize - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        //get selected data in Model
                        Item_Model model = message_models.get(selected.keyAt(i));
                        String title = model.getTitle();
                        String subTitle = model.getSubTitle();
                        //Print the data to show if its working properly or not
                        Log.e("Selected Items", "Title - " + title + "\n" + "Sub Title - " + subTitle);

                    }
                }
                Toast.makeText(context, "You selected Copy menu.", Toast.LENGTH_SHORT).show();//Show toast
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
            Fragment recyclerFragment = new MainActivity().getFragment(1);//Get recycler fragment
            if (recyclerFragment != null)
                ((RecyclerView_Fragment) recyclerFragment).setNullToActionMode();//Set action mode null

    }
}
