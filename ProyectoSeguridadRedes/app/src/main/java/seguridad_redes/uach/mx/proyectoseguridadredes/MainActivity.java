package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        mensaje = (TextView) findViewById(R.id.txtVwMensaje);
        mensaje.setText("Has iniciado sesión como: " + bundle.getString("nombre") + " " +
        bundle.getString("paterno")+ " " + bundle.getString("materno"));

    }
}
