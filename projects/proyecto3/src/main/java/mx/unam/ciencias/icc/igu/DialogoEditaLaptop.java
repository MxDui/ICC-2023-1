package mx.unam.ciencias.icc.igu;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.Laptop;

/**
 * Clase para diálogos con formas para editar laptops.
 */
public class DialogoEditaLaptop extends Stage {

    /* Vista de la forma para agregar/editar laptops. */
    private static final String EDITA_LAPTOP_FXML = "fxml/forma-edita-laptop.fxml";

    /* El controlador. */
    private ControladorFormaEditaLaptop controlador;

    /**
     * Define el estado inicial de un diálogo para laptop.
     * 
     * @param escenario el escenario al que el diálogo pertenece.
     * @param laptop    el laptop; puede ser <code>null</code> para agregar
     *                  un laptop.
     * @throws IOException si no se puede cargar el archivo FXML.
     */
    public DialogoEditaLaptop(Stage escenario,
            Laptop laptop) throws IOException {
        // Aquí va su código.
        try {
            ClassLoader cl = getClass().getClassLoader();
            FXMLLoader cargador = new FXMLLoader(cl.getResource(EDITA_LAPTOP_FXML));
            AnchorPane root = cargador.load();
            controlador = cargador.getController();
            controlador.setLaptop(laptop);
            Scene scene = new Scene(root);
            setScene(scene);

            controlador.setEscenario(this);
            controlador.setLaptop(laptop);
            if (laptop == null) {
                setTitle(" Agregar ␣ laptop ");
                controlador.setVerbo(" Agregar ");
            } else {
                setTitle(" Editar ␣ laptop ");
                controlador.setVerbo(" Actualizar ");
            }
            setOnShown(w -> controlador.defineFoco());
            setResizable(false);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
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
     * Regresa el laptop del diálogo.
     * 
     * @return el laptop del diálogo.
     */
    public Laptop getLaptop() {
        // Aquí va su código.
        return controlador.getLaptop();
    }
}