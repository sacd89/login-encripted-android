package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity implements ScannerDelegate {

    private TextView mensaje;
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Url> mUrls = new ArrayList<>();
    private BeaconAdapter mBeaconAdapter;
    public static ReadJson readJson = new ReadJson();
    public static String URL_PENDIENTE;
    private ListView lstVwUsuarios;
    List<Pendiente> pendientes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Bundle bundle = getIntent().getExtras();
        //mensaje = (TextView) findViewById(R.id.txtVwMensaje);
        //mensaje.setText("Has iniciado sesión como: " + bundle.getString("nombre") + " " +
        //        bundle.getString("paterno")+ " " + bundle.getString("materno"));
        ListView beaconListView = (ListView) findViewById(R.id.beaconListView);
        mBeaconAdapter = new BeaconAdapter(this, R.layout.beacon_list_item, mUrls);
        beaconListView.setAdapter(mBeaconAdapter);
        Global.logging = true;
        Global.expireTimer = 30000;
        Scanner.start(this);
        this.lstVwUsuarios = (ListView) findViewById(R.id.lstVwUsuarios);
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
            if (str.equals("http://bit.ly/2a2QDMc")) {
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
                MainActivity.super.onPause();
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
