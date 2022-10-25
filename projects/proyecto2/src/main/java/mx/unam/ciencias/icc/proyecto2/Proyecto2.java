package mx.unam.ciencias.icc.proyecto2;

public class Proyecto2 {

    private static final int ERROR_USO = 1;

    private static void uso() {

        System.out.println("Uso: java -jar proyecto2.jar [opciones] archivo");
        System.out.println("Opciones: ");
        System.out.println("  -r, \t\tOrdena de forma inversa.");
        System.out.println("  -o archivo salida, \t\tExporta el archivo ordenado a un archivo.");

        System.exit(ERROR_USO);
    }

    public static void main(String[] args) {

        if (args.length != 1)
            uso();

        try {
            // Here execute the program with class Aplicacion
            
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.exit(ERROR_USO);
        } catch (Exception e) {
            System.err.println("Error inesperado.");
            System.exit(ERROR_USO);
        }

    }
}
