package mx.unam.ciencias.icc.igu;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.CampoEstudiante;

/**
 * Clase para diálogos con formas de búsquedas de estudiantes.
 */
public class DialogoBuscaEstudiantes extends Stage {

    /* Vista de la forma para realizar búsquedas de estudiantes. */
    private static final String BUSCA_ESTUDIANTES_FXML = "mx/unam/ciencias/icc/forma-busca-estudiantes.fxml";

    /* El controlador. */
    private ControladorFormaBuscaEstudiantes controlador;

    /**
     * Define el estado inicial de un diálogo para búsquedas de estudiantes.
     * 
     * @param escenario el escenario al que el diálogo pertenece.
     * @throws IOException si no se puede cargar el archivo FXML.
     */
    public DialogoBuscaEstudiantes(Stage escenario) throws IOException {
        // Aquí va su código.
        try {
            escenario.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(BUSCA_ESTUDIANTES_FXML));
            AnchorPane root = loader.load();
            controlador = loader.getController();
            Scene scene = new Scene(root);
            setScene(scene);
        } catch (IOException ioe) {
            throw new IOException();
        }

    }

    /**
     * Nos dice si el usuario activó el botón de aceptar.
     * 
     * @return <code>true</code> si el usuario activó el botón de aceptar,
     *         <code>false</code> en otro caso.
     */
    public boolean isAceptado() {
        // Aquí va su código.
        return controlador.isAceptado();
    }

    /**
     * Regresa el campo seleccionado.
     * 
     * @return el campo seleccionado.
     */
    public CampoEstudiante getCampo() {
        // Aquí va su código.
        return controlador.getCampo();
    }

    /**
     * Regresa el valor ingresado.
     * 
     * @return el valor ingresado.
     */
    public Object getValor() {
        // Aquí va su código.
        return controlador.getValor();
    }
}
