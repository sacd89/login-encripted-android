package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
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
import seguridad_redes.uach.mx.proyectoseguridadredes.models.Usuario;
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
    ArrayList<Pendiente> terminadas = new ArrayList<>();
    ArrayList<Pendiente> sinTerminar = new ArrayList<>();




    ScannerDelegate a = new ScannerDelegate() {
        @Override
        public void eddytoneNearbyDidChange() {
            mUrls = Arrays.asList(Scanner.nearbyUrls());

            if(items.isEmpty()){
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String email = settings.getString("email", "");
                String password = settings.getString("password", "");
                String id = settings.getString("id", "");
//                Usuario usuario = null;
//                if (!email.isEmpty() && !password.isEmpty()) {
//                    usuario = getUsuario(email, password);
//                }


                if(fragmentPendientes.getAdapter() != null && fragmentPendientes.getRecyclerView()!=null && fragmentRealizadas.getAdapter() != null && fragmentRealizadas.getRecyclerView()!=null){
                    ArrayList<Pendiente> terminadas = new ArrayList<>();
                    ArrayList<Pendiente> sinTerminar = new ArrayList<>();
                    for (Url url : mUrls) {
                        String str = url.getUrl().toString();
                        items = getPendientes(id);
                        System.out.println("items.size() = " + items.size());
                    }
                    for(Pendiente p : items){
                        if(p.getTerminado()){
                            terminadas.add(p);
                        } else {
                            sinTerminar.add(p);
                        }
                    }
                    fragmentPendientes.setAdapter(new Pendiente_Adapter(fragmentPendientes.getActivity(), sinTerminar));
                    fragmentPendientes.getRecyclerView().setAdapter(fragmentPendientes.getAdapter());
                    fragmentPendientes.getAdapter().notifyDataSetChanged();

                    fragmentRealizadas.setAdapter(new Realizada_Adapter(fragmentRealizadas.getActivity(), terminadas));
                    fragmentRealizadas.getRecyclerView().setAdapter(fragmentRealizadas.getAdapter());
                    fragmentRealizadas.getAdapter().notifyDataSetChanged();

                }
            } else {

                fragmentPendientes.getRecyclerView().setAdapter(fragmentPendientes.getAdapter());
                fragmentPendientes.getAdapter().notifyDataSetChanged();

                fragmentRealizadas.getRecyclerView().setAdapter(fragmentRealizadas.getAdapter());
                fragmentRealizadas.getAdapter().notifyDataSetChanged();

//                ArrayList<Pendiente> terminadas = new ArrayList<>();
//                ArrayList<Pendiente> sinTerminar = new ArrayList<>();
//                for(Pendiente p : items){
//                    if(p.getTerminado()){
//                        terminadas.add(p);
//                    } else {
//                        sinTerminar.add(p);
//                    }
//                }
//                fragmentPendientes.setAdapter(new Pendiente_Adapter(fragmentPendientes.getActivity(), sinTerminar));
//                fragmentPendientes.getRecyclerView().setAdapter(fragmentPendientes.getAdapter());
//                fragmentPendientes.getAdapter().notifyDataSetChanged();
//
//                fragmentRealizadas.setAdapter(new Realizada_Adapter(fragmentRealizadas.getActivity(), terminadas));
//                fragmentRealizadas.getRecyclerView().setAdapter(fragmentRealizadas.getAdapter());
//                fragmentRealizadas.getAdapter().notifyDataSetChanged();
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

        System.out.println("#############");

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String email = settings.getString("email", "");
        String password = settings.getString("password", "");
        String nombre = settings.getString("nombre", "");
        String paterno = settings.getString("paterno", "");
        String materno = settings.getString("materno", "");
//        Usuario usuario = null;
//        if (!email.isEmpty() && !password.isEmpty()) {
//            usuario = getUsuario(email, password);
//        }

        getSupportActionBar().setTitle(String.format("%s %s %s", nombre, paterno, materno));

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
//                Snackbar.make(view, getString(R.string.tareaNueva), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent i = new Intent(getBaseContext(), AddPendienteActivity.class);
                startActivity(i);
            }
        });

        try {
            socket = IO.socket("https://task-master-web.herokuapp.com");
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
                //Notificacion
                notificaciones();
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

    public void notificaciones(){
        int mId = 001;
        NotificationCompat.Builder mBuilder = (android.support.v7.app.NotificationCompat.Builder)
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.andy_icon)
                        .setAutoCancel(true)
                        .setContentTitle("Pendientes")
                        .setContentText("Tu lista de pendientes se ha actualizado.");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, TabsActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
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

    public class ConnectServer extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... parameters) {
            System.out.println("parameters[0] = " + parameters[0]);
            String json = ReadJson.readTODOS(parameters[0]);
            System.out.println("json = " + json);
            return json;

        }
    }
//    public class ConnectServerUsuario extends AsyncTask<String, Integer, String>{
//        @Override
//        protected String doInBackground(String... parameters) {
//            String json = ReadJson.logIn(parameters[0], parameters[1]);
//            return json;
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    public Usuario getUsuario(String correo, String pass){
//        ConnectServerUsuario server = new ConnectServerUsuario();
//        server.execute(correo, pass);
//        Usuario usuario = new Usuario();
//        try {
//            String json = server.get();
//            if(json.equals("null")){
//                return null;
//            } else{
//                Gson gson = new Gson();
//                Type listType = new TypeToken<Usuario>(){}.getType();
//                usuario = gson.fromJson(json, listType);
//                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putString("user_id", usuario.get_id());
//                editor.apply();
//            }
//
//        } catch (Exception e){
//            Log.e("Error", "No se puede leer el JSON.");
//        }
//
//        return usuario;
//    }

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
