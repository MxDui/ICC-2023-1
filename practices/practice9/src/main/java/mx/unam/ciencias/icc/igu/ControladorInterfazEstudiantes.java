package mx.unam.ciencias.icc.igu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Optional;

import org.junit.rules.DisableOnDebug;

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
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.BaseDeDatosEstudiantes;
import mx.unam.ciencias.icc.CampoEstudiante;
import mx.unam.ciencias.icc.Estudiante;
import mx.unam.ciencias.icc.EventoBaseDeDatos;
import mx.unam.ciencias.icc.Lista;

/**
 * Clase para el controlador de la ventana principal de la aplicación.
 */
public class ControladorInterfazEstudiantes {

    /* Opción de menu para guardar. */
    @FXML
    private MenuItem menuGuardar;
    /* Opción de menu para editar. */
    @FXML
    private MenuItem menuEditar;
    /* Opción de menu para eliminar. */
    @FXML
    private MenuItem menuEliminar;

    /* La tabla. */
    @FXML
    private TableView<Estudiante> tabla;

    /* El botón de editar. */
    @FXML
    private Button botonEditar;
    /* El botón de eliminar. */
    @FXML
    private Button botonEliminar;

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
    /* El archivo. */
    private File archivo;
    /* Si la operación de guardar fue exitosa. */
    private boolean guardadoExitoso;

    /* Inicializa el controlador. */
    @FXML
    private void initialize() {
        // Aquí va su código.
        tabla.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        renglones = tabla.getItems();
        modeloSeleccion = tabla.getSelectionModel();
        seleccion = modeloSeleccion.getSelectedCells();

        setModificada(false);
        setBaseDeDatos(new BaseDeDatosEstudiantes());
    }

    /* Crea una nueva base de datos. */
    @FXML
    private void nuevaBaseDeDatos(ActionEvent evento) {
        // Aquí va su código.
        if (verificaGuardada("¿Desea guardarla antes de crear una nuevaaaa?"))
            return;
        archivo = null;
        setBaseDeDatos(new BaseDeDatosEstudiantes());
        setModificada(false);
        tabla.getItems().clear();

    }

    /* Carga una base de datos. */
    @FXML
    private void cargaBaseDeDatos(ActionEvent evento) {
        // Aquí va su código.
        if (verificaGuardada("¿Desea guardarla antes de cargar otra?"))
            return;
        FileChooser fc = new FileChooser();
        fc.setTitle("Cargar Base de Datos");
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("Bases de datos", "*.bd"),
                new ExtensionFilter("Todos los archivos", "*.*"));
        File archivo = fc.showOpenDialog(escenario);
        if (archivo != null)
            cargaBaseDeDatosDeArchivo(archivo);
    }

    /* Guarda la base de datos. */
    @FXML
    private void guardaBaseDeDatos(ActionEvent evento) {
        // Aquí va su código.
        if (archivo == null)
            guardaBaseDeDatosComo(evento);
        else
            guardaBaseDeDatosEnArchivo();
    }

    /* Guarda la base de datos con un nombre distinto. */
    @FXML
    private void guardaBaseDeDatosComo(ActionEvent evento) {
        // Aquí va su código.
        BaseDeDatosEstudiantes nbdd = new BaseDeDatosEstudiantes();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(archivo)));
            nbdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            String mensaje = String.format("Ocurrió un error al tratar de " +
                    "cargar la base de datos en '%s'.",
                    archivo.getName());
            dialogoError("Error al cargar base de datos", mensaje);
            return;
        }
        setBaseDeDatos(nbdd);

        this.archivo = null;
        setModificada(false);
    }

    /**
     * Termina el programa.
     * 
     * @param evento el evento que generó la acción.
     */
    @FXML
    public void salir(Event evento) {
        // Aquí va su código.
        if (!verificaGuardada("¿Desea guardarla antes de salir?"))
            return;
        Platform.exit();
    }

    /* Agrega un nuevo estudiante. */
    @FXML
    private void agregaEstudiante(ActionEvent evento) {
        // Aquí va su código
        DialogoEditaEstudiante dialogo;
        try {
            dialogo = new DialogoEditaEstudiante(escenario, null);
            dialogo.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /* Edita un estudiante. */
    @FXML
    private void editaEstudiante(ActionEvent evento) {
        // Aquí va su código.

        DialogoEditaEstudiante dialogo;
        try {
            dialogo = new DialogoEditaEstudiante(escenario, null);
            dialogo.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Elimina uno o varios estudiantes. */
    @FXML
    private void eliminaEstudiantes(ActionEvent evento) {

        Lista<Estudiante> aEliminar = new Lista<Estudiante>();
        for (TablePosition tp : seleccion)
            aEliminar.agregaFinal(renglones.get(tp.getRow()));
        modeloSeleccion.clearSelection();
        for (Estudiante estudiante : aEliminar)
            bdd.eliminaRegistro(estudiante);

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

            // update selected cells to the matching students
            seleccion.clear();
            for (Estudiante e : estudiantes) {
                seleccion.add(new TablePosition<Estudiante, String>(tabla, renglones.indexOf(e), null));
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
        dialogo.setTitle("Administrador de Estudiantes.");
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

    /* Carga la base de datos de un archivo. */
    private void cargaBaseDeDatosDeArchivo(File archivo) {
        // Aquí va su código.
        BaseDeDatosEstudiantes nbdd = new BaseDeDatosEstudiantes();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(archivo)));
            nbdd.carga(in);

            in.close();
        } catch (IOException ioe) {
            String mensaje = String.format("Ocurrió un error al tratar de " +
                    "cargar la base de datos en '%s'.",
                    archivo.getName());
            dialogoError("Error al cargar base de datos", mensaje);
            return;
        }
        setBaseDeDatos(nbdd);
        for (Estudiante e : nbdd.getRegistros()) {
            tabla.getItems().add(e);
        }

        this.archivo = archivo;
        setModificada(false);

    }

    /* Guarda la base de datos en un archivo. */
    private void guardaBaseDeDatosEnArchivo() {
        // Aquí va su código.
        try {
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(archivo)));
            bdd.guarda(out);
            out.close();
            setModificada(false);
            guardadoExitoso = true;
        } catch (IOException ioe) {
            String mensaje = String.format("Ocurrió un error al tratar de " +
                    "guardar la base de datos en '%s'.",
                    archivo.getPath());
            dialogoError("Error al guardar base de datos", mensaje);
            archivo = null;
            guardadoExitoso = false;
        }
    }

    /*
     * Si la base de datos ha sido modificada, muestra un diálogo preguntando al
     * usuario si quiere guardarla.
     */
    private boolean verificaGuardada(String pregunta) {
        // Aquí va su código.
        if (guardadoExitoso == false) {

            Boolean result = dialogoDeConfirmacion("Guardar", "Los cambios no se guardaran si no guarda",
                    pregunta,
                    "Guardar", "No guardar");

            if (result == true) {
                guardaBaseDeDatosEnArchivo();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /* Actualiza la interfaz con una nueva base de datos. */
    private void setBaseDeDatos(BaseDeDatosEstudiantes bdd) {
        // Aquí va su código.
        this.bdd = bdd;
    }

    /* Actualiza la interfaz para mostrar que el archivo ha sido modificado. */
    private void setModificada(boolean modificado) {
        // Aquí va su código.

    }

    /* Maneja un evento de cambio en la base de datos. */
    private void eventoBaseDeDatos(EventoBaseDeDatos evento,
            Estudiante estudiante1,
            Estudiante estudiante2) {
        // Aquí va su código.
        setModificada(true);

        switch (evento) {
            case BASE_LIMPIADA:
                tabla.getItems().clear();
                break;
            case REGISTRO_AGREGADO:
                tabla.getItems().add(estudiante1);

                break;
            case REGISTRO_ELIMINADO:
                tabla.getItems().remove(estudiante1);

                break;
            case REGISTRO_MODIFICADO:
                /* Los escuchas de Estudiante se hacen cargo. */

                tabla.refresh();

                break;
        }

    }

    /*
     * Actualiza la interfaz dependiendo del número de renglones
     * seleccionados.
     */
    private void cambioSeleccion() {
        // Aquí va su código.

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

    /* Muestra un diálogo de error. */
    private void dialogoError(String titulo, String mensaje) {
        // Aquí va su código.
        Alert dialogo = new Alert(AlertType.ERROR);
        dialogo.initOwner(escenario);
        dialogo.initModality(Modality.WINDOW_MODAL);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(null);
        dialogo.setContentText(mensaje);
        dialogo.showAndWait();

    }
}
