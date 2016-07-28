package seguridad_redes.uach.mx.proyectoseguridadredes;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import seguridad_redes.uach.mx.proyectoseguridadredes.models.Usuario;
import seguridad_redes.uach.mx.proyectoseguridadredes.utils.ReadJson;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    //private Socket socket;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final LinearLayout LoginBox = (LinearLayout) findViewById(R.id.LoginBox);
        LoginBox.setVisibility(View.GONE);
        Animation animTranslate = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate);
        animTranslate.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                LoginBox.setVisibility(View.VISIBLE);
                Animation animFade = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade);
                LoginBox.startAnimation(animFade);
            }
        });
        ImageView imgLogo = (ImageView) findViewById(R.id.imageView);
        imgLogo.startAnimation(animTranslate);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();

        try {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            String email = settings.getString("email", "");
            String password = settings.getString("password", "");
            if (!email.isEmpty() && !password.isEmpty()) {
                final Usuario usuario = getUsuario(email, password);
                if(usuario != null && usuario.getNombre() != null){
                    ejecutar(usuario);
                    super.onPause();
                    finish();
                    System.out.println("[Sesión recuperada]");
                }
            }
        } catch (Exception e) {
            Log.e("getUsuario Error", e.getMessage());
        }

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    try {
                        attemptLogin();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    attemptLogin();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() throws MalformedURLException {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            //focusView.requestFocus();
        }else{
            final Usuario usuario =getUsuario(mEmailView.getText().toString(), mPasswordView.getText().toString());
            if(usuario != null && usuario.getNombre() != null){

                /*try {
                    socket = IO.socket("https://login-encripted.herokuapp.com");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        System.out.println("Se conecto");
                        socket.emit("echo", "hello");
                    }

                }).on("echo back", new Emitter.Listener(){
                    @Override
                    public void call(Object... args) {
                        System.out.println("Respuesta de Socket");
                        ejecutar(usuario);
                        //Toast.makeText(LoginActivity.this , "Respuesta de Sockets", Toast.LENGTH_LONG).show();

                    }
                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        System.out.println("Se desconecto");
                    }

                });
                socket.connect();*/
                ejecutar(usuario);
                super.onPause();
                finish();
            } else {
                Toast.makeText(this, "No se pudo acceder correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public void ejecutar(Usuario usuario) {
        Intent i = new Intent(this, TabsActivity.class);
//        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("nombre", usuario.getNombre());
        i.putExtra("paterno", usuario.getPaterno());
        i.putExtra("materno", usuario.getMaterno());
        i.putExtra("idUsuario", usuario.get_id());
        startActivity(i);
    }

    public Usuario getUsuario(String correo, String pass){
        ConnectServer server = new ConnectServer();
        server.execute(correo, pass);
        Usuario usuario = new Usuario();
        try {
            String json = server.get();
            if(json.equals("null")){
                return null;
            } else{
                Gson gson = new Gson();
                Type listType = new TypeToken<Usuario>(){}.getType();
                usuario = gson.fromJson(json, listType);
                try {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("email", correo);
                    editor.putString("password", pass);
                    editor.apply();
                    System.out.println("[Sesión guardada]");
                } catch (Exception e) {
                    Log.e("getUsuario Error", e.getMessage());
                }
            }

        } catch (Exception e){
            Log.e("Error", "No se puede leer el JSON.");
        }

        return usuario;
    }

    private class ConnectServer extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... parameters) {
            String json = ReadJson.logIn(parameters[0], parameters[1]);
            return json;
        }
    }
}

