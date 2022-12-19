package mx.unam.ciencias.icc.igu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import mx.unam.ciencias.icc.CampoLaptop;

/**
 * Clase para el controlador del contenido del diálogo para buscar laptops.
 */
public class ControladorFormaBuscaLaptops
        extends ControladorFormaLaptop {

    /* El combo del campo. */
    @FXML
    private ComboBox<CampoLaptop> opcionesCampo;
    /* El campo de texto para el valor. */
    @FXML
    private EntradaVerificable entradaValor;

    /* Inicializa el estado de la forma. */
    @FXML
    private void initialize() {
        // Aquí va su código.
        entradaValor.setVerificador(s -> verificaValor(s));
        entradaValor.textProperty().addListener(
                (o, v, n) -> revisaValor(null));
    }

    /* Revisa el valor después de un cambio. */
    @FXML
    private void revisaValor(ActionEvent evento) {
        // Aquí va su código.
        Tooltip.install(entradaValor, getTooltip());
        botonAceptar.setDisable(!entradaValor.esValida());
    }

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML
    private void aceptar(ActionEvent evento) {
        // Aquí va su código.
        aceptado = true;

        // close the scene
        escenario.close();
    }

    /* Verifica el valor. */
    private boolean verificaValor(String valor) {
        // Aquí va su código.
        switch (opcionesCampo.getValue()) {
            case MARCA:
                return verificaMarca(valor);
            case MODELO:
                return verificaModelo(valor);
            case PRECIO:
                return verificaPrecio(valor);
            case RAM:
                return verificaRam(valor);
            case ALMACENAMIENTO:
                return verificaAlmacenamiento(valor);
            case PROCESADOR:
                return verificaProcesador(valor);

            default:
                return false;
        }
    }

    /* Obtiene la pista. */
    private Tooltip getTooltip() {
        // Aquí va su código.
        String t = "";
        switch (opcionesCampo.getValue()) {
            case MARCA:
                t = "Buscar por marca necesita una cadena de texto";
                break;
            case MODELO:
                t = "Buscar por modelo necesita una cadena de texto";
                break;
            case PRECIO:
                t = "Buscar por precio necesita un número mayor a 5000 y menor a 100000";
                break;
            case RAM:
                t = "Buscar por ram necesita un número mayor a 4 y menor a 128";
                break;
            case ALMACENAMIENTO:
                t = "Buscar por almacenamiento necesita un número mayor a 256 y menor a 10000";
                break;
            case PROCESADOR:
                t = "Buscar por procesador necesita una cadena de texto";
                break;

        }
        return new Tooltip(t);
    }

    /**
     * Regresa el valor ingresado.
     * 
     * @return el valor ingresado.
     */
    public Object getValor() {
        // Aquí va su código.
        String e = entradaValor.getText();

        switch (opcionesCampo.getValue()) {
            case MARCA:
                return e;
            case MODELO:
                return e;
            case PRECIO:
                return Double.parseDouble(e);
            case RAM:
                return Integer.parseInt(e);
            case ALMACENAMIENTO:
                return Integer.parseInt(e);
            case PROCESADOR:
                return e;
            default:
                return null;
        }
    }

    /**
     * Regresa el campo seleccionado.
     * 
     * @return el campo seleccionado.
     */
    public CampoLaptop getCampo() {
        // Aquí va su código.
        return opcionesCampo.getValue();
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override
    public void defineFoco() {
        // Aquí va su código.
        entradaValor.requestFocus();
    }
}