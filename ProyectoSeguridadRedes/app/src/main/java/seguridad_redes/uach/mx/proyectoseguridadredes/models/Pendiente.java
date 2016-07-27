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

    /**
     * {@code String} Nivel de prioridad para el Pendiente.
     */
    Integer prioridad;

    /**
     * {@code Boolean} Determina si ha sido terminado
     */
    Boolean terminado;

    /**
     * Constructor vacío.
     */
    public Pendiente () {
        this.terminado = Boolean.FALSE;
    }

    /**
     * Constructor de la clase {@link Pendiente}.
     *
     * @param descripcion {@code String} Descripción del Pendiente
     * @param fecha {@code String} Fecha de creación
     * @param prioridad {@code String} Nivel de prioridad para el Pendiente
     * @param terminado {@code Boolean} Determina si ha sido terminado o no el pendiente
     */
    public Pendiente(String descripcion, String fecha, Integer prioridad, Boolean terminado) {
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.prioridad = prioridad;
        this.terminado = terminado;
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
        return terminado;
    }

    /**
     * Establece el valor del campo incluido en el nombre del mismo método.
     *
     * @param terminado Boolean terminado
     */
    public void setTerminado(Boolean terminado) {
        this.terminado = terminado;
    }

    @Override
    public String toString(){
        return String.format("%s - %s", this.getDescripcion(), this.getPrioridad());
    }

}
