package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import seguridad_redes.uach.mx.proyectoseguridadredes.models.Pendiente;
import seguridad_redes.uach.mx.proyectoseguridadredes.models.Usuario;

import static android.R.attr.type;
import static android.R.attr.value;

public class AddPendienteActivity extends AppCompatActivity {

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText descripcionTxt,datePickerTxt,prioridadTxt;
    Socket socket;
    String descripcion, fecha, userId;
    Integer prioridad;
    Boolean terminada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pendiente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        descripcionTxt = (EditText) findViewById(R.id.txtDescripcion);
        datePickerTxt = (EditText) findViewById(R.id.txtDate);
        prioridadTxt = (EditText) findViewById(R.id.txtPrioridad);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            addRow();
            Intent i = new Intent(getBaseContext(), TabsActivity.class);
            startActivity(i);
            }
        });

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        datePickerTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddPendienteActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        datePickerTxt.setText(sdf.format(myCalendar.getTime()));
    }

    public void addRow() {
        try {
            socket = IO.socket("https://task-master-web.herokuapp.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String usurioOwner = settings.getString("id", "");

        descripcion = String.valueOf(descripcionTxt.getText());
        fecha = String.valueOf(datePickerTxt.getText());
        prioridad = Integer.parseInt(String.valueOf(prioridadTxt.getText()));
        terminada = false;
        Pendiente p = new Pendiente(descripcion,fecha,prioridad, terminada, usurioOwner);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ = "+ p);
        socket.connect();
        JsonObject json = new JsonObject();
        json.addProperty("descripcion", descripcion);
        json.addProperty("prioridad", prioridad);
        json.addProperty("idUsuario", usurioOwner);
        socket.emit("crearNuevoPendienteMovil", json.toString());
    }

}
