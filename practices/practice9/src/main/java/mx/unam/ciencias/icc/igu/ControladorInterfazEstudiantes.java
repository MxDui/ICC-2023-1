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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
        renglones = tabla.getItems();
        modeloSeleccion = tabla.getSelectionModel();
        modeloSeleccion.setSelectionMode(SelectionMode.MULTIPLE);
        seleccion = modeloSeleccion.getSelectedCells();
        ListChangeListener<TablePosition> lcl = c -> cambioSeleccion();
        seleccion.addListener(lcl);
        cambioSeleccion();

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
        FileChooser fc = new FileChooser();

        fc.setTitle("Guardar Base de Datos como...");
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("Bases de datos", "*.bd"),
                new ExtensionFilter("Todos los archivos", "*.*"));
        archivo = fc.showSaveDialog(escenario);

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

    /**
     * Termina el programa.
     * 
     * @param evento el evento que generó la acción.
     */
    @FXML
    public void salir(Event evento) {
        // Aquí va su código.
        if (verificaGuardada("¿ Desea ␣ guardarla ␣ antes ␣de␣ salir ?")) {
            evento.consume();
            return;
        }
        Platform.exit();
    }

    /* Agrega un nuevo estudiante. */
    @FXML
    private void agregaEstudiante(ActionEvent evento) {
        // Aquí va su código
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
        bdd.modificaRegistro(estudiante, dialogo.getEstudiante());
    }

    /* Elimina uno o varios estudiantes. */
    @FXML
    private void eliminaEstudiantes(ActionEvent evento) {
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
        dialogo.setTitle(" Acerca ␣de␣ Administrador ␣" +
                "de␣ Estudiantes ");
        dialogo.setHeaderText(null);
        dialogo.setContentText(" Aplicaci ón␣ para ␣ administrar ␣" +
                " estudiantes .\n" +
                " Copyright ␣©␣ 2022 ␣ Facultad ␣" +
                "de␣ Ciencias ,␣ UNAM .");
        dialogo.showAndWait();
        tabla.requestFocus();
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
            this.archivo = archivo;
            setBaseDeDatos(nbdd);
        } catch (IOException ioe) {
            String mensaje = String.format(
                    " Ocurrió un error al tratar de cargar la base de datos " +
                            "de %s .",
                    archivo.getName());

            dialogoError(" Error al cargar base de datos ", mensaje);
        }

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
        if (this.bdd != null)
            this.bdd.limpia();
        this.bdd = bdd;
        for (Estudiante estudiante : bdd.getRegistros())
            renglones.add(estudiante);
        tabla.sort();
        bdd.agregaEscucha((e, r, s) -> eventoBaseDeDatos(e, r, s));
        setModificada(false);
    }

    /* Actualiza la interfaz para mostrar que el archivo ha sido modificado. */
    private void setModificada(boolean modificado) {
        // Aquí va su código.
        // menuGuardar.setDisable(!modificado);
        // String a = (archivo != null) ? archivo.getName() : "<Nuevo >";
        // String t = "Administrador de Estudiantes - " + a;

        // if (modificado)
        // t += "*";
        // escenario.setTitle(t);
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
                setModificada(true);
                renglones.add(estudiante1);
                tabla.sort();
                break;
            case REGISTRO_ELIMINADO:
                setModificada(true);
                renglones.remove(estudiante1);
                tabla.sort();
                break;
            case REGISTRO_MODIFICADO:
                setModificada(true);
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

    /* Muestra un diálogo de error. */
    private void dialogoError(String titulo, String mensaje) {
        // Aquí va su código.
        Alert dialogo = new Alert(AlertType.ERROR);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(null);
        dialogo.setContentText(mensaje);
        dialogo.showAndWait();
        tabla.requestFocus();

    }
}
