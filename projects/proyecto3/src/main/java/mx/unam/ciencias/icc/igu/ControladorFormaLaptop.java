package mx.unam.ciencias.icc.igu;

import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public abstract class ControladorFormaLaptop {
    /** El botón para aceptar. */
    @FXML
    protected Button botonAceptar;

    /** La ventana del diálogo. */
    protected Stage escenario;
    /** Si el usuario aceptó la forma. */
    protected boolean aceptado;

    protected String marca;
    protected String modelo;
    protected double precio;
    protected String procesador;
    protected int ram;
    protected int almacenamiento;

    @FXML
    protected void cancelar(ActionEvent evento) {
        // Aquí va su código.
        aceptado = false;

        // close window modal in the stage
        escenario.close();
    }

    /**
     * Define el escenario del diálogo.
     * 
     * @param escenario el nuevo escenario del diálogo.
     */
    public void setEscenario(Stage escenario) {
        // Aquí va su código.
        this.escenario = escenario;
        Scene escena = escenario.getScene();
        KeyCodeCombination combinacion;
        combinacion = new KeyCodeCombination(KeyCode.ENTER,
                KeyCombination.CONTROL_DOWN);
        ObservableMap<KeyCombination, Runnable> accs = escena.getAccelerators();
        accs.put(combinacion, () -> botonAceptar.fire());
    }

    /**
     * Nos dice si el usuario activó el botón de aceptar.
     * 
     * @return <code>true</code> si el usuario activó el botón de aceptar,
     *         <code>false</code> en otro caso.
     */
    public boolean isAceptado() {
        // Aquí va su código.
        return aceptado;

    }

    /**
     * Define el foco incial del diálogo.
     */
    public abstract void defineFoco();

    protected boolean verificaModelo(String modelo) {
        String temp = modelo.trim();
        if (temp.length() > 0) {
            this.modelo = temp;
            return true;
        }
        return false;
    }

    protected boolean verificaMarca(String marca) {
        String temp = marca.trim();
        if (temp.length() > 0) {
            this.marca = temp;
            return true;
        }
        return false;
    }

    protected boolean verificaPrecio(String precio) {
        try {
            double temp = Double.parseDouble(precio);
            if (temp > 5000 && temp < 100000) {
                this.precio = temp;
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    protected boolean verificaProcesador(String procesador) {
        String temp = procesador.trim();
        if (temp.length() > 0) {
            this.procesador = temp;
            return true;
        }
        return false;
    }

    protected boolean verificaRam(String ram) {
        try {
            int temp = Integer.parseInt(ram);
            if (temp > 4 && temp < 128) {
                this.ram = temp;
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    protected boolean verificaAlmacenamiento(String almacenamiento) {
        try {
            int temp = Integer.parseInt(almacenamiento);
            if (temp > 256 && temp < 10000) {
                this.almacenamiento = temp;
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

}
