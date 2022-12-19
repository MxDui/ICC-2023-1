package mx.unam.ciencias.icc;

public enum CampoLaptop {

    MODELO,
    MARCA,
    PRECIO,
    PROCESADOR,
    RAM,
    ALMACENAMIENTO;

    @Override
    public String toString() {
        switch (this) {
            case MODELO:
                return "Modelo";
            case MARCA:
                return "Marca";
            case PRECIO:
                return "Precio";
            case PROCESADOR:
                return "Procesador";
            case RAM:
                return "RAM";
            case ALMACENAMIENTO:
                return "Almacenamiento";
            default:
                return "";
        }
    }
}
