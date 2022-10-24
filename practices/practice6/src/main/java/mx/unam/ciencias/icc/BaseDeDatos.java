package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Clase abstracta para bases de datos genéricas. Provee métodos para agregar y
 * eliminar registros, y para guardarse y cargarse de una entrada y salida
 * dados. Además, puede hacer búsquedas con valores arbitrarios sobre los campos
 * de los registros.
 *
 * Las clases que extiendan a BaseDeDatos deben implementar el método {@link
 * #creaRegistro}, que crea un registro genérico en blanco.
 *
 * @param <R> El tipo de los registros, que deben implementar la interfaz {@link
 *            Registro}.
 * @param <C> El tipo de los campos de los registros, que debe ser una
 *            enumeración {@link Enum}.
 */
public abstract class BaseDeDatos<R extends Registro<R, C>, C extends Enum> {

    /* Lista de registros en la base de datos. */
    private Lista<R> registros;

    /**
     * Constructor único.
     */
    public BaseDeDatos() {
        // Aquí va su código.
        this.registros = new Lista<R>();
    }

    /**
     * Regresa el número de registros en la base de datos.
     * 
     * @return el número de registros en la base de datos.
     */
    public int getNumRegistros() {
        // Aquí va su código.
        return registros.getLongitud();
    }

    /**
     * Regresa una lista con los registros en la base de datos. Modificar esta
     * lista no cambia a la información en la base de datos.
     * 
     * @return una lista con los registros en la base de datos.
     */
    public Lista<R> getRegistros() {
        // Aquí va su código.
        return registros.copia();
    }

    /**
     * Agrega el registro recibido a la base de datos.
     * 
     * @param registro el registro que hay que agregar a la base de datos.
     */
    public void agregaRegistro(R registro) {
        // Aquí va su código.
        registros.agregaFinal(registro);
    }

    /**
     * Elimina el registro recibido de la base de datos.
     * 
     * @param registro el registro que hay que eliminar de la base de datos.
     */
    public void eliminaRegistro(R registro) {
        // Aquí va su código.
        registros.elimina(registro);
    }

    /**
     * Limpia la base de datos.
     */
    public void limpia() {
        // Aquí va su código.
        registros.limpia();
    }

    /**
     * Guarda todos los registros en la base de datos en la salida recibida.
     * 
     * @param out la salida donde hay que guardar los registos.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public void guarda(BufferedWriter out) throws IOException {
        // Aquí va su código.
        try {
            for (int i = 0; i < registros.getLongitud(); i++) {
                out.write(((Estudiante) registros.get(i)).seria());
            }
        } catch (IOException e) {
            throw new IOException();
        }
    }

    /**
     * Carga los registros de la entrada recibida en la base de datos. Si antes
     * de llamar el método había registros en la base de datos, estos son
     * eliminados.
     * 
     * @param in la entrada de donde hay que cargar los registos.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public void carga(BufferedReader in) throws IOException {
        // Aquí va su código.
        registros.limpia();
        String linea = in.readLine();

        try {
            try {
                while (linea != null) {
                    String[] campos = linea.split("\t");

                    R registro = creaRegistro();

                    if (campos.length == 4) {
                        registro.deseria(linea);
                    } else {
                        break;
                    }

                    // removed unchecked cast

                    registros.agregaFinal(registro);

                    linea = in.readLine();

                }
            } catch (IOException e) {
                throw new IOException("Error de lectura");
            }
        } catch (IOException ioe) {
            throw new IOException(ioe.getMessage());
        }

    }

    /**
     * Busca registros por un campo específico.
     * 
     * @param campo el campo del registro por el cuál buscar.
     * @param valor el valor a buscar.
     * @return una lista con los registros tales que casan el campo especificado
     *         con el valor dado.
     * @throws IllegalArgumentException si el campo no es de la enumeración
     *                                  correcta.
     */
    public Lista<R> buscaRegistros(C campo, Object valor) {
        // Aquí va su código.
        Lista<R> lista = new Lista<R>();

        for (int i = 0; i < registros.getLongitud(); i++) {
            R registro = registros.get(i);

            if (registro.casa(campo, valor)) {
                lista.agregaFinal((R) registro);
            }

        }
        return lista;

    }

    /**
     * Crea un registro en blanco.
     * 
     * @return un registro en blanco.
     */
    public abstract R creaRegistro();
}
