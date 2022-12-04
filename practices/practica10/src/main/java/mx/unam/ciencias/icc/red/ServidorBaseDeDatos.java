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
        while (continuaEjecucion) {
            try {
                Socket socket = servidor.accept();
                Conexion<R> conexion = new Conexion<R>(bdd, socket);

                anotaMensaje("Conexión recibida de: %s.",
                        socket.getInetAddress().getCanonicalHostName());
                anotaMensaje("Serie de conexión: %d.",
                        Integer.valueOf(socket.getPort()));

                // set the port to the connection
                puerto = socket.getPort();

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
        escuchas.limpia();
    }

    /* Carga la base de datos del disco duro. */

    private void carga() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(ruta)));
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            anotaMensaje("Error al leer la base de datos de %s.", ruta);
        }
    }

    /**
     * Guarda la base de datos al disco duro, en el debido archivo.
     */
    private synchronized void guarda() {
        anotaMensaje("Guardando base de datos en %s.", ruta);

        try {
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(ruta)));

            synchronized (bdd) {
                bdd.guarda(out);
            }
            out.close();
        } catch (IOException ioe) {
            anotaMensaje("Error al guardar la base de datos en %s.",
                    ruta);
        }

        anotaMensaje("Base de datos guardada.");
    }

    /* Recibe los mensajes de la conexión. */
    private void mensajeRecibido(Conexion<R> conexion, Mensaje mensaje) {
        if (conexion.isActiva())
            switch (mensaje) {
                case BASE_DE_DATOS:
                    baseDeDatos(conexion);
                    break;
                case REGISTRO_AGREGADO:
                    registroAlterado(conexion, mensaje);
                    break;
                case REGISTRO_ELIMINADO:
                    registroEliminado(conexion);
                    break;
                case REGISTRO_MODIFICADO:
                    registroModificado(conexion);
                    break;
                case DESCONECTAR:
                    desconectar(conexion);
                    break;
                case DETENER_SERVICIO:
                    detenerServicio();
                    break;
                case GUARDA:

                    break;
                case ECO:
                    eco(conexion);
                    break;
                case INVALIDO:
                    error(conexion);
                    break;

            }
    }

    /* Maneja el mensaje BASE_DE_DATOS. */
    private void baseDeDatos(Conexion<R> conexion) {
        anotaMensaje("Base de datos pedida por %d.", puerto);

        try {
            conexion.enviaMensaje(Mensaje.BASE_DE_DATOS);
            conexion.enviaBaseDeDatos();
        } catch (IOException ioe) {
            anotaMensaje("Error al enviar la base de datos a la conexión %d.",
                    puerto);
        }
    }

    /**
     * Procesa la solicitud de agregar registro, agregando al registro a la
     * base de datos, y notificando a todas las conexiones del cambio.
     * Los cambios se deben reflejar en el archivo del disco duro.
     * 
     * @param conexion la conexión que agregó el registro.
     */
    private void registroAlterado(Conexion<R> conexion, Mensaje mensaje) {
        // view if message is MENSAGE.REGISTRO_AGREGADO or MENSAGE.REGISTRO_MODIFICADO
        if (mensaje == Mensaje.REGISTRO_AGREGADO) {
            try {
                R registro = conexion.recibeRegistro();
                agregaRegistro(registro);

                anotaMensaje("Registro agregado por %d.", puerto);

                for (Conexion<R> con : conexiones) {
                    if (con == conexion)
                        continue;

                    con.enviaMensaje(Mensaje.REGISTRO_AGREGADO);
                    con.enviaRegistro(registro);
                }
            } catch (IOException ioe) {
                anotaMensaje("Error al agregar registro por la conexión %d.",
                        puerto);
            }
            guarda();
        } else if (mensaje == Mensaje.REGISTRO_ELIMINADO) {
            registroEliminado(conexion);
        }

    }

    /**
     * Procesa la solicitud de eliminar un registro, eliminándo el registro de
     * la base de datos y notificando a las conexiones del cambio.
     * Los cambios se deben reflejar en el archivo del disco duro.
     * 
     * @param conexion la conexión que eliminó el registro.
     */
    private void registroEliminado(Conexion<R> conexion) {
        try {
            R registro = conexion.recibeRegistro();
            eliminaRegistro(registro);

            anotaMensaje("Registro eliminado por %d.", puerto);

            for (Conexion<R> con : conexiones) {
                if (con == conexion)
                    continue;

                con.enviaMensaje(Mensaje.REGISTRO_ELIMINADO);
                con.enviaRegistro(registro);
            }
        } catch (IOException ioe) {
            anotaMensaje("Error al eliminar registro por la conexión %d.",
                    puerto);
        }
        guarda();
    }

    /**
     * Procesa la solicitud de modificar un registro, modificando el registro
     * y notificando a las conexiones del cambio.
     * Los cambios se deben reflejar en el archivo del disco duro.
     * 
     * @param conexion la conexión que modificó el registro.
     */
    private void registroModificado(Conexion<R> conexion) {

        /*
         * Similar al anterior; en un try . . . catch recibimos dos registros de la
         * conexión; si ocurre un error ejecutamos el método error() con el mensaje
         * "Error recibiendo registros" y terminamos.
         * De otra forma invocamos modificaRegistro(); y para todas las conexiones dis-
         * tintas de la que envió el mensaje, les mandamos el mismo mensaje y los
         * registros. Si ocurre un error ejecutamos el método error() con el mensaje
         * "Error enviando registros"; pero no dejamos de recorrer la lista de
         * conexiones.
         * Al final anotamos el mensaje "Registro modificado por SERIE." con el número
         * de serie de la conexión. Por último guardamos la base de datos en el disco
         * duro con el método guarda().
         */
        try {
            R registro = conexion.recibeRegistro();
            R registro2 = conexion.recibeRegistro();
            modificaRegistro(registro, registro2);

            anotaMensaje("Registro modificado por %d.", puerto);

            for (Conexion<R> con : conexiones) {
                if (con == conexion)
                    continue;

                con.enviaMensaje(Mensaje.REGISTRO_MODIFICADO);
                con.enviaRegistro(registro);
                con.enviaRegistro(registro2);
            }
        } catch (IOException ioe) {
            anotaMensaje("Error al modificar registro por la conexión %d.",
                    puerto);
        }
    }

    /**
     * Desconecta a un cliente.
     * 
     * @param conexion la conexión que solicitó ser desconectada.
     */
    private void desconectar(Conexion<R> conexion) {
        desconecta(conexion);
    }

    /**
     * Detiene el servidor, terminando su ejecución.
     */
    private void detenerServicio() {
        continuaEjecucion = false;

        for (Conexion<R> con : conexiones)
            desconecta(con);

        try {
            servidor.close();
        } catch (IOException ioe) {
            anotaMensaje("Error al detener el servidor.");
        }
    }

    /**
     * Procesa la solicitud de eco, regresando eco.
     * 
     * @param conexion la conexión que realizó la solicitud.
     */
    private void eco(Conexion<R> conexion) {
        anotaMensaje("Solicitud de eco de %d.", puerto);

        try {
            conexion.enviaMensaje(Mensaje.ECO);
        } catch (IOException ioe) {
            anotaMensaje("Error al enviar ECO a la conexión %d.",
                    puerto);
        }
    }

    /**
     * Procesa un mensaje inválido desconectando al cliente que lo mandó.
     * 
     * @param conexion la conexión que mandó el mensaje.
     */
    private void error(Conexion<R> conexion) {
        anotaMensaje("Desconectando la conexión %d: Mensaje inválido.",
                puerto);
        desconecta(conexion);
    }

    /**
     * Desconecta a un cliente.
     * 
     * @param conexion la conexión a eliminar.
     */
    private void desconecta(Conexion<R> conexion) {
        conexion.desconecta();
        synchronized (conexiones) {
            conexiones.elimina(conexion);
        }
        anotaMensaje("La conexión %d ha sido desconectada.",
                puerto);
    }

    /* Agrega el registro a la base de datos. */
    private synchronized void agregaRegistro(R registro) {
        // Aquí va su código.
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

    /**
     * Notifica a los escuchas de alguna acción que se realizó.
     * 
     * @param formato    la cadena con el formato del mensaje.
     * @param argumentos los argumentos con los que se modificará la cadena.
     */
    private void anotaMensaje(String formato, Object... argumentos) {
        for (EscuchaServidor escucha : escuchas)
            escucha.procesaMensaje(formato, argumentos);
    }

    /**
     * Crea la base de datos concreta.
     * 
     * @return la base de datos concreta.
     */
    public abstract BaseDeDatos<R, ?> creaBaseDeDatos();

}