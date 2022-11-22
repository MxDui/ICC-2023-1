package mx.unam.ciencias.icc.igu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mx.unam.ciencias.icc.Estudiante;

/**
 * Clase para el controlador del contenido del diálogo para editar y crear
 * estudiantes.
 */
public class ControladorFormaEditaEstudiante
        extends ControladorFormaEstudiante {

    /* La entrada verificable para el nombre. */
    @FXML
    private EntradaVerificable entradaNombre;
    /* La entrada verificable para el número de cuenta. */
    @FXML
    private EntradaVerificable entradaCuenta;
    /* La entrada verificable para el promedio. */
    @FXML
    private EntradaVerificable entradaPromedio;
    /* La entrada verificable para la edad. */
    @FXML
    private EntradaVerificable entradaEdad;

    /* El estudiante creado o editado. */
    private Estudiante estudiante;

    /* Inicializa el estado de la forma. */
    @FXML
    private void initialize() {
        // Aquí va su código.
        entradaNombre.setVerificador((String s) -> {
            return s != null && !s.isEmpty();
        });
    }

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML
    private void aceptar(ActionEvent evento) {
        // Aquí va su código.
        if (entradaNombre.esValida() && entradaCuenta.esValida() && entradaPromedio.esValida()
                && entradaEdad.esValida()) {
            estudiante = new Estudiante(entradaNombre.getText(), Integer.parseInt(entradaCuenta.getText()),
                    Double.parseDouble(entradaPromedio.getText()), Integer.parseInt(entradaEdad.getText()));

        }
    }

    /* Actualiza al estudiante, o lo crea si no existe. */
    private void actualizaEstudiante() {
        // Aquí va su código.
        if (estudiante == null) {
            estudiante = new Estudiante(entradaNombre.getText(), Integer.parseInt(entradaCuenta.getText()),
                    Double.parseDouble(entradaPromedio.getText()), Integer.parseInt(entradaEdad.getText()));
        } else {
            estudiante.setNombre(entradaNombre.getText());
            estudiante.setCuenta(Integer.parseInt(entradaCuenta.getText()));
            estudiante.setPromedio(Double.parseDouble(entradaPromedio.getText()));
            estudiante.setEdad(Integer.parseInt(entradaEdad.getText()));
        }
    }

    /**
     * Define el estudiante del diálogo.
     * 
     * @param estudiante el nuevo estudiante del diálogo.
     */
    public void setEstudiante(Estudiante estudiante) {
        // Aquí va su código.
        this.estudiante = estudiante;
    }

    /**
     * Regresa el estudiante del diálogo.
     * 
     * @return el estudiante del diálogo.
     */
    public Estudiante getEstudiante() {
        // Aquí va su código.
        return estudiante;
    }

    /**
     * Define el verbo del botón de aceptar.
     * 
     * @param verbo el nuevo verbo del botón de aceptar.
     */
    public void setVerbo(String verbo) {
        // Aquí va su código.
        botonAceptar.setText(verbo);
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override
    public void defineFoco() {
        // Aquí va su código.
        entradaNombre.requestFocus();
    }

    /* Verifica que los cuatro campos sean válidos. */
    private void verificaEstudiante() {
        // Aquí va su código.
        if (entradaNombre.esValida() && entradaCuenta.esValida() && entradaPromedio.esValida()
                && entradaEdad.esValida()) {
            botonAceptar.setDisable(false);
        } else {
            botonAceptar.setDisable(true);
        }
    }

    /**
     * Verifica que el número de cuenta sea válido.
     * 
     * @param cuenta el número de cuenta a verificar.
     * @return <code>true</code> si el número de cuenta es válido;
     *         <code>false</code> en otro caso.
     */
    @Override
    protected boolean verificaCuenta(String cuenta) {
        // Aquí va su código.
        try {
            int c = Integer.parseInt(cuenta);
            if (c < 0) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Verifica que el promedio sea válido.
     * 
     * @param promedio el promedio a verificar.
     * @return <code>true</code> si el promedio es válido; <code>false</code> en
     *         otro caso.
     */
    @Override
    protected boolean verificaPromedio(String promedio) {
        // Aquí va su código.
        try {
            double p = Double.parseDouble(promedio);
            if (p < 0 || p > 10) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Verifica que la edad sea válida.
     * 
     * @param edad la edad a verificar.
     * @return <code>true</code> si la edad es válida; <code>false</code> en
     *         otro caso.
     */
    @Override
    protected boolean verificaEdad(String edad) {
        // Aquí va su código.
        try {
            int e = Integer.parseInt(edad);
            if (e < 0) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
