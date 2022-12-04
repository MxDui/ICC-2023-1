package mx.unam.ciencias.icc.red;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.Registro;

/**
 * Clase abstracta para servidores de bases de datos genéricas.
 */
public abstract class ServidorBaseDeDatos<R extends Registro<R, ?>> {

    /* La base de datos. */
    private BaseDeDatos<R, ?> bdd;
    /* La ruta donde cargar/guardar la base de datos. */
    private String ruta;
    /* El servidor de enchufes. */
    private ServerSocket servidor;
    /* El puerto. */
    private int puerto;
    /* Lista con las conexiones. */
    private Lista<Conexion<R>> conexiones;
    /* Bandera de continuación. */
    private boolean continuaEjecucion;
    /* Escuchas del servidor. */
    private Lista<EscuchaServidor> escuchas;

    /**
     * Crea un nuevo servidor usando la ruta recibida para poblar la base de
     * datos.
     * 
     * @param puerto el puerto dónde escuchar por conexiones.
     * @param ruta   la ruta en el disco del cual cargar/guardar la base de
     *               datos. Puede ser <code>null</code>, en cuyo caso se usará el
     *               nombre por omisión <code>base-de-datos.bd</code>.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public ServidorBaseDeDatos(int puerto, String ruta)
            throws IOException {
        // Aquí va su código.
        this.bdd = creaBaseDeDatos();
        this.servidor = new ServerSocket(puerto);
        this.puerto = puerto;
        this.ruta = ruta == null ? "base-de-datos.bd" : ruta;
        this.conexiones = new Lista<Conexion<R>>();
        this.continuaEjecucion = true;
        this.escuchas = new Lista<EscuchaServidor>();

        carga();
    }

    /**
     * Comienza a escuchar por conexiones de clientes.
     */
    public void sirve() {
        // Aquí va su código.

        anotaMensaje("Escuchando en el puerto: " + puerto + ".");
        while (continuaEjecucion) {
            try {
                Socket socket = servidor.accept();
                Conexion<R> conexion = new Conexion<R>(bdd, socket);

                anotaMensaje("Conexión recibida de: %s.",
                        socket.getInetAddress().getCanonicalHostName());
                anotaMensaje("Serial de conexión: %d.",
                        conexion.getSerie());

                conexion.agregaEscucha((con, men) -> mensajeRecibido(con, men));

                synchronized (conexiones) {
                    conexiones.agregaFinal(conexion);
                }

                new Thread(() -> conexion.recibeMensajes()).start();
            } catch (IOException ioe) {
                anotaMensaje("Error al aceptar conexión.");
            }
        }

    }

    /**
     * Agrega un escucha de servidor.
     * 
     * @param escucha el escucha a agregar.
     */
    public void agregaEscucha(EscuchaServidor escucha) {
        // Aquí va su código.
        escuchas.agregaFinal(escucha);
    }

    /**
     * Limpia todos los escuchas del servidor.
     */
    public void limpiaEscuchas() {
        // Aquí va su código.
        escuchas.limpia();
    }

    /* Carga la base de datos del disco duro. */
    private void carga() {
        // Aquí va su código.

        try {
            anotaMensaje("Cargando base de datos de " + ruta + ".");

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ruta)));

            bdd.carga(br);

            br.close();
            anotaMensaje("Base de datos cargada exitosamente de " + ruta + ".");
        } catch (IOException e) {
            anotaMensaje("Ocurrió un error al tratar de cargar " + ruta + ".");
            anotaMensaje("La base de datos estará inicialmente vacía.");
        }

    }

    /* Guarda la base de datos en el disco duro. */
    private synchronized void guarda() {
        // Aquí va su código.

        try {
            anotaMensaje("Guardando base de datos en " + ruta + ".");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruta)));

            // synchronized (bdd) {

            synchronized (bdd) {
                bdd.guarda(bw);
            }

            bw.close();

            anotaMensaje("Base de datos guardada.");
        } catch (IOException e) {
            anotaMensaje("Ocurrió un error al guardar la base de datos.");
        }

    }

    /* Recibe los mensajes de la conexión. */
    private void mensajeRecibido(Conexion<R> conexion, Mensaje mensaje) {
        // Aquí va su código.

        if (!conexion.isActiva()) {
            return;
        } else {
            switch (mensaje) {

                case BASE_DE_DATOS:
                    baseDeDatos(conexion);
                    break;
                case REGISTRO_AGREGADO:
                    registroAlterado(conexion, mensaje);
                    break;
                case REGISTRO_ELIMINADO:
                    registroAlterado(conexion, mensaje);
                    break;
                case REGISTRO_MODIFICADO:
                    registroModificado(conexion);
                    break;
                case DESCONECTAR:
                    desconectar(conexion);
                    break;
                case GUARDA:
                    guarda();
                    break;
                case DETENER_SERVICIO:
                    detenerServicio();
                    break;
                case ECO:
                    eco(conexion);
                case INVALIDO:
                    error(conexion, mensaje.toString());
                    break;

                default:
                    error(conexion, "Mensaje desconocido: " + mensaje);
                    break;
            }
        }
    }

    /* Maneja el mensaje BASE_DE_DATOS. */
    private void baseDeDatos(Conexion<R> conexion) {
        // Aquí va su código.

        try {
            conexion.enviaMensaje(Mensaje.BASE_DE_DATOS);
            conexion.enviaBaseDeDatos();
            anotaMensaje("Base de datos pedida por " + conexion.getSerie() + ".");
        } catch (IOException e) {
            error(conexion, "Error enviando la base de datos.");
        }

    }

    /* Maneja los mensajes REGISTRO_AGREGADO y REGISTRO_MODIFICADO. */
    private void registroAlterado(Conexion<R> conexion, Mensaje mensaje) {
        // Aquí va su código.

        try {
            R registro = conexion.recibeRegistro();
            if (mensaje == Mensaje.REGISTRO_AGREGADO) {
                agregaRegistro(registro);
                anotaMensaje("Registro agregado por " + conexion.getSerie() + ".");
            } else {
                eliminaRegistro(registro);
                anotaMensaje("Registro eliminado por " + conexion.getSerie() + ".");
            }
            guarda();
            for (Conexion<R> c : conexiones) {
                if (c != conexion) {
                    try {
                        c.enviaMensaje(mensaje);
                        c.enviaRegistro(registro);
                    } catch (IOException e) {
                        error(c, "Error enviando registro.");
                    }
                }
            }
        } catch (IOException e) {
            error(conexion, "Error recibiendo registro.");
        }
    }

    /* Maneja el mensaje REGISTRO_MODIFICADO. */
    private void registroModificado(Conexion<R> conexion) {
        // Aquí va su código.
        /*
         * Similar al anterior; en un try . . . catch recibimos dos registros de la
         * conexión;
         * si ocurre un error ejecutamos el método error() con el mensaje "Error
         * recibiendo
         * registros" y terminamos.
         * De otra forma invocamos modificaRegistro(); y para todas las conexiones
         * distintas de la que envió el mensaje, les mandamos el mismo mensaje y los
         * registros.
         * Si ocurre un error ejecutamos el método error() con el mensaje "Error
         * enviando
         * registros"; pero no dejamos de recorrer la lista de conexiones.
         * Al final anotamos el mensaje "Registro modificado por SERIE." con el número
         * de serie de la conexión. Por último guardamos la base de datos en el disco
         * duro
         * con el método guarda().
         */

        try {
            R viejo = conexion.recibeRegistro();
            R nuevo = conexion.recibeRegistro();
            modificaRegistro(viejo, nuevo);
            anotaMensaje("Registro modificado por " + conexion.getSerie() + ".");
            for (Conexion<R> c : conexiones) {
                if (c != conexion) {
                    try {
                        c.enviaMensaje(Mensaje.REGISTRO_MODIFICADO);
                        c.enviaRegistro(viejo);
                        c.enviaRegistro(nuevo);
                    } catch (IOException e) {
                        error(c, "Error enviando registros.");
                    }
                }
            }
        } catch (IOException e) {
            error(conexion, "Error recibiendo registros.");
        }
        guarda();
    }

    /* Maneja el mensaje DESCONECTAR. */
    private void desconectar(Conexion<R> conexion) {
        // Aquí va su código.
        /*
         * Anotamos el mensaje "Solicitud de desconexión de SERIE." con el número de
         * serie de la conexión y la desconectamos con el método desconecta().
         */

        anotaMensaje("Solicitud de desconexión de " + conexion.getSerie() + ".");
        desconecta(conexion);

    }

    /* Maneja el mensaje DETENER_SERVICIO. */
    private void detenerServicio() {
        // Aquí va su código.

        /*
         * Anotamos el mensaje "Solicitud para detener el servicio."; definimos la
         * bandera para continuar la ejecución como falsa; para toda conexión en la
         * lista
         * de conexiones la desconectamos; e intentamos cerrar el servidor de enchufes.
         * Si
         * ocurre un error, lo ignoramos: no hay nada que se pueda hacer en este punto.
         * Técnicamente puede ocurrir que dos clientes envíen este mensaje al mismo
         * tiempo y que dos hilos de ejecución intenten modificar la bandera para
         * continuar
         * la ejecución. Pero como de por sí el mensaje no puede ser enviado por la
         * interfaz
         * gráfica y el servidor termina su ejecución al finalizar el método, vamos a
         * ignorar
         * esta remota posibilidad.
         */

        anotaMensaje("Solicitud para detener el servicio.");
        continuaEjecucion = false;
        for (Conexion<R> c : conexiones) {
            desconecta(c);
        }
        try {
            servidor.close();
        } catch (IOException e) {
        }
    }

    /* Maneja el mensaje ECO. */
    private void eco(Conexion<R> conexion) {
        // Aquí va su código.
        /*
         * El método eco() anota el mensaje "Solicitud de eco de SERIE." con el número
         * de serie de la conexión; y trata de enviar el mensaje ECO de regreso. Si
         * ocurre un
         * error ejecuta el método error() con el mensaje "Error enviando eco".
         */

        anotaMensaje("Solicitud de eco de " + conexion.getSerie() + ".");
        try {
            conexion.enviaMensaje(Mensaje.ECO);
        } catch (IOException e) {
            error(conexion, "Error enviando eco.");
        }
    }

    /* Imprime un mensaje a los escuchas y desconecta la conexión. */
    private void error(Conexion<R> conexion, String mensaje) {
        // Aquí va su código.
        /*
         * El método error se ejecuta cuando ocurre cualquier error; además de anotar
         * el mensaje "Desconectando la conexión SERIE: MENSAJE." con el número de serie
         * de la conexión y el mensaje que reciba, desconecta la conexión con el método
         * desconecta().
         */

        anotaMensaje("Desconectando la conexión " + conexion.getSerie() + ": " + mensaje);
        desconecta(conexion);

    }

    /* Desconecta la conexión. */
    private void desconecta(Conexion<R> conexion) {
        // Aquí va su código.

        /*
         * Desconecta la conexión y la elimina de la lista de conexiones. Hay que
         * sincronizar la lista de conexiones antes de eliminar la conexión, para evitar
         * que se
         * intente modificar la lista al mismo tiempo por varios hilos de ejecución.
         * Por último se anota el mensaje "La conexión SERIE ha sido desconectada." con
         * el número de serie de la conexión.
         */

        conexion.desconecta();
        conexiones.elimina(conexion);
        anotaMensaje("La conexión " + conexion.getSerie() + " ha sido desconectada.");
    }

    /* Agrega el registro a la base de datos. */
    private synchronized void agregaRegistro(R registro) {
        // Aquí va su código.
        /*
         * Sólo agrega el registro a la base de datos; la única razón de tener un método
         * auxiliar para hacerlo, es para que el mismo esté sincronizado.
         */

        synchronized (bdd) {
            bdd.agregaRegistro(registro);
        }

    }

    /* Elimina el registro de la base de datos. */
    private synchronized void eliminaRegistro(R registro) {
        // Aquí va su código.

        synchronized (bdd) {
            bdd.eliminaRegistro(registro);
        }
    }

    /* Modifica el registro en la base de datos. */
    private synchronized void modificaRegistro(R registro1, R registro2) {
        // Aquí va su código.

        synchronized (bdd) {
            bdd.modificaRegistro(registro1, registro2);
        }
    }

    /* Procesa los mensajes de todos los escuchas. */
    private void anotaMensaje(String formato, Object... argumentos) {
        // Aquí va su código.

        /*
         * Sólo modifica el registro en la base de datos; la única razón de tener un
         * método auxiliar para hacerlo, es para que el mismo esté sincronizado.
         */

        for (EscuchaServidor escucha : escuchas) {
            escucha.procesaMensaje(formato, argumentos);
        }
    }

    /**
     * Crea la base de datos concreta.
     * 
     * @return la base de datos concreta.
     */
    public abstract BaseDeDatos<R, ?> creaBaseDeDatos();
}
