package mx.unam.ciencias.icc.igu;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
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
    }

    /* Edita un estudiante. */
    @FXML
    private void editaEstudiante(ActionEvent evento) {
        // Aquí va su código.
    }

    /* Elimina uno o varios estudiantes. */
    @FXML
    private void eliminaEstudiantes(ActionEvent evento) {
        // Aquí va su código.
    }

    /* Busca estudiantes. */
    @FXML
    private void buscaEstudiantes(ActionEvent evento) {
        // Aquí va su código.
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
        // Aquí va su código.
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
                renglones.add(estudiante1);
                tabla.sort();
                break;
            case REGISTRO_ELIMINADO:
                renglones.remove(estudiante1);
                tabla.sort();
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
    }

    /* Maneja el mensaje BASE_DE_DATOS. */
    private void baseDeDatos(Conexion<Estudiante> conexion) {
        // Aquí va su código.
    }

    /* Maneja los mensajes REGISTRO_AGREGADO y REGISTRO_MODIFICADO. */
    private void registroAlterado(Conexion<Estudiante> conexion,
            Mensaje mensaje) {
        // Aquí va su código.
    }

    /* Maneja el mensaje REGISTRO_MODIFICADO. */
    private void registroModificado(Conexion<Estudiante> conexion) {
        // Aquí va su código.
    }

    /* Muestra un diálogo de error. */
    private void dialogoError(String titulo, String mensaje) {
        // Aquí va su código.
    }

    /* Agrega un estudiante a la tabla. */
    private void agregaEstudiante(Estudiante estudiante) {
        // Aquí va su código.
    }

    /* Elimina un estudiante de la tabla. */
    private void eliminaEstudiante(Estudiante estudiante) {
        // Aquí va su código.
    }
}
