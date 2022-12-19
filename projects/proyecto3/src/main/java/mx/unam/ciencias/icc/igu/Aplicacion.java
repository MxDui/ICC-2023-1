package mx.unam.ciencias.icc.igu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Clase para aplicaciones con interfaz gráfica de la base de datos.
 */
public class Aplicacion extends Application {

    /* Vista de la interfaz estudiantes. */
    private static final String INTERFAZ_LAPTOPS_FXML = "/fxml/interfaz-estudiantes.fxml";
    /* Ícono de la Facultad de Ciencias. */
    private static final String ICONO_CIENCIAS = "icons/ciencias.png";

    /**
     * Inicia la aplicación.
     * 
     * @param escenario la ventana principal de la aplicación.
     * @throws Exception si algo sale mal.
     */
    @Override
    public void start(Stage escenario) throws Exception {
        // Aquí va su código.

        FXMLLoader cargador = new FXMLLoader();
        cargador.setLocation(
                getClass().getResource(INTERFAZ_LAPTOPS_FXML));
        BorderPane raiz = cargador.load();
        // ControladorInterfazEstudiantes controlador = cargador.getController();
        // controlador.setEscenario(escenario);

        // Scene escena = new Scene(raiz);
        // escenario.setScene(escena);
        // escenario.setTitle("Base de datos de estudiantes");
        // escenario.getIcons().add(new Image(ICONO_CIENCIAS));
        // escenario.show();
    }
}