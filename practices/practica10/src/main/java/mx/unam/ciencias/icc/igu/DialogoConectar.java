package mx.unam.ciencias.icc.igu;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase para diálogos con formas de conexión.
 */
public class DialogoConectar extends Stage {

    /* Vista de la forma para realizar búsquedas de estudiantes. */
    private static final String CONECTAR_FXML = "fxml/forma-conectar.fxml";

    /* El controlador. */
    private ControladorFormaConectar controlador;

    /**
     * Define el estado inicial de un diálogo para búsquedas de estudiantes.
     * 
     * @param escenario el escenario al que el diálogo pertenece.
     * @throws IOException si no se puede cargar el archivo FXML.
     */
    public DialogoConectar(Stage escenario) throws IOException {
        // Aquí va su código.

        try {
            ClassLoader cl = getClass().getClassLoader();
            FXMLLoader cargador = new FXMLLoader(cl.getResource(CONECTAR_FXML));
            AnchorPane root = cargador.load();
            controlador = cargador.getController();
            Scene scene = new Scene(root);
            setScene(scene);
            initModality(Modality.WINDOW_MODAL);
            initOwner(escenario);

            controlador.setEscenario(this);
            escenario.setOnShown(w -> controlador.defineFoco());

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
     * Regresa la dirección del diálogo.
     * 
     * @return la dirección del diálogo.
     */
    public String getDireccion() {
        // Aquí va su código.
        return controlador.getDireccion();
    }

    /**
     * Regresa el puerto del diálogo.
     * 
     * @return el puerto del diálogo.
     */
    public int getPuerto() {
        // Aquí va su código.
        return controlador.getPuerto();
    }
}
