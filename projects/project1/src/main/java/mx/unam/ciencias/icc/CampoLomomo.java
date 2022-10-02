package mx.unam.ciencias.icc;

/**
 * Enumeración para los campos de un {@link Lomomo}.
 */

public enum CampoLomomo {
    /** El nombre del lomomo */
    NOMBRE,
    /** La raza del lomomo */
    RAZA,
    /** El color del lomomo */
    COLOR,
    /** La edad del lomomo */
    EDAD,
    /** El peso del lomomo */
    PESO;

    /**
     * Regresa una representación en cadena del campo para ser usada en
     * interfaces gráficas.
     * 
     * @return una representación en cadena del campo.
     */
    @Override
    public String toString() {
        switch (this) {
            case NOMBRE:
                return "Nombre";
            case RAZA:
                return "Raza";
            case COLOR:
                return "Color";
            case EDAD:
                return "Edad";
            case PESO:
                return "Peso";
            default:
                return "";
        }
    }
}
