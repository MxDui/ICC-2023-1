package mx.unam.ciencias.icc.igu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import mx.unam.ciencias.icc.CampoEstudiante;

/**
 * Clase para el controlador del contenido del diálogo para buscar estudiantes.
 */
public class ControladorFormaBuscaEstudiantes
        extends ControladorFormaEstudiante {

    /* El combo del campo. */
    @FXML
    private ComboBox<CampoEstudiante> opcionesCampo;
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
            case NOMBRE:
                return verificaNombre(valor);
            case CUENTA:
                return verificaCuenta(valor);
            case PROMEDIO:
                return verificaPromedio(valor);
            case EDAD:
                return verificaEdad(valor);
            default:
                return false;
        }
    }

    /* Obtiene la pista. */
    private Tooltip getTooltip() {
        // Aquí va su código.
        String t = "";
        switch (opcionesCampo.getValue()) {
            case NOMBRE:
                t = "Buscar por nombre necesita al menos un carácter";
                break;
            case CUENTA:
                t = "Buscar por cuenta necesita un número entre " +
                        "1000000 y 99999999";
                break;
            case PROMEDIO:
                t = "Buscar por promedio necesita un número entre 0.0 y 10.0";
                break;
            case EDAD:
                t = "Buscar por edad necesita un número entre 13 y 99";
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
            case NOMBRE:
                return e;
            case CUENTA:
                return Integer.valueOf(e);
            case PROMEDIO:
                return Double.valueOf(e);
            case EDAD:
                return Integer.valueOf(e);
            default:
                return null;
        }
    }

    /**
     * Regresa el campo seleccionado.
     * 
     * @return el campo seleccionado.
     */
    public CampoEstudiante getCampo() {
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
