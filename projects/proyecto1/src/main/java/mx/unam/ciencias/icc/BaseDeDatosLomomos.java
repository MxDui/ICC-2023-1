package mx.unam.ciencias.icc;

/**
 * Clase para bases de datos de lomomos.
 */
public class BaseDeDatosLomomos extends BaseDeDatos {

    /**
     * Crea un lomomo en blanco.
     * 
     * @return un lomomo en blanco.
     */

    @Override
    public Registro creaRegistro() {
        return new Lomomo(null, null, 0, 0.0, null);
    }
}
