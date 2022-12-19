package mx.unam.ciencias.icc.igu;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.CampoLaptop;

/**
 * Clase para diálogos con formas de búsquedas de laptops.
 */
public class DialogoBuscaLaptops extends Stage {

    /* Vista de la forma para realizar búsquedas de laptops. */
    private static final String BUSCA_LAPTOPS_FXML = "fxml/forma-busca-laptops.fxml";

    /* El controlador. */
    private ControladorFormaBuscaLaptops controlador;

    /**
     * Define el estado inicial de un diálogo para búsquedas de laptops.
     * 
     * @param escenario el escenario al que el diálogo pertenece.
     * @throws IOException si no se puede cargar el archivo FXML.
     */
    public DialogoBuscaLaptops(Stage escenario) throws IOException {
        // Aquí va su código.
        try {
            ClassLoader cl = getClass().getClassLoader();
            FXMLLoader cargador = new FXMLLoader(cl.getResource(BUSCA_LAPTOPS_FXML));
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
     * Regresa el campo seleccionado.
     * 
     * @return el campo seleccionado.
     */
    public CampoLaptop getCampo() {
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
