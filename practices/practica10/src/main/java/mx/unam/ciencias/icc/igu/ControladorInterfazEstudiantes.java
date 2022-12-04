package mx.unam.ciencias.icc.igu;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.BaseDeDatosEstudiantes;
import mx.unam.ciencias.icc.Estudiante;
import mx.unam.ciencias.icc.EventoBaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.red.Conexion;
import mx.unam.ciencias.icc.red.Mensaje;

/**
 * Clase para el controlador de la ventana principal de la aplicación.
 */
public class ControladorInterfazEstudiantes {

    /* Opción de menu para cambiar el estado de la conexión. */
    @FXML
    private MenuItem menuConexion;
    /* Opción de menu para agregar. */
    @FXML
    private MenuItem menuAgregar;
    /* Opción de menu para editar. */
    @FXML
    private MenuItem menuEditar;
    /* Opción de menu para eliminar. */
    @FXML
    private MenuItem menuEliminar;
    /* Opción de menu para buscar. */
    @FXML
    private MenuItem menuBuscar;
    /* El botón de agregar. */
    @FXML
    private Button botonAgregar;
    /* El botón de editar. */
    @FXML
    private Button botonEditar;
    /* El botón de eliminar. */
    @FXML
    private Button botonEliminar;
    /* El botón de buscar. */
    @FXML
    private Button botonBuscar;

    /* La tabla. */
    @FXML
    private TableView<Estudiante> tabla;

    /* La ventana. */
    private Stage escenario;
    /* El modelo de la selección. */
    private TableView.TableViewSelectionModel<Estudiante> modeloSeleccion;
    /* La selección. */
    private ObservableList<TablePosition> seleccion;
    /* Los renglones en la tabla. */
    private ObservableList<Estudiante> renglones;

    /* La base de datos. */
    private BaseDeDatosEstudiantes bdd;
    /* La conexión del cliente. */
    private Conexion<Estudiante> conexion;
    /* Si hay o no conexión. */
    private boolean conectado;

    /* Inicializa el controlador. */
    @FXML
    private void initialize() {
        // Aquí va su código.
        renglones = tabla.getItems();
        modeloSeleccion = tabla.getSelectionModel();
        modeloSeleccion.setSelectionMode(SelectionMode.MULTIPLE);
        seleccion = modeloSeleccion.getSelectedCells();
        ListChangeListener<TablePosition> lcl = c -> cambioSeleccion();
        seleccion.addListener(lcl);
        cambioSeleccion();

        setConectado(false);
        bdd = new BaseDeDatosEstudiantes();
        bdd.agregaEscucha((e, r1, r2) -> eventoBaseDeDatos(e, r1, r2));

    }

    /* Cambioa el estado de la conexión. */
    @FXML
    private void cambiaConexion(ActionEvent evento) {
        // Aquí va su código.

        if (!conectado)
            conectar();
        else
            desconectar();

    }

    /**
     * Termina el programa.
     * 
     * @param evento el evento que generó la acción.
     */
    @FXML
    public void salir(Event evento) {
        // Aquí va su código.

        desconectar();
        Platform.exit();
    }

    /* Agrega un nuevo estudiante. */
    @FXML
    private void agregaEstudiante(ActionEvent evento) {
        // Aquí va su código.
        DialogoEditaEstudiante dialogo;
        try {
            dialogo = new DialogoEditaEstudiante(escenario, null);
        } catch (IOException ioe) {
            String mensaje = (" Ocurrió un error al tratar de cargar el " +
                    "archivo fxml del diálogo.");
            dialogoError(" Error al cargar diálogo", mensaje);
            return;
        }
        dialogo.showAndWait();
        tabla.requestFocus();
        if (!dialogo.isAceptado())
            return;
        bdd.agregaRegistro(dialogo.getEstudiante());

        try {
            conexion.enviaMensaje(Mensaje.REGISTRO_AGREGADO);
            conexion.enviaRegistro(dialogo.getEstudiante());

            System.out.println("Se envió el mensaje de registro agregado");
        } catch (IOException ioe) {
            dialogoError("Error con el servidor",
                    "No se pudo enviar un estudiante a agregar.");
        }
    }

    /* Edita un estudiante. */
    @FXML
    private void editaEstudiante(ActionEvent evento) {
        // Aquí va su código.
        int r = seleccion.get(0).getRow();
        Estudiante estudiante = renglones.get(r);
        DialogoEditaEstudiante dialogo;
        try {
            dialogo = new DialogoEditaEstudiante(escenario,
                    estudiante);
        } catch (IOException ioe) {
            String mensaje = (" Ocurri ó␣un␣ error ␣al␣ tratar ␣de␣" +
                    " cargar ␣el␣diá logo ␣de␣ estudiante .");
            dialogoError(" Error ␣al␣ cargar ␣ interfaz ", mensaje);
            return;
        }
        dialogo.showAndWait();
        tabla.requestFocus();
        if (!dialogo.isAceptado())
            return;

        try {
            conexion.enviaMensaje(Mensaje.REGISTRO_MODIFICADO);
            conexion.enviaRegistro(estudiante);
            conexion.enviaRegistro(dialogo.getEstudiante());
        } catch (IOException ioe) {
            dialogoError("Error con el servidor",
                    "No se pudieron enviar estudiantes a modificar.");
        }
        bdd.modificaRegistro(estudiante, dialogo.getEstudiante());
    }

    /* Elimina uno o varios estudiantes. */
    @FXML
    private void eliminaEstudiantes(ActionEvent evento) {
        // Aquí va su código.
        int s = seleccion.size();
        String titulo = (s > 1) ? " Elimina  estudiantes " : " Elimina  estudiante ";
        String mensaje = (s > 1) ? " Esto eliminará a los estudiantes seleccionados "
                : " Esto eliminará al estudiante seleccionado ";
        String aceptar = titulo;
        String cancelar = (s > 1) ? " Conservar estudiantes " : " Conservar estudiante ";
        if (!dialogoDeConfirmacion(
                titulo, mensaje, "¿Está seguro ?",
                aceptar, cancelar))
            return;
        Lista<Estudiante> aEliminar = new Lista<Estudiante>();
        for (TablePosition tp : seleccion)
            aEliminar.agregaFinal(renglones.get(tp.getRow()));
        modeloSeleccion.clearSelection();

        for (Estudiante e : aEliminar) {
            bdd.eliminaRegistro(e);
            renglones.remove(e);
            try {
                conexion.enviaMensaje(Mensaje.REGISTRO_ELIMINADO);
                conexion.enviaRegistro(e);
            } catch (IOException ioe) {
                dialogoError("Error con el servidor",
                        "No se pudo enviar un estudiante a eliminar.");
            }
        }
    }

    /* Busca estudiantes. */
    @FXML
    private void buscaEstudiantes(ActionEvent evento) {
        // Aquí va su código.
        try {

            DialogoBuscaEstudiantes dialogo = new DialogoBuscaEstudiantes(escenario);
            dialogo.showAndWait();

            if (!dialogo.isAceptado())
                return;

            Lista<Estudiante> estudiantes = bdd.buscaRegistros(
                    dialogo.getCampo(), dialogo.getValor());

            modeloSeleccion.clearSelection();

            for (Estudiante estudiante : estudiantes) {
                modeloSeleccion.select(estudiante);
            }

        } catch (IOException ioe) {

            String mensaje = String.format("Ocurrió un error al tratar de " + "cargar el diálogo de búsqueda.");
            dialogoError("Error al cargar diálogo de búsqueda", mensaje);
            return;

        }

    }

    /* Muestra un diálogo con información del programa. */
    @FXML
    private void acercaDe(ActionEvent evento) {
        // Aquí va su código.
        Alert dialogo = new Alert(AlertType.INFORMATION);
        dialogo.initOwner(escenario);
        dialogo.initModality(Modality.WINDOW_MODAL);
        dialogo.setTitle("Acerca de Administrador de Estudiantes.");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Aplicación para administrar estudiantes.\n" +
                "Copyright © 2022 Facultad de Ciencias, UNAM.");
        dialogo.showAndWait();

    }

    /**
     * Define el escenario.
     * 
     * @param escenario el escenario.
     */
    public void setEscenario(Stage escenario) {
        // Aquí va su código.
        this.escenario = escenario;
    }

    /* Conecta el cliente con el servidor. */
    private void conectar() {
        String direccion = "localhost";
        int puerto = 5000;
        // Aquí va su código.

        try {
            DialogoConectar dialogo = new DialogoConectar(escenario);
            dialogo.showAndWait();
            if (!dialogo.isAceptado())
                return;
            direccion = dialogo.getDireccion();
            puerto = dialogo.getPuerto();
        } catch (IOException ioe) {
            String mensaje = String.format("Ocurrió un error al tratar de " + "cargar el diálogo de conexión.");
            dialogoError("Error al cargar diálogo de conexión", mensaje);
            return;
        }

        try {
            Socket enchufe = new Socket(direccion, puerto);
            conexion = new Conexion<Estudiante>(bdd, enchufe);
            new Thread(() -> conexion.recibeMensajes()).start();
            conexion.enviaMensaje(Mensaje.BASE_DE_DATOS);
            conexion.agregaEscucha((c, m) -> mensajeRecibido(c, m));
        } catch (IOException ioe) {
            conexion = null;
            String mensaje = String.format("Ocurrió un error al tratar de " +
                    "conectarnos a %s:%d.\n", direccion, puerto);
            dialogoError("Error al establecer conexión", mensaje);
            System.out.println(ioe.getMessage());
            return;
        }
        setConectado(true);
    }

    /* Desconecta el cliente del servidor. */
    private void desconectar() {
        // Aquí va su código.

        if (!conectado)
            return;
        setConectado(false);
        conexion.desconecta();
        conexion = null;
        bdd.limpia();
    }

    /* Cambia la interfaz gráfica dependiendo si estamos o no conectados. */
    private void setConectado(boolean conectado) {
        // Aquí va su código.
        this.conectado = conectado;
        menuConexion.setDisable(conectado);
        menuAgregar.setDisable(!conectado);
        menuBuscar.setDisable(!conectado);
        botonAgregar.setDisable(!conectado);
        botonBuscar.setDisable(!conectado);
    }

    /* Maneja un evento de cambio en la base de datos. */
    private void eventoBaseDeDatos(EventoBaseDeDatos evento,
            Estudiante estudiante1,
            Estudiante estudiante2) {
        // Aquí va su código.

        switch (evento) {
            case BASE_LIMPIADA:
                renglones.clear();
                break;
            case REGISTRO_AGREGADO:
                agregaEstudiante(estudiante1);
                break;
            case REGISTRO_ELIMINADO:
                eliminaEstudiante(estudiante1);
                break;
            case REGISTRO_MODIFICADO:
                Platform.runLater(() -> tabla.sort());
                break;
        }
    }

    /*
     * Actualiza la interfaz dependiendo del número de renglones
     * seleccionados.
     */
    private void cambioSeleccion() {
        // Aquí va su código.
        int s = seleccion.size();
        menuEliminar.setDisable(s == 0);
        menuEditar.setDisable(s != 1);
        botonEliminar.setDisable(s == 0);
        botonEditar.setDisable(s != 1);
    }

    /* Crea un diálogo con una pregunta que hay que confirmar. */
    private boolean dialogoDeConfirmacion(String titulo,
            String mensaje, String pregunta,
            String aceptar, String cancelar) {
        // Aquí va su código.
        Alert dialogo = new Alert(AlertType.CONFIRMATION);
        dialogo.initOwner(escenario);
        dialogo.initModality(Modality.WINDOW_MODAL);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(mensaje);
        dialogo.setContentText(pregunta);
        ButtonType botonAceptar = new ButtonType(aceptar);
        ButtonType botonCancelar = new ButtonType(cancelar);
        dialogo.getButtonTypes().setAll(botonAceptar, botonCancelar);
        Optional<ButtonType> resultado = dialogo.showAndWait();
        return resultado.get() == botonAceptar;
    }

    /* Recibe los mensajes de la conexión. */
    private void mensajeRecibido(Conexion<Estudiante> conexion, Mensaje mensaje) {
        // Aquí va su código.
        System.out.println(mensaje);
        if (conectado) {
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
                    desconectar();
                    break;
                case GUARDA:
                    break;
                case DETENER_SERVICIO:
                    break;
                case ECO:
                    break;
                default:
                    Platform.runLater(() -> dialogoError("Error con el servidor",
                            "Mensaje inválido recibido. " +
                                    "Se finalizará la conexión."));
                    break;

            }
        }
    }

    /* Maneja el mensaje BASE_DE_DATOS. */
    private void baseDeDatos(Conexion<Estudiante> conexion) {
        // Aquí va su código.
        try {
            conexion.recibeBaseDeDatos();
        } catch (IOException ioe) {
            String m = "No se pudo recibir la base de datos. " +
                    "Se finalizará la conexión.";
            Platform.runLater(() -> dialogoError("Error con el servidor", m));
            return;
        }
    }

    /* Maneja los mensajes REGISTRO_AGREGADO y REGISTRO_MODIFICADO. */
    private void registroAlterado(Conexion<Estudiante> conexion,
            Mensaje mensaje) {
        // Aquí va su código.
        Estudiante e;
        try {
            e = conexion.recibeRegistro();
        } catch (IOException ioe) {
            String m = "No se pudo recibir un registro. " +
                    "Se finalizará la conexión.";
            Platform.runLater(() -> dialogoError("Error con el servidor", m));
            return;
        }
        if (mensaje == Mensaje.REGISTRO_AGREGADO)
            bdd.agregaRegistro(e);

        else
            bdd.eliminaRegistro(e);
    }

    /* Maneja el mensaje REGISTRO_MODIFICADO. */
    private void registroModificado(Conexion<Estudiante> conexion) {
        // Aquí va su código.
        /*
         * • registroModificado(). Se intenta recibir dos registros; si ocurre un error
         * se
         * muestra un diálogo de error con un mensaje apropiado. El diálogo se debe
         * mostrar usando Platform.runLater().
         * Si no hay error se modifica el primer registro con el segundo en la base de
         * datos.
         */

        Estudiante e1, e2;
        try {
            e1 = conexion.recibeRegistro();
            e2 = conexion.recibeRegistro();
        } catch (IOException ioe) {
            String m = "No se pudo recibir un registro. " +
                    "Se finalizará la conexión.";
            Platform.runLater(() -> dialogoError("Error con el servidor", m));
            return;
        }

    }

    /* Muestra un diálogo de error. */
    private void dialogoError(String titulo, String mensaje) {
        // Aquí va su código.
        if (conectado)
            desconectar();
        Alert dialogo = new Alert(AlertType.ERROR);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(null);
        dialogo.setContentText(mensaje);
        dialogo.setOnCloseRequest(e -> tabla.getItems().clear());
        dialogo.show();
        escenario.requestFocus();
    }

    /* Agrega un estudiante a la tabla. */
    private void agregaEstudiante(Estudiante estudiante) {
        // Aquí va su código.
        tabla.getItems().add(estudiante);
        tabla.sort();
    }

    /* Elimina un estudiante de la tabla. */
    private void eliminaEstudiante(Estudiante estudiante) {
        // Aquí va su código.
        tabla.getItems().remove(estudiante);
        tabla.sort();
    }
}
