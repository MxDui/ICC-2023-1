package mx.unam.ciencias.icc.proyecto2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Invertidor {

    public static void invertirOrden(String archivo) {
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "UTF-8"));

            Lista<Registro> lista = new Lista<Registro>();
            Lista<Registro> orderLista = new Lista<Registro>();

            String linea = br.readLine();

            while (linea != null) {
                lista.agregaFinal(new Registro(linea));
                linea = br.readLine();
            }

            br.close();

            orderLista = lista.mergeSort((a, b) -> a.getLinea().trim().replaceAll("[^a-zA-Z]", "").toLowerCase()
                    .compareTo(b.getLinea().replaceAll("[^a-zA-Z]", "").toLowerCase()));

            // write to the same file
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), "UTF-8"));

            for (Registro r : orderLista.reversa()) {
                bw.write(r.getLinea());
                bw.newLine();
            }

            bw.close();

        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo.");
            System.exit(1);
        }
    }

    public static void invertirOrden(String[] archivos, String archivoSalida) {
        try {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(archivoSalida), "UTF-8"));

            Lista<Registro> lista = new Lista<Registro>();
            Lista<Registro> orderLista = new Lista<Registro>();

            for (String archivo : archivos) {

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(new FileInputStream(archivo), "UTF-8"));

                String linea = br.readLine();

                while (linea != null) {
                    lista.agregaFinal(new Registro(linea));
                    linea = br.readLine();
                }
                br.close();
            }

            orderLista = lista.mergeSort((a, b) -> a.getLinea().trim().replaceAll("[^a-zA-Z]", "").toLowerCase()
                    .compareTo(b.getLinea().replaceAll("[^a-zA-Z]", "").toLowerCase()));

            for (Registro r : orderLista.reversa()) {
                bw.write(r.getLinea());
                bw.newLine();
            }

            bw.close();

        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo.");
            System.exit(1);
        }
    }
}
