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

            String linea = br.readLine();

            while (linea != null) {
                lista.agregaFinal(new Registro(linea));
                linea = br.readLine();
            }

            br.close();

            lista = lista.mergeSort(
                    (a, b) -> a.getLinea().codePointAt(0) - b.getLinea().codePointAt(0));

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), "UTF-8"));

            for (Registro r : lista.reversa()) {
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
            for (String archivo : archivos) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "UTF-8"));
                String linea = br.readLine();
                while (linea != null) {
                    bw.write(linea);
                    bw.newLine();
                    linea = br.readLine();
                }
                br.close();
            }
            bw.close();
        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo.");
            System.exit(1);
        }
    }
}
