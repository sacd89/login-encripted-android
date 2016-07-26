package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluebite.android.eddystone.Global;
import com.bluebite.android.eddystone.Scanner;
import com.bluebite.android.eddystone.ScannerDelegate;
import com.bluebite.android.eddystone.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seguridad_redes.uach.mx.proyectoseguridadredes.models.Pendiente;
import seguridad_redes.uach.mx.proyectoseguridadredes.utils.ReadJson;

public class TabsActivity extends AppCompatActivity implements ScannerDelegate {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static String usuario;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private int REQUEST_ENABLE_BT = 1;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private static List<Pendiente> items;
    private BeaconAdapter mBeaconAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Url> mUrls = new ArrayList<>();
    private ListView lstVwUsuarios;
    public static String URL_PENDIENTE;
    List<Pendiente> pendientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        Global.logging = true;
        Global.expireTimer = 30000;
        Scanner.start(this);
        final int[] ICONS = new int[]{
                R.drawable.ic_assignment_white_24dp,
                R.drawable.ic_assignment_turned_in_white_24dp
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        //tabLayout.getTabAt(2).setIcon(ICONS[2]);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Agregar Tarea Nueva", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        usuario = bundle.getString("nombre") + " " +
                bundle.getString("paterno")+ " " + bundle.getString("materno");
        System.out.println("usuario = " + usuario);
        getSupportActionBar().setTitle(usuario);

        items = getPendientes();
        if(items == null){
            Toast.makeText(this,"No tienes tareas pendientes", Toast.LENGTH_LONG);
        }else {
            for (Pendiente item : items) {
                items.add(new Pendiente());
            }
            // Obtener el Recycler
            recycler = (RecyclerView) findViewById(R.id.reciclador);
            recycler.setHasFixedSize(true);

            // Usar un administrador para LinearLayout
            lManager = new LinearLayoutManager(this);
            recycler.setLayoutManager(lManager);

            // Crear un nuevo adaptador
            adapter = new PendienteAdapter(items);
            recycler.setAdapter(adapter);
        }
    }

    @Override
    public void eddytoneNearbyDidChange() {
        mUrls = Arrays.asList(Scanner.nearbyUrls());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBeaconAdapter.clear();
                mBeaconAdapter.addAll(mUrls);
            }
        });
        System.out.println("mUrls = " + mUrls.size());
        for (Url url : mUrls) {
            String str = url.getUrl().toString();
            System.out.println("str = " + str);
            if (str.equals("http://bit.ly/2a2QDMc") || true) {
                URL_PENDIENTE = str;
                pendientes = getPendientes();
                ArrayAdapter<Pendiente> adapter = new ArrayAdapter<Pendiente>(this,
                        android.R.layout.activity_list_item, android.R.id.text1, pendientes);
                this.lstVwUsuarios.setAdapter(adapter);
            }
            System.out.println("URL_PENDIENTE After pedo = " + URL_PENDIENTE);

        }
    }

    public List<Pendiente> getPendientes(){
        ConnectServer server = new ConnectServer();
        server.execute();
        List<Pendiente> pendientes = new ArrayList<>();

        try {
            String json = server.get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Pendiente>>(){}.getType();
            pendientes = gson.fromJson(json, listType);
        } catch (Exception e){
            Log.e("Error", "No pude leer el JSON.");
        }

        return pendientes;
    }

    private class ConnectServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String json = ReadJson.read(URL_PENDIENTE);
            System.out.println("json = " + json);
            return json;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                TabsActivity.super.onPause();
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {
                View rootView = inflater.inflate(R.layout.fragment_tareas_pendientes, container, false);
                return rootView;
            }else{
                View rootView = inflater.inflate(R.layout.fragment_tareas_realizadas, container, false);
                return rootView;
            }/*else{
                View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);
                TextView user = (TextView) rootView.findViewById(R.id.txtVwMensaje);
                TextView pendientes = (TextView) rootView.findViewById(R.id.txtVwPendientes);
                TextView realizadas = (TextView) rootView.findViewById(R.id.txtVwRealizadas);
                user.setText(usuario);
                pendientes.setText("5\nPendientes");
                realizadas.setText("2\nRealizadas");
                return rootView;
            }*/
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

       /* @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:

                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }*/
    }
}
