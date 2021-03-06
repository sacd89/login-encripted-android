package seguridad_redes.uach.mx.proyectoseguridadredes.models;

/**
 * Created by dani on 25/07/16.
 */
public class Pendiente {
    /**
     * {@code String} Descripción del Pendiente.
     */
    String descripcion;

    /**
     * {@code String} Fecha de creación.
     */
    String fecha;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    private String _id;

    /**
     * {@code String} Nivel de prioridad para el Pendiente.
     */
    Integer prioridad;

    /**
     * {@code Boolean} Determina si ha sido terminado
     */
    Boolean terminada;

    /**
     * Constructor vacío.
     */
    public Pendiente () {
        this.terminada = Boolean.FALSE;
    }

    /**
     * Constructor de la clase {@link Pendiente}.
     *
     * @param descripcion {@code String} Descripción del Pendiente
     * @param fecha {@code String} Fecha de creación
     * @param prioridad {@code String} Nivel de prioridad para el Pendiente
     * @param terminado {@code Boolean} Determina si ha sido terminado o no el pendiente
     */
    public Pendiente(String descripcion, String fecha, Integer prioridad, Boolean terminado, String id) {
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.prioridad = prioridad;
        this.terminada = terminado;
        this._id = id;
    }

    public Pendiente(String descripcion, Integer prioridad){
        this.descripcion = descripcion;
        this.prioridad = prioridad;
    }

    public Pendiente(String descripcion, String fecha){
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    /**
     * Regresa el campo incluido en el nombre del mismo método.
     *
     * @return String - descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece el valor del campo incluido en el nombre del mismo método.
     *
     * @param descripcion String descripción
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Regresa el campo incluido en el nombre del mismo método.
     *
     * @return String fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Establece el valor del campo incluido en el nombre del mismo método.
     *
     * @param fecha String fecha
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Regresa el campo incluido en el nombre del mismo método.
     *
     * @return String prioridad
     */
    public Integer getPrioridad() {
        return prioridad;
    }

    /**
     * Establece el valor del campo incluido en el nombre del mismo método.
     *
     * @param prioridad String prioridad
     */
    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    /**
     * Regresa el campo incluido en el nombre del mismo método.
     *
     * @return Boolean terminado
     */
    public Boolean getTerminado() {
        return terminada;
    }

    /**
     * Establece el valor del campo incluido en el nombre del mismo método.
     *
     * @param terminado Boolean terminado
     */
    public void setTerminado(Boolean terminado) {
        this.terminada = terminado;
    }

    @Override
    public String toString(){
        return String.format("%s - %s", this.getDescripcion(), this.getPrioridad());
    }

}
