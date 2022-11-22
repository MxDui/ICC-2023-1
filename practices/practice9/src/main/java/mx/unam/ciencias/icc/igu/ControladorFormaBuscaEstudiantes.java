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
        opcionesCampo.getItems().addAll(CampoEstudiante.values());
    }

    /* Revisa el valor después de un cambio. */
    @FXML
    private void revisaValor(ActionEvent evento) {
        // Aquí va su código.
        CampoEstudiante campo = opcionesCampo.getValue();
        if (campo == null) {
            entradaValor.setTooltip(new Tooltip("Seleccione un campo"));
            entradaValor.esValida();
            return;
        }
    }

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML
    private void aceptar(ActionEvent evento) {
        // Aquí va su código.
        if (opcionesCampo.getValue() == null) {
            entradaValor.setTooltip(new Tooltip("Seleccione un campo"));
            entradaValor.esValida();
            return;
        }

    }

    /* Verifica el valor. */
    private boolean verificaValor(String valor) {
        // Aquí va su código.
        CampoEstudiante campo = opcionesCampo.getValue();
        if (campo == null) {
            entradaValor.setTooltip(new Tooltip("Seleccione un campo"));
            entradaValor.esValida();
            return false;
        }
        switch (campo) {
            case NOMBRE:
                return valor != null && !valor.isEmpty();
            case CUENTA:
                try {
                    Integer.parseInt(valor);
                    return true;
                } catch (NumberFormatException nfe) {
                    return false;
                }
            case PROMEDIO:
                try {
                    Double.parseDouble(valor);
                    return true;
                } catch (NumberFormatException nfe) {
                    return false;
                }
            case EDAD:
                try {
                    Integer.parseInt(valor);
                    return true;
                } catch (NumberFormatException nfe) {
                    return false;
                }
            default:
                return false;
        }

    }

    /* Obtiene la pista. */
    private Tooltip getTooltip() {
        // Aquí va su código.
        CampoEstudiante campo = opcionesCampo.getValue();
        if (campo == null)
            return new Tooltip("Seleccione un campo");
        switch (campo) {
            case CUENTA:
                return new Tooltip("Ingrese una matrícula");
            case NOMBRE:
                return new Tooltip("Ingrese un nombre");
            case PROMEDIO:
                return new Tooltip("Ingrese un promedio");
            case EDAD:
                return new Tooltip("Ingrese una edad");
        }
        return null;

    }

    /**
     * Regresa el valor ingresado.
     * 
     * @return el valor ingresado.
     */
    public Object getValor() {
        // Aquí va su código.
        CampoEstudiante campo = opcionesCampo.getValue();
        if (campo == null)
            return null;
        switch (campo) {
            case CUENTA:
                return entradaValor.getText();
            case NOMBRE:
                return entradaValor.getText();
            case PROMEDIO:
                return Double.parseDouble(entradaValor.getText());
            case EDAD:
                return Integer.parseInt(entradaValor.getText());
        }
        return null;
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
        opcionesCampo.requestFocus();
    }
}
