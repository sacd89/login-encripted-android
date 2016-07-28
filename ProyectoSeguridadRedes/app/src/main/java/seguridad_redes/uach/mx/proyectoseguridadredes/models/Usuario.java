package seguridad_redes.uach.mx.proyectoseguridadredes.models;

/**
 * Created by dani on 8/07/16.
 */
public class Usuario {

    private String _id;
    private String nombre;
    private String paterno;
    private String materno;
    private String email;
    private String password;

    public Usuario() {
    }

    public Usuario(String nombre, String paterno, String materno, String email, String password) {
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.email = email;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
