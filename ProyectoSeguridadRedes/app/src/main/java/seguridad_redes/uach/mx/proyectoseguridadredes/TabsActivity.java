package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bluebite.android.eddystone.Global;
import com.bluebite.android.eddystone.Scanner;
import com.bluebite.android.eddystone.ScannerDelegate;
import com.bluebite.android.eddystone.Url;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seguridad_redes.uach.mx.proyectoseguridadredes.models.Pendiente;
import seguridad_redes.uach.mx.proyectoseguridadredes.utils.ReadJson;

public class TabsActivity extends AppCompatActivity {//implements ScannerDelegate {

    private Socket socket;
    ArrayList<Pendiente> items = new ArrayList<>();
    private ViewPager mViewPager;
    private static ViewPagerAdapter adapter;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private int REQUEST_ENABLE_BT = 1;
    private String usuario;
    private TareasPendientes fragmentPendientes;
    private TareasRealizadas fragmentRealizadas;
    private List<Url> mUrls = new ArrayList<>();


    ScannerDelegate a = new ScannerDelegate() {
        @Override
        public void eddytoneNearbyDidChange() {
            System.out.println("items = " + items);
            mUrls = Arrays.asList(Scanner.nearbyUrls());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //mBeaconAdapter.clear();
                    System.out.println("WE MADE IT ONE TIME");
                    //mBeaconAdapter.addAll(mUrls);
                }
            });

            Bundle bundle = getIntent().getExtras();


            if(items.isEmpty()){
                for (Url url : mUrls) {
                    String str = url.getUrl().toString();
                    items = getPendientes(bundle.getString("idUsuario"));
                }
                fragmentPendientes.setAdapter(new Pendiente_Adapter(fragmentPendientes.getActivity(), items));
                fragmentPendientes.getRecyclerView().setAdapter(fragmentPendientes.getAdapter());
                fragmentPendientes.getAdapter().notifyDataSetChanged();

                fragmentRealizadas.setAdapter(new Realizada_Adapter(fragmentRealizadas.getActivity(), items));
                fragmentRealizadas.getRecyclerView().setAdapter(fragmentRealizadas.getAdapter());
                fragmentRealizadas.getAdapter().notifyDataSetChanged();
            }
        }
    };

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
        Scanner.start(a);
        System.out.println("MA ");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        usuario = bundle.getString("nombre") + " " +
                bundle.getString("paterno") + " " + bundle.getString("materno");
        System.out.println("usuario = " + usuario);
        getSupportActionBar().setTitle(usuario);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_assignment_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_assignment_turned_in_white_24dp);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {
            socket = IO.socket("https://task-master-seguridad.herokuapp.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("Se conecto");
                socket.emit("echo", "hello");
            }

        }).on("recargarPendientes", new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                System.out.println("Respuesta de Socket");
                items = new ArrayList<Pendiente>();

            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("Se desconecto");
            }

        });
        socket.connect();

    }

    //Setting View Pager
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentPendientes = new TareasPendientes();
        fragmentRealizadas = new TareasRealizadas();
        adapter.addFrag(fragmentPendientes);
        adapter.addFrag(fragmentRealizadas);
        viewPager.setAdapter(adapter);
    }


    //Return current fragment on basis of Position
    public Fragment getFragment(int pos) {
        return adapter.getItem(pos);
    }

    public ArrayList<Pendiente> getPendientes(String idUsuario){
        ConnectServer server = new ConnectServer();
        server.execute(idUsuario);
        ArrayList<Pendiente> pendientes = new ArrayList<>();

        try {
            String json = server.get();
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Pendiente>>(){}.getType();
            pendientes = gson.fromJson(json, listType);
        } catch (Exception e){
            Log.e("Error", "No pude leer el JSON.");
        }

        return pendientes;
    }

    private class ConnectServer extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... parameters) {
            System.out.println("parameters[0] = " + parameters[0]);
            String json = ReadJson.readTODOS(parameters[0]);
            System.out.println("json = " + json);

            //String json;
            //try {
            //    java.net.URL newUrl = new URL(URL_PENDIENTE);
            //    final HttpURLConnection urlConnection = (HttpURLConnection) newUrl.openConnection();
            //    urlConnection.setInstanceFollowRedirects(false);
            //    final String location = urlConnection.getHeaderField("location");
            //    System.out.println("location = " + location);
            //    json = ReadJson.read(URL_PENDIENTE);
            //    System.out.println("json = " + json);
            //} catch (Exception e) {
            //    json = "{}";
            //    System.out.println("ConnectServer error = " + e);
            //}
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
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("email");
                editor.remove("password");
                editor.apply();
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
}
