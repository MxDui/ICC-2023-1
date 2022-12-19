package mx.unam.ciencias.icc;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import mx.unam.ciencias.icc.red.ServidorBaseDeDatosLaptops;

public class ServidorProyecto3 {
    private static void uso() {
        System.out.println("Uso: java -jar servidor-proyecto3.jar " +
                "puerto [archivo]");
        System.exit(0);
    }

    private static void bitacora(String formato, Object... argumentos) {
        ZonedDateTime now = ZonedDateTime.now();
        String ts = now.format(DateTimeFormatter.RFC_1123_DATE_TIME);
        System.err.printf(ts + " " + formato + "\n", argumentos);
    }

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2)
            uso();

        int puerto = 1234;
        try {
            puerto = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            uso();
        }

        String archivo = (args.length == 2) ? args[1] : null;

        try {
            ServidorBaseDeDatosLaptops servidor;
            servidor = new ServidorBaseDeDatosLaptops(puerto, archivo);
            servidor.agregaEscucha((f, p) -> bitacora(f, p));
            System.out.println("Servidor iniciado.");
            servidor.sirve();

        } catch (IOException ioe) {
            bitacora("Error al crear el servidor.");
        }
    }

}
