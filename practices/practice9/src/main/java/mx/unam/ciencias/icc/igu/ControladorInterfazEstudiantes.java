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

        setModificada(false);
        setBaseDeDatos(new BaseDeDatosEstudiantes());
    }

    /* Crea una nueva base de datos. */
    @FXML
    private void nuevaBaseDeDatos(ActionEvent evento) {
        // Aquí va su código.
        if (!verificaGuardada("¿Desea guardarla antes de crear una nueva?"))
            return;
        archivo = null;
        setBaseDeDatos(new BaseDeDatosEstudiantes());
        setModificada(false);
    }

    /* Carga una base de datos. */
    @FXML
    private void cargaBaseDeDatos(ActionEvent evento) {
        // Aquí va su código.
        if (!verificaGuardada("¿Desea guardarla antes de cargar otra?"))
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
        try {

            DialogoBuscaEstudiantes dialogo = new DialogoBuscaEstudiantes(escenario);

            setEscenario(dialogo);

            // escenario.setOnShown(w -> controlador.defineFoco());
            // escenario.setResizable(false);
            // escenario.showAndWait();
            // controladorTablaEstudiantes.enfocaTabla();
            // if (!controlador.isAceptado())
            // return;

            // Lista<Estudiante> resultados = bdd.buscaRegistros(controlador.getCampo(),
            // controlador.getValor());

            // controladorTablaEstudiantes.seleccionaRenglones(resultados);
        } catch (IOException ioe) {

        }

    }

    /* Muestra un diálogo con información del programa. */
    @FXML
    private void acercaDe(ActionEvent evento) {
        // Aquí va su código.
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Acerca de");
        alerta.setHeaderText("Base de Datos de Estudiantes");

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
        guardadoExitoso = true;
        if (pregunta == null) {
            if (dialogoDeConfirmacion("Base de datos modificada",
                    "La base de datos ha sido modificada.",
                    pregunta,
                    "Guardar cambios", "Ignorar cambios"))
                guardaBaseDeDatos(null);
        }
        return guardadoExitoso;
    }

    /* Actualiza la interfaz con una nueva base de datos. */
    private void setBaseDeDatos(BaseDeDatosEstudiantes bdd) {
        // Aquí va su código.
    }

    /* Actualiza la interfaz para mostrar que el archivo ha sido modificado. */
    private void setModificada(boolean modificado) {
        // Aquí va su código.
        setModificada(modificado);
        menuGuardar.setDisable(!modificado);
    }

    /* Maneja un evento de cambio en la base de datos. */
    private void eventoBaseDeDatos(EventoBaseDeDatos evento,
            Estudiante estudiante1,
            Estudiante estudiante2) {
        // Aquí va su código.
        switch (evento) {
            case BASE_LIMPIADA:

                break;
            case REGISTRO_AGREGADO:

                break;
            case REGISTRO_ELIMINADO:

                break;
            case REGISTRO_MODIFICADO:
                /* Los escuchas de Estudiante se hacen cargo. */
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
    }

    /* Muestra un diálogo de error. */
    private void dialogoError(String titulo, String mensaje) {
        // Aquí va su código.
    }
}
